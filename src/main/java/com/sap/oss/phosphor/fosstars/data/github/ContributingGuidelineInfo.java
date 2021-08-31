package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_CONTRIBUTING_GUIDELINE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.util.Deserialization.readListFrom;
import static java.util.Arrays.asList;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This data provider gathers info about project's contributing guidelines.
 * It fills out the following features:
 * <ul>
 *   <li>{@link OssFeatures#HAS_CONTRIBUTING_GUIDELINE}</li>
 *   <li>{@link OssFeatures#HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE}</li>
 * </ul>
 */
public class ContributingGuidelineInfo extends GitHubCachingDataProvider {

  /**
   * A list of files that may be a contributing guideline.
   */
  private static final List<String> DEFAULT_KNOWN_CONTRIBUTING_GUIDELINE_FILES
      = asList("CONTRIBUTING", "CONTRIBUTING.md", "CONTRIBUTING.txt");

  /**
   * A list of paths to contributing guidelines.
   */
  private final List<String> knownContributingGuidelineFiles = new ArrayList<>();
  /**
   * A list of patterns that describe required content in contributing guidelines.
   */
  private final List<Pattern> requiredContentPatterns = new ArrayList<>();

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If a default config could not be loaded.
   */
  public ContributingGuidelineInfo(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
    knownContributingGuidelineFiles.addAll(DEFAULT_KNOWN_CONTRIBUTING_GUIDELINE_FILES);
    loadDefaultConfigIfAvailable();
  }

  /**
   * Set a list of file that may be a contributing guideline.
   *
   * @param files The files.
   * @return This data provider.
   */
  public ContributingGuidelineInfo knownContributingGuidelineFiles(String... files) {
    Objects.requireNonNull(files, "Oops! Files can't be null");

    if (files.length == 0) {
      throw new IllegalArgumentException("Oops! Files can't be empty!");
    }

    knownContributingGuidelineFiles.clear();
    knownContributingGuidelineFiles.addAll(asList(files));

    return this;
  }

  /**
   * Get a list of patterns that describe required content in contributing guidelines.
   *
   * @return A list of patterns that describe required content in contributing guidelines.
   */
  List<Pattern> requiredContentPatterns() {
    return new ArrayList<>(requiredContentPatterns);
  }

  /**
   * Set a list of patterns that describe required content in contributing guidelines.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public ContributingGuidelineInfo requiredContentPatterns(String... patterns) {
    return requiredContentPatterns(asList(patterns));
  }

  /**
   * Set a list of patterns that describe required content in contributing guidelines.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public ContributingGuidelineInfo requiredContentPatterns(List<String> patterns) {
    Objects.requireNonNull(patterns, "Oops! Patterns can't be null!");
    requiredContentPatterns.clear();
    requiredContentPatterns.addAll(
        patterns.stream()
            .map(pattern -> Pattern.compile(pattern, Pattern.DOTALL)).collect(Collectors.toList()));
    return this;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(HAS_CONTRIBUTING_GUIDELINE, HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Gathering info about project's contributing guidelines ...");

    Optional<String> content = lookForContributingGuideline(project);
    if (!content.isPresent()) {
      return ValueHashSet.from(
          HAS_CONTRIBUTING_GUIDELINE.value(false),
          HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE.value(false));
    }

    ValueSet values = new ValueHashSet();
    values.update(HAS_CONTRIBUTING_GUIDELINE.value(true));
    values.update(infoAboutContributingGuideline(content.get()));

    return values;
  }

  /**
   * Looks for a contributing guideline in a project.
   *
   * @param project The project.
   * @return Content of a contributing guideline if found.
   * @throws IOException If something went wrong.
   */
  private Optional<String> lookForContributingGuideline(GitHubProject project) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    for (String path : knownContributingGuidelineFiles) {
      Optional<String> content = repository.readTextFrom(path);
      if (content.isPresent()) {
        return content;
      }
    }

    return Optional.empty();
  }

  /**
   * Extract info about a contributing guideline.
   *
   * @param content Content of the contributing guideline.
   * @return A value of {@link OssFeatures#HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE}.
   */
  private Value<Boolean> infoAboutContributingGuideline(String content) {
    if (requiredContentPatterns.isEmpty()) {
      return HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE.value(true);
    }

    return HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE.value(
        requiredContentPatterns.stream().allMatch(pattern -> pattern.matcher(content).find()));
  }

  /**
   * Reads a configuration from a YAML file.
   *
   * @param path A path to the YAML file.
   * @return This data provider.
   * @throws IOException If something went wrong.
   */
  @Override
  public ContributingGuidelineInfo configure(Path path) throws IOException {
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
  ContributingGuidelineInfo configure(InputStream is) throws IOException {
    JsonNode config = Yaml.mapper().readTree(is);
    requiredContentPatterns(readListFrom(config, "requiredContentPatterns"));
    return this;
  }
}
