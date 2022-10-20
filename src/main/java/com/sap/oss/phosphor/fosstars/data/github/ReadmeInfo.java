package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_README;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.INCOMPLETE_README;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.util.Deserialization.readListFrom;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * This data provider gathers info about project's README file.
 * It fills out {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#HAS_README}.
 */
public class ReadmeInfo extends GitHubCachingDataProvider {

  /**
   * A list of known README file names.
   */
  private static final List<String> KNOWN_README_FILES
      = Arrays.asList("README", "README.txt", "README.md", "README.rst",
          "README.adoc", "readme", "readme.txt", "readme.md", "readme.rst", 
          "readme.adoc", "README.MD", "readme.MD");

  /**
   * A list of patterns that describe required content in README.
   */
  private final List<Pattern> requiredContentPatterns = new ArrayList<>();

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If the provider could not load a default config.
   */
  public ReadmeInfo(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
    loadDefaultConfigIfAvailable();
  }

  /**
   * Return a list of patterns that describe required content in README.
   *
   * @return A list of patterns.
   */
  List<Pattern> requiredContentPatterns() {
    return new ArrayList<>(requiredContentPatterns);
  }

  /**
   * Set a list of patterns that describe required content in README.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public ReadmeInfo requiredContentPatterns(String... patterns) {
    return requiredContentPatterns(asList(patterns));
  }

  /**
   * Set a list of patterns that describe required content in README.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public ReadmeInfo requiredContentPatterns(List<String> patterns) {
    Objects.requireNonNull(patterns, "Oops! Patterns can't be null!");
    requiredContentPatterns.clear();
    requiredContentPatterns.addAll(
        patterns.stream()
            .map(pattern -> Pattern.compile(pattern, Pattern.DOTALL)).collect(toList()));
    return this;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(HAS_README, INCOMPLETE_README);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Gathering info about project's README file ...");
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    Optional<String> readme = readReadmeIn(repository);

    if (!readme.isPresent()) {
      return ValueHashSet.from(
          HAS_README.value(false).explain("No README file found"),
          INCOMPLETE_README.value(true).explain("The project should have a README file"));
    }

    Value<Boolean> hasReadme = HAS_README.value(true);

    List<Pattern> missedPatterns = requiredContentPatterns.stream()
        .filter(pattern -> !pattern.matcher(readme.get()).find())
        .collect(toList());
    Value<Boolean> incompleteReadme = INCOMPLETE_README.value(!missedPatterns.isEmpty())
        .explainIf(true, "The README does not contain required text that should match %s",
            missedPatterns.stream()
                .map(pattern -> format("'%s'", pattern))
                .collect(joining(", ")));

    return ValueHashSet.from(hasReadme, incompleteReadme);
  }

  /**
   * Looks for a README file in a repository.
   *
   * @param repository The repository.
   * @return A file name of README.
   */
  static Optional<String> readmeIn(LocalRepository repository) {
    for (String filename : KNOWN_README_FILES) {
      if (repository.hasFile(filename)) {
        return Optional.of(filename);
      }
    }

    return Optional.empty();
  }

  /**
   * Reads a README file in a repository.
   *
   * @param repository The repository.
   * @return Content of a README fine if found.
   * @throws IOException If something went wrong.
   */
  static Optional<String> readReadmeIn(LocalRepository repository) throws IOException {
    Optional<String> readme = readmeIn(repository);
    if (!readme.isPresent()) {
      return Optional.empty();
    }

    return repository.readTextFrom(readme.get());
  }

  /**
   * Reads a configuration from a YAML file.
   *
   * @param path A path to the YAML file.
   * @return This data provider.
   * @throws IOException If something went wrong.
   */
  @Override
  public ReadmeInfo configure(Path path) throws IOException {
    try (InputStream is = Files.newInputStream(path)) {
      return configure(is);
    }
  }

  /**
   * Reads a configuration from YAML.
   *
   * @param is An input stream with YAML.
   * @return This data provider.
   * @throws IOException If something went wrong.
   */
  ReadmeInfo configure(InputStream is) throws IOException {
    JsonNode config = Yaml.mapper().readTree(is);
    requiredContentPatterns(readListFrom(config, "requiredContentPatterns"));
    return this;
  }
}
