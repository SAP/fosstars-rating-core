package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ALLOWED_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LICENSE_HAS_DISALLOWED_CONTENT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.util.Deserialization.readListFrom;
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
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;

/**
 * This data provider gathers info about project's license. It fills out the following features:
 * <ul>
 *   <li>{@link OssFeatures#HAS_LICENSE}</li>
 *   <li>{@link OssFeatures#ALLOWED_LICENSE}</li>
 *   <li>{@link OssFeatures#LICENSE_HAS_DISALLOWED_CONTENT}</li>
 * </ul>
 */
public class LicenseInfo extends GitHubCachingDataProvider {
  
  static final String SPDX_ID = "spdxId";
  static final String LICENSE_PATH = "licensePath";
  
  /**
   * A list of SPDX IDs of allowed licenses.
   */
  private final List<String> allowedLicenses = new ArrayList<String>();

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
   * @param patterns The patterns.
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

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(HAS_LICENSE, ALLOWED_LICENSE, LICENSE_HAS_DISALLOWED_CONTENT);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Gathering info about project's license ...");
    
    // The GitHub API library doesn't support getting the SPDX entry and the _actual_ content of the license
    // We need both to perform proper checks and therefore are querying the API manually
    Map<String, String> licenseMetadata = licenseMetadata(project);
    
    if (licenseMetadata.isEmpty()) {
      return ValueHashSet.from(
          HAS_LICENSE.value(false),
          ALLOWED_LICENSE.unknown(),
          LICENSE_HAS_DISALLOWED_CONTENT.unknown());
    }
    
    ValueSet values = new ValueHashSet();
    values.update(HAS_LICENSE.value(true));
    values.update(analyzeLicenseContent(retrieveLicenseIn(project, licenseMetadata.get(LICENSE_PATH)).get()));
    values.update(ALLOWED_LICENSE.value(allowedLicenses.contains(licenseMetadata.get(SPDX_ID))));

    return values;
  }

  /**
   * Retrieves the license in a project.
   *
   * @param project The project.
   * @return Content of the license if found.
   * @throws IOException If something went wrong.
   */
  private Optional<String> retrieveLicenseIn(GitHubProject project, String path) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    
    Optional<InputStream> content = repository.read(path);
    if (content.isPresent()) {
      return Optional.of(IOUtils.toString(content.get()));
    }

    return Optional.empty();
  }

  /**
   * Analyze license content.
   *
   * @param content Content of the license.
   * @return A set of values.
   */
  ValueSet analyzeLicenseContent(String content) {
    ValueSet values = ValueHashSet.from(
        ALLOWED_LICENSE.value(false), LICENSE_HAS_DISALLOWED_CONTENT.value(false));

    values.update(LICENSE_HAS_DISALLOWED_CONTENT.value(
        disallowedLicensePatterns.stream()
            .anyMatch(pattern -> pattern.matcher(content).find())));

    return values;
  }
  
  /**
   * Creates an HTTP client.
   *
   * @return A new HTTP client.
   */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }
  
  Map<String, String> licenseMetadata(GitHubProject project) {
    HashMap<String, String> licenseMetadata = new HashMap<String, String>();
    
    try (CloseableHttpClient client = httpClient()) {
      String url = String.format("https://api.github.com/repos/%s/%s/license",
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
    return configure(Files.newInputStream(path));
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
    return this;
  }
}
