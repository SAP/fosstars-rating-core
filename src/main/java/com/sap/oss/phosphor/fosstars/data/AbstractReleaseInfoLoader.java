package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a base class for data providers that would like to collect release information from
 * different sources.
 */
public abstract class AbstractReleaseInfoLoader implements DataProvider {

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
  protected JsonNode fetch(String url) throws IOException {
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
    return HttpClients.createDefault();
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
}