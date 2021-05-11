package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ALLOWED_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LICENSE_HAS_DISALLOWED_CONTENT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.util.Deserialization.readListFrom;

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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.kohsuke.github.GHLicense;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

/**
 * This data provider gathers info about project's license. It fills out the following features:
 * <ul>
 *   <li>{@link OssFeatures#HAS_LICENSE}</li>
 *   <li>{@link OssFeatures#ALLOWED_LICENSE}</li>
 *   <li>{@link OssFeatures#LICENSE_HAS_DISALLOWED_CONTENT}</li>
 * </ul>
 */
public class LicenseInfo extends GitHubCachingDataProvider {

  /**
   * Possible sources of information for the data provider.
   */
  enum Source {
    LOCAL_REPOSITORY, GITHUB_API
  }

  /**
   * A list of files that may be a license.
   */
  private static final List<String> DEFAULT_KNOWN_LICENSE_FILES
      = Arrays.asList("LICENSE", "LICENSE.txt");

  /**
   * A list of paths to licenses.
   */
  private final List<String> knownLicenseFiles = new ArrayList<>();

  /**
   * A list of patterns for matching a valid license.
   */
  private final List<Pattern> allowedLicensePatterns = new ArrayList<>();

  /**
   * A list of patterns that are not allowed in licenses.
   */
  private final List<Pattern> disallowedLicensePatterns = new ArrayList<>();

  /**
   * A list of allowed license names that are known on GitHub.
   */
  private final List<String> githubNamesOfAllowedLicenses = new ArrayList<>();

  /**
   * Preferred source of information for the data provider.
   */
  private Source preferredSource = Source.LOCAL_REPOSITORY;

  /**
   * Initializes a data provider. The constructor searches for default configs for the provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If one of the default configs could not be loaded.
   */
  public LicenseInfo(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
    knownLicenseFiles.addAll(DEFAULT_KNOWN_LICENSE_FILES);
    loadDefaultConfigIfAvailable();
  }

  /**
   * Set a list of file that may be a license.
   *
   * @param files The files.
   * @return This data provider.
   */
  public LicenseInfo knownLicenseFiles(String... files) {
    Objects.requireNonNull(files, "Oops! Files can't be null");

    if (files.length == 0) {
      throw new IllegalArgumentException("Oops! Files can't be empty!");
    }

    knownLicenseFiles.clear();
    knownLicenseFiles.addAll(Arrays.asList(files));

    return this;
  }

  /**
   * Returns a list of allowed license headers.
   *
   * @return A list of allowed license headers.
   */
  List<Pattern> allowedLicensePatterns() {
    return new ArrayList<>(allowedLicensePatterns);
  }

  /**
   * Set a list of patterns for matching an allowed licence.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public LicenseInfo allowedLicensePatterns(String... patterns) {
    return allowedLicensePatterns(Arrays.asList(patterns));
  }

  /**
   * Set a list of patterns for matching an allowed licence.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public LicenseInfo allowedLicensePatterns(List<String> patterns) {
    Objects.requireNonNull(patterns, "Oops! Patterns is null");
    allowedLicensePatterns.clear();
    allowedLicensePatterns.addAll(
        patterns.stream()
            .map(pattern -> Pattern.compile(pattern, Pattern.DOTALL)).collect(Collectors.toList()));
    return this;
  }

  /**
   * Returns a list of disallowed license headers.
   *
   * @return A list of disallowed license headers.
   */
  List<Pattern> disallowedLicensePatterns() {
    return new ArrayList<>(disallowedLicensePatterns);
  }

  /**
   * Set a list of patterns for detecting not-allowed content in a license.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public LicenseInfo disallowedLicensePatterns(String... patterns) {
    return disallowedLicensePatterns(Arrays.asList(patterns));
  }

  /**
   * Set a list of patterns for detecting not-allowed content in a license.
   *
   * @param patterns The patterns.
   * @return This data provider.
   */
  public LicenseInfo disallowedLicensePatterns(List<String> patterns) {
    Objects.requireNonNull(patterns, "Oops! Patterns can't be null!");
    disallowedLicensePatterns.clear();
    disallowedLicensePatterns.addAll(
        patterns.stream()
            .map(pattern -> Pattern.compile(pattern, Pattern.DOTALL)).collect(Collectors.toList()));
    return this;
  }

  /**
   * Returns a list of allowed license names.
   *
   * @return A list of allowed license names.
   */
  List<String> githubNamesOfAllowedLicenses() {
    return new ArrayList<>(githubNamesOfAllowedLicenses);
  }

  /**
   * Set a list of allowed license names.
   *
   * @param names The license names.
   * @return This data provider.
   */
  public LicenseInfo githubNamesOfAllowedLicenses(String... names) {
    return githubNamesOfAllowedLicenses(Arrays.asList(names));
  }

  /**
   * Set a list of allowed license names.
   *
   * @param names The license names.
   * @return This data provider.
   */
  public LicenseInfo githubNamesOfAllowedLicenses(List<String> names) {
    Objects.requireNonNull(names, "Oops! License names can't be null!");
    githubNamesOfAllowedLicenses.clear();
    githubNamesOfAllowedLicenses.addAll(names);
    return this;
  }

