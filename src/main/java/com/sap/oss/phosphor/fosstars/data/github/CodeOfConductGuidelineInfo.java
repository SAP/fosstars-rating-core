package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_CODE_OF_CONDUCT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE;
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
 * This data provider gathers info about project's code of conduct. more information - https://docs.github.com/en/communities/setting-up-your-project-for-healthy-contributions/adding-a-code-of-conduct-to-your-project
 * <ul>
 *   <li>{@link OssFeatures#HAS_CODE_OF_CONDUCT}</li>
 *   <li>{@link OssFeatures#HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE}</li>
 * </ul>
 */
public class CodeOfConductGuidelineInfo extends GitHubCachingDataProvider {

  /**
   * A list of files that may be a code of conduct guideline.
   */
  private static final List<String> DEFAULT_KNOWN_CODE_OF_CONDUCT_GUIDELINE_FILES
      = asList("CODE_OF_CONDUCT", "CODE_OF_CONDUCT.md", 
          "CODE_OF_CONDUCT.txt","code_of_conduct.md",
              "code_of_conduct","code_of_conduct.txt");

  /**
   * A list of paths to code of conduct guidelines.
   */
  private final List<String> knownCodeofConductGuidelineFiles = new ArrayList<>();

  /**
   * A list of patterns that describe required content in code on conduct guidelines.
   */
  private final List<Pattern> requiredContentPatterns = new ArrayList<>();

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If a default config could not be loaded.
   */
  public CodeOfConductGuidelineInfo(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
    knownCodeofConductGuidelineFiles.addAll(DEFAULT_KNOWN_CODE_OF_CONDUCT_GUIDELINE_FILES);
    loadDefaultConfigIfAvailable();
  }

  /**
   * Set a list of file that may be a Code of conduct guideline.
   *
   * @param files The files.
   * @return This data provider.
   */
  public CodeOfConductGuidelineInfo knownCodeofConductGuidelineFiles(String... files) {
    Objects.requireNonNull(files, "Oops! Files can't be null");

    if (files.length == 0) {
      throw new IllegalArgumentException("Oops! Files can't be empty!");
    }

    knownCodeofConductGuidelineFiles.clear();
    knownCodeofConductGuidelineFiles.addAll(asList(files));

    return this;
  }

  /**
   * Get a list of patterns that describe required content in code of conduct guidelines.
   *
   * @return A list of patterns that describe required content in code of conduct guidelines.
   */
  List<Pattern> requiredContentPatterns() {
    return new ArrayList<>(requiredContentPatterns);
  }

  /**
   * Set a list of patterns that describe required content in code of conduct guidelines.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public CodeOfConductGuidelineInfo requiredContentPatterns(String... patterns) {
    return requiredContentPatterns(asList(patterns));
  }

  /**
   * Set a list of patterns that describe required content in code of conduct guidelines.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public CodeOfConductGuidelineInfo requiredContentPatterns(List<String> patterns) {
    Objects.requireNonNull(patterns, "Oops! Patterns can't be null!");
    requiredContentPatterns.clear();
    requiredContentPatterns.addAll(
        patterns.stream()
            .map(pattern -> Pattern.compile(pattern, Pattern.DOTALL)).collect(Collectors.toList()));
    return this;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(HAS_CODE_OF_CONDUCT, HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Gathering info about project's code of conduct guidelines ...");

    Optional<String> content = lookForCodeOfConductGuideline(project);
    if (!content.isPresent()) {
      return ValueHashSet.from(
        HAS_CODE_OF_CONDUCT.value(true),
        HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE.value(true));
    }

    ValueSet values = new ValueHashSet();
    values.update(HAS_CODE_OF_CONDUCT.value(true));
    values.update(infoAboutCodeOfConductGuideline(content.get()));

    return values;
  }

  /**
   * Looks for a code of conduct guideline in a project.
   *
   * @param project The project.
   * @return Content of a code of conduct guideline if found.
   * @throws IOException If something went wrong.
   */
  private Optional<String> lookForCodeOfConductGuideline(GitHubProject project) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    for (String path : knownCodeofConductGuidelineFiles) {
      Optional<String> content = repository.readTextFrom(path);
      if (content.isPresent()) {
        return content;
      }
      return content;
    }

    return Optional.empty();
  }

  /**
   * Extract info about a code of conduct guideline.
   *
   * @param content Content of the code of conduct guideline.
   * @return A value of {@link OssFeatures#HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE}.
   */
  private Value<Boolean> infoAboutCodeOfConductGuideline(String content) {
    if (requiredContentPatterns.isEmpty()) {
      return HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE.value(true);
    }

    return HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE.value(
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
  public CodeOfConductGuidelineInfo configure(Path path) throws IOException {
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
  CodeOfConductGuidelineInfo configure(InputStream is) throws IOException {
    JsonNode config = Yaml.mapper().readTree(is);
    requiredContentPatterns(readListFrom(config, "requiredContentPatterns"));
    return this;
  }
}
