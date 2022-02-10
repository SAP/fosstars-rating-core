package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ALLOWED_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LICENSE_HAS_DISALLOWED_CONTENT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.util.Deserialization.readListFrom;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

/**
 * This data provider gathers info about project's license. It fills out the following features:
 * <ul>
 * <li>{@link OssFeatures#HAS_LICENSE}</li>
 * <li>{@link OssFeatures#ALLOWED_LICENSE}</li>
 * <li>{@link OssFeatures#LICENSE_HAS_DISALLOWED_CONTENT}</li>
 * </ul>
 */
public class LicenseInfo extends GitHubCachingDataProvider {

  /**
   * A key for SPDX identifier in metadata.
   */
  static final String SPDX_ID = "spdxId";
  
  /**
   * A key to a license path in metadata.
   */
  static final String LICENSE_PATH = "licensePath";

  /**
   * A list of SPDX IDs of allowed licenses.
   */
  private final List<String> allowedLicenses = new ArrayList<>();

  /**
   * A list of repositories that are already known to be compliant.
   */
  private final List<String> repositoryExceptionUrls = new ArrayList<>();

  /**
   * A list of patterns that are not allowed in licenses.
   */
  private final List<Pattern> disallowedLicensePatterns = new ArrayList<>();

  /**
   * Initializes a data provider. The constructor searches for default configs for the provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If one of the default configs could not be loaded.
   */
  public LicenseInfo(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
    loadDefaultConfigIfAvailable();
  }

  /**
   * Returns a list of SPDX IDs of allowed licenses.
   *
   * @return A list of SPDX IDs of allowed licenses.
   */
  List<String> allowedLicenses() {
    return new ArrayList<>(allowedLicenses);
  }

  /**
   * Set a list of SPDX IDs for allowed licenses.
   *
   * @param spdxIds The SPDX IDs.
   * @return This data provider.
   */
  public LicenseInfo allowedLicenses(String... spdxIds) {
    return allowedLicenses(Arrays.asList(spdxIds));
  }

  /**
   * Set a list of SPDX IDs for allowed licences.
   *
   * @param spdxIds The SPDX IDs.
   * @return This data provider.
   */
  public LicenseInfo allowedLicenses(List<String> spdxIds) {
    Objects.requireNonNull(spdxIds, "Oops! License list is null");
    allowedLicenses.clear();
    allowedLicenses.addAll(spdxIds);
    return this;
  }

  /**
   * Returns a list of repositories that are known to be compliant.
   *
   * @return A list of repository URLs.
   */
  List<String> repositoryExceptions() {
    return new ArrayList<>(repositoryExceptionUrls);
  }

  /**
   * Set a list of repositories that are known to be compliant.
   *
   * @param repositoryExceptions The repository URLs.
   * @return This data provider.
   */
  public LicenseInfo repositoryExceptions(String... repositoryExceptions) {
    return repositoryExceptions(Arrays.asList(repositoryExceptions));
  }

  /**
   * Set a list of repositories. that are known to be compliant.
   *
   * @param repositoryExceptions The repository URLs.
   * @return This data provider.
   */
  public LicenseInfo repositoryExceptions(List<String> repositoryExceptions) {
    Objects.requireNonNull(repositoryExceptions, "Oops! Repository URL list is null");
    repositoryExceptionUrls.clear();
    repositoryExceptionUrls.addAll(repositoryExceptions);
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
    disallowedLicensePatterns.addAll(patterns.stream()
        .map(pattern -> Pattern.compile(pattern, Pattern.DOTALL)).collect(toList()));
    return this;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(HAS_LICENSE, ALLOWED_LICENSE, LICENSE_HAS_DISALLOWED_CONTENT);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Gathering info about project's license ...");

    // Some repositories use normally disallowed licenses, but are well-known exceptions.
    // Those ones will reported as OK by this data provider.
    if (this.repositoryExceptionUrls.contains(project.toString())) {
      return ValueHashSet.from(
          HAS_LICENSE.value(true),
          ALLOWED_LICENSE.value(true),
          LICENSE_HAS_DISALLOWED_CONTENT.value(false));
    }

    // The GitHub API library doesn't support getting the SPDX entry and the _actual_ content of the
    // license. We need both to perform proper checks and therefore are querying the API manually.
    Map<String, String> metadata = licenseMetadata(project);
    Optional<String> content = retrieveLicenseIn(project, metadata.get(LICENSE_PATH));

    if (metadata.isEmpty() || !content.isPresent()) {
      return ValueHashSet.from(
          HAS_LICENSE.value(false).explain("No license found in the project"),
          ALLOWED_LICENSE.unknown(),
          LICENSE_HAS_DISALLOWED_CONTENT.unknown());
    }

    ValueSet values = new ValueHashSet();
    values.update(HAS_LICENSE.value(true));
    values.update(analyzeLicense(content.get(), metadata.get(SPDX_ID)));

    return values;
  }

