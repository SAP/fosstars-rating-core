package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a base class for data providers that would like to collect release information from
 * different sources.
 */
public abstract class AbstractReleaseInfoLoader implements DataProvider {

  /**
   * Standard character type encoding.
   */
  private static final String UTF_8 = StandardCharsets.UTF_8.name();

  /**
   * Pattern to match version and release date.
   */
  private static final Pattern VERSIONS_PATTERN =
      Pattern.compile(
          "<a href=\"[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*/\" title=\"[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*/\">([A-Za-z0-9]+(\\.[A-Za-z0-9]+)*)/</a>\\s+([1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])[\\ ]([01]?[0-9]|2[0-3]):[0-5][0-9])+\\s+-\\s*",
          Pattern.CASE_INSENSITIVE);

  /**
   * A logger.
   */
  protected final Logger logger = LogManager.getLogger(getClass());

  /**
   * The method always returns false, so that all child classes can't be interactive.
   */
  @Override
  public final boolean interactive() {
    return false;
  }

  /**
   * Returns the supported feature loaded by this data provider.
   */
  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(RELEASED_ARTIFACT_VERSIONS, ARTIFACT_VERSION);
  }

  /**
   * This is a dummy cache which stores nothing.
   */
  @Override
  public ValueCache<Subject> cache() {
    return NoValueCache.create();
  }

  /**
   * There is not call back required for this data provider.
   */
  @Override
  public AbstractReleaseInfoLoader set(UserCallback callback) {
    return this;
  }

  /**
   * No cache value is needed that is used by the data provider.
   */
  @Override
  public AbstractReleaseInfoLoader set(ValueCache<Subject> cache) {
    return this;
  }

  /**
   * No configuration is required for this data provider.
   */
  @Override
  public AbstractReleaseInfoLoader configure(Path config) throws IOException {
    return this;
  }

  /**
   * Fetches info from the URL.
   *
   * @param url from which the information needs to be collected.
   * @return The info from the URL.
   * @throws IOException If something went wrong.
   */
  protected JsonNode fetchJson(String url) throws IOException {
    try (CloseableHttpClient client = httpClient()) {
      HttpGet httpGetRequest = new HttpGet(url);
      httpGetRequest.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest)) {
        return Json.mapper().readTree(httpResponse.getEntity().getContent());
      }
    }
  }

  /**
   * Update the {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#ARTIFACT_VERSION}
   * value based on given parameters.
   * If version is not in the set of artifact versions
   * or the optional version is not the ARTIFACT_VERSION is set to unknown.
   *
   * @param version The artifact version.
   * @param artifactVersions All found artifact versions.
   * @param values The set of values to be updated.
   */
  protected static void updateArtifactVersion(Optional<String> version,
      Set<ArtifactVersion> artifactVersions, ValueSet values) {

    Value<ArtifactVersion> match = version
        .flatMap(ver -> artifactVersions.stream()
            .filter(v -> v.version().equals(ver))
            .findFirst())
        .map(ARTIFACT_VERSION::value)
        .orElseGet(ARTIFACT_VERSION::unknown);
    values.update(match);
  }

  /**
   * Creates an HTTP client.
   *
   * @return An HTTP client.
   */
  public CloseableHttpClient httpClient() {
    // Set the Connection timeout to 120 seconds
    int timeout = 120;
    RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
          .setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
    return HttpClientBuilder.create()
          .setDefaultRequestConfig(config).build();
  }

  /**
   * Converts Epoch time in milliseconds to Local Date.
   *
   * @param epoch The DateTime in milliseconds.
   * @return Local Date.
   */
  protected static LocalDateTime convertEpochToLocalDate(Long epoch) {
    return Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  /**
   * Converts string Date to Local Date.
   *
   * @param date in String.
   * @return Local Date.
   */
  protected static LocalDateTime convertToLocalDate(String date) {
    return ZonedDateTime.parse(date).toLocalDateTime();
  }

  /**
   * Gathers release versions about Maven artifact.
   *
   * @param mavenArtifact A {@link MavenArtifact}.
   * @return A set  of {@link ArtifactVersion} containing the Maven artifact release versions.
   * @throws IOException If something goes wrong.
   */
  protected Set<ArtifactVersion> versionsOf(MavenArtifact mavenArtifact)
      throws IOException {
    String url = String.format(
        "https://repo1.maven.org/maven2/%s/%s/",
        mavenArtifact.group().replaceAll("\\.", "/"), mavenArtifact.artifact());
    Set<ArtifactVersion> artifactVersions = fetchVersionsOf(url, new HashSet<>());
    return artifactVersions;
  }

  /**
   * Fetches release versions from the URL.
   *
   * @param url from which the information needs to be collected.
   * @return A set  of {@link ArtifactVersion} containing the Maven artifact release versions.
   * @throws IOException If something went wrong.
   */
  private Set<ArtifactVersion> fetchVersionsOf(String url, Set<ArtifactVersion> artifactVersions)
      throws IOException {
    try (CloseableHttpClient client = httpClient()) {
      HttpGet httpGetRequest = new HttpGet(url);
      httpGetRequest.addHeader(HttpHeaders.ACCEPT, ContentType.TEXT_HTML.getMimeType());
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest);
          BufferedReader reader = new BufferedReader(
              new InputStreamReader(httpResponse.getEntity().getContent(), UTF_8))) {
        String line = null;
        while ((line = reader.readLine()) != null) {
          Matcher matcher = VERSIONS_PATTERN.matcher(line);
          if (matcher.matches()) {
            try {
              artifactVersions.add(new ArtifactVersion(matcher.group(3),
                  LocalDateTime.parse(matcher.group(5),
                      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
            } catch (NumberFormatException ignored) {
              // no special handling required
            }
          }
        }
        return artifactVersions;
      }
    }
  }
}