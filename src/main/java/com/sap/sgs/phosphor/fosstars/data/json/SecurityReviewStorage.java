package com.sap.sgs.phosphor.fosstars.data.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReview;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviews;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The class maintains a list of security reviews which have been done for open-source projects.
 * An open-source project is represented by the URL it its source code repository.
 */
public class SecurityReviewStorage extends AbstractJsonStorage {

  /**
   * Path to a resource which contains the information about security reviews.
   */
  private static final String RESOURCE_PATH =
      "com/sap/sgs/phosphor/fosstars/data/SecurityReview.json";

  /**
   * Maps a project's code repository URL to a set of security reviews
   * which have been done for the project.
   */
  private final Map<String, Set<SecurityReview>> reviews;

  /**
   * Initializes a storage with a number of security reviews.
   *
   * @param reviews The Security reviews (can't be null).
   */
  private SecurityReviewStorage(
      @JsonProperty("reviews") Map<String, Set<SecurityReview>> reviews) {

    Objects.requireNonNull(reviews, "Reviews can't be null");
    this.reviews = reviews;
  }

  /**
   * Looks for security reviews which have been done for a project.
   *
   * @param url The project's code repository URL.
   * @return Security reviews.
   */
  public SecurityReviews get(String url) {
    checkHttps(url);

    Set<SecurityReview> entries = reviews.get(url);
    if (entries == null) {
      entries = Collections.emptySet();
    }

    return new SecurityReviews(entries);
  }

  /*
   * This getter is here to make Jackson happy.
   */
  @JsonGetter("reviews")
  private Map<String, Set<SecurityReview>> reviews() {
    return Collections.unmodifiableMap(reviews);
  }

  /**
   * Loads info about security reviews from the default location.
   *
   * @return An instance of {@link SecurityReviewStorage}.
   * @throws IOException If something went wrong.
   */
  public static SecurityReviewStorage load() throws IOException {
    return load(RESOURCE_PATH);
  }

  /**
   * Loads info about security reviews.
   *
   * @param path A path to a JSON file.
   * @return An instance of {@link SecurityReviewStorage}.
   * @throws IOException If something went wrong.
   */
  public static SecurityReviewStorage load(String path) throws IOException {
    File file = new File(path);
    SecurityReviewStorage storage;
    if (file.exists()) {
      storage = MAPPER.readValue(file, SecurityReviewStorage.class);
    } else {
      storage = loadFromResource(path);
    }
    if (storage == null) {
      throw new IOException(String.format(
          "Could not load security reviews from %s", path));
    }
    return storage;
  }

  /**
   * Tries to load info about security reviews.
   *
   * @param path A path to the resource.
   * @return An instance of {@link SecurityReviewStorage}.
   * @throws IOException If something went wrong.
   */
  private static SecurityReviewStorage loadFromResource(String path) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (is != null) {
      try {
        return MAPPER.readValue(is, SecurityReviewStorage.class);
      } finally {
        is.close();
      }
    }
    throw new IOException(String.format("Resource '%s' not found!", path));
  }
}
