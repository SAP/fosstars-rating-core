package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
 * <p>
 * This is a base class for data providers that would like to collect release information from
 * different sources.
 * </p>
 *
 * @param <T> A type of the objects for which the data provider can fetch data.
 */
public abstract class AbstractReleaseInfoLoader<T> implements DataProvider<T> {

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
  public ValueCache<T> cache() {
    return NoValueCache.create();
  }

  /**
   * There is not call back required for this data provider.
   */
  @Override
  public DataProvider<T> set(UserCallback callback) {
    return this;
  }

  /**
   * No cache value is needed that is used by the data provider.
   */
  @Override
  public AbstractReleaseInfoLoader<T> set(ValueCache<T> cache) {
    return this;
  }

  /**
   * No configuration is required for this data provider.
   */
  @Override
  public AbstractReleaseInfoLoader<T> configure(Path config) throws IOException {
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