  /**
   * Set a preferred source of information for the provider.
   *
   * @param source The preferred source of information.
   * @return This data provider.
   */
  LicenseInfo prefer(Source source) {
    Objects.requireNonNull(source, "Oh no! Source can't be null!");
    preferredSource = source;
    return this;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(HAS_LICENSE, ALLOWED_LICENSE, LICENSE_HAS_DISALLOWED_CONTENT);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Gathering info about project's license ...");

    switch (preferredSource) {
      case GITHUB_API:
        return fetchValuesFromGitHubFor(project);
      case LOCAL_REPOSITORY:
        return fetchValuesFromLocalRepositoryFor(project);
      default:
        throw new IllegalStateException(
            String.format("Oops! Unexpected preference: %s", preferredSource));
    }
  }

  /**
   * Fill out the supported features using a locally cloned repository of a project.
   *
   * @param project The project.
   * @return A set of values.
   * @throws IOException If something went wrong.
   */
  ValueSet fetchValuesFromLocalRepositoryFor(GitHubProject project) throws IOException {
    Optional<String> license = lookForLicenseIn(project);
    if (!license.isPresent()) {
      return ValueHashSet.from(
          HAS_LICENSE.value(false),
          ALLOWED_LICENSE.unknown(),
          LICENSE_HAS_DISALLOWED_CONTENT.unknown());
    }

    ValueSet values = new ValueHashSet();
    values.update(
        HAS_LICENSE.value(true),
        ALLOWED_LICENSE.value(false),
        LICENSE_HAS_DISALLOWED_CONTENT.value(false));

    values.update(ALLOWED_LICENSE.value(matchAllowedLicensePattern(license.get())));
    values.update(LICENSE_HAS_DISALLOWED_CONTENT.value(hasDisallowedContent(license.get())));

    return values;
  }

  /**
   * Fill out the supported features for a project using GitHub API.
   *
   * @param project The project.
   * @return A set of values.
   * @throws IOException If something went wrong.
   * @see <a href="https://docs.github.com/en/rest/reference/licenses">GitHub Licenses API</a>
   */
  private ValueSet fetchValuesFromGitHubFor(GitHubProject project) throws IOException {
    GHLicense license = fetcher.repositoryFor(project).getLicense();
    if (license == null) {
      return ValueHashSet.from(
          HAS_LICENSE.value(false),
          ALLOWED_LICENSE.unknown(),
          LICENSE_HAS_DISALLOWED_CONTENT.unknown());
    }

    ValueSet values = new ValueHashSet();
    values.update(HAS_LICENSE.value(true));

    values.update(ALLOWED_LICENSE.value(
        githubNamesOfAllowedLicenses.contains(license.getName())
            || matchAllowedLicensePattern(license.getBody())));

    values.update(LICENSE_HAS_DISALLOWED_CONTENT.value(hasDisallowedContent(license.getBody())));

    return values;
  }

  /**
   * Check if a license matches with any allowed pattern.
   *
   * @param content The license content.
   * @return True if the license matches with any allowed pattern, false otherwise.
   */
  private boolean matchAllowedLicensePattern(String content) {
    if (allowedLicensePatterns.isEmpty()) {
      return false;
    }

    return allowedLicensePatterns.stream().allMatch(pattern -> pattern.matcher(content).find());
  }

  /**
   * Check if a license has disallowed content.
   *
   * @param content The license content.
   * @return True if the license has disallowed content, false otherwise.
   */
  private boolean hasDisallowedContent(String content) {
    return disallowedLicensePatterns.stream().anyMatch(pattern -> pattern.matcher(content).find());
  }

  /**
   * Looks for license in a project.
   *
   * @param project The project.
   * @return Content of the license if found.
   * @throws IOException If something went wrong.
   */
  private Optional<String> lookForLicenseIn(GitHubProject project) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    for (String path : knownLicenseFiles) {
      Optional<String> content = repository.readTextFrom(path);
      if (content.isPresent()) {
        return content;
      }
    }

    return Optional.empty();
  }

  /**
   * Reads a configuration from a YAML file.
   *
   * @param path A path to the YAML file.
   * @return This data provider.
   * @throws IOException If something went wrong.
   */
  @Override
  public LicenseInfo configure(Path path) throws IOException {
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
  LicenseInfo configure(InputStream is) throws IOException {
    JsonNode config = Yaml.mapper().readTree(is);
    allowedLicensePatterns(readListFrom(config, "allowedLicensePatterns"));
    disallowedLicensePatterns(readListFrom(config, "disallowedLicensePatterns"));

    if (config.has("preferredSource")) {
      JsonNode node = config.get("preferredSource");
      if (!node.isTextual()) {
        throw new IOException("Oops! preferredSource is not a string!");
      }

      prefer(Source.valueOf(node.asText().toUpperCase()));
    }

    return this;
  }

  /**
   * This is for testing and demo purposes.
   *
   * @param args Command-line options.
   * @throws Exception If something went wrong.
   */
  public static void main(String... args) throws Exception {
    String token = args.length > 0 ? args[0] : "";
    String url = args.length > 1 ? args[1] : "https://github.com/SAP/fosstars-rating-core";
    GitHubProject project = GitHubProject.parse(url);
    GitHub github = new GitHubBuilder().withOAuthToken(token).build();
    LicenseInfo provider = new LicenseInfo(new GitHubDataFetcher(github, token));
    provider.prefer(Source.GITHUB_API);
    provider.githubNamesOfAllowedLicenses("Apache License 2.0");
    ValueSet values = provider.fetchValuesFor(project);
    for (Value<?> value : values) {
      System.out.printf("%s: %s%n", value.feature().name(), value.get());
    }
  }
}