  /**
   * Retrieves the license in a project.
   *
   * @param project The project.
   * @param path A path to the license.
   * @return Content of the license if found.
   * @throws IOException If something went wrong.
   */
  private Optional<String> retrieveLicenseIn(GitHubProject project, @Nullable String path)
      throws IOException {

    if (path == null) {
      logger.warn("Oops! License path is null!");
      return Optional.empty();
    }

    return GitHubDataFetcher.localRepositoryFor(project).readTextFrom(path);
  }

  /**
   * Analyze a license.
   *
   * @param content Content of the license.
   * @param spdxId SPDX ID of the license.
   * @return A set of values.
   */
  ValueSet analyzeLicense(String content, String spdxId) {
    Value<Boolean> allowedLicense = ALLOWED_LICENSE.value(allowedLicenses.contains(spdxId))
        .explainIf(false, "%s is not allowed", spdxId);

    List<Pattern> violated = disallowedLicensePatterns.stream()
        .filter(pattern -> pattern.matcher(content).find())
        .collect(toList());
    Value<Boolean> hasDisallowedText = LICENSE_HAS_DISALLOWED_CONTENT.value(!violated.isEmpty())
        .explainIf(true, "The license contains disallowed text that match %s",
            violated.stream()
                .map(pattern -> format("'%s'", pattern))
                .collect(joining(", ")));

    return ValueHashSet.from(allowedLicense, hasDisallowedText);
  }

  /**
   * Creates an HTTP client.
   *
   * @return A new HTTP client.
   */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

  /**
   * Retrieves metadata of the license that is used in a project on GitHub.
   *
   * @param project The project.
   * @return Metadata of the license.
   */
  Map<String, String> licenseMetadata(GitHubProject project) {
    HashMap<String, String> licenseMetadata = new HashMap<>();

    try (CloseableHttpClient client = httpClient()) {
      String url = format("https://api.github.com/repos/%s/%s/license",
          project.organization().name(), project.name());
      HttpGet request = new HttpGet(url);
      request.addHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json");
      request.addHeader(HttpHeaders.AUTHORIZATION, "token " + fetcher.token());
      try (CloseableHttpResponse response = client.execute(request)) {
        if (response.getStatusLine().getStatusCode() != 200) {
          logger.warn("Oops! Could not fetch license metadata from GitHub API ({})",
              response.getStatusLine().getStatusCode());
          return licenseMetadata;
        }

        JsonNode root = Json.mapper().readTree(response.getEntity().getContent());
        licenseMetadata.put(LICENSE_PATH, root.at("/path").asText());
        licenseMetadata.put(SPDX_ID, root.at("/license/spdx_id").asText());
      }
    } catch (IOException e) {
      logger.warn("Oops! Could not fetch license metadata from GitHub API", e);
    }

    return licenseMetadata;
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
    allowedLicenses(readListFrom(config, "allowedLicenses"));
    disallowedLicensePatterns(readListFrom(config, "disallowedLicensePatterns"));
    repositoryExceptions(readListFrom(config, "repositoryExceptions"));
    return this;
  }

  /**
   * This is for testing and demo purposes.
   *
   * @param args Command-line options (option 1: API token, option 2: project URL).
   * @throws Exception If something went wrong.
   */
  public static void main(String... args) throws Exception {
    String token = args.length > 0 ? args[0] : "";
    String url = args.length > 1 ? args[1] : "https://github.com/SAP/fosstars-rating-core";
    GitHubProject project = GitHubProject.parse(url);
    GitHub github = new GitHubBuilder().withOAuthToken(token).build();
    LicenseInfo provider = new LicenseInfo(new GitHubDataFetcher(github, token));
    provider.configure(IOUtils.toInputStream(
        "---\n"
            + "allowedLicenses:\n"
            + "  - Apache-2.0\n"
            + "  - CC-BY-4.0\n"
            + "  - MIT\n"
            + "  - EPL-2.0\n"
            + "disallowedLicensePatterns:\n"
            + "  - API\n"
            + "repositoryExceptions:\n"
            + "  - https://github.com/SAP/SapMachine\n"
            + "  - https://github.com/SAP/jmc\n",
        "UTF-8"));
    ValueSet values = provider.fetchValuesFor(project);
    for (Value<?> value : values) {
      System.out.printf("%s: %s%n", value.feature().name(), value.get());
    }
  }

}
