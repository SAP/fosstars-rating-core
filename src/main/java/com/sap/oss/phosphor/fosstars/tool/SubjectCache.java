package com.sap.oss.phosphor.fosstars.tool;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This is a cache of {@link Subject}s.
 */
public class SubjectCache {

  /**
   * The default lifetime of a cache entry in days.
   */
  private static final int DEFAULT_LIFETIME = 7;

  /**
   * Maps a PURL to a {@link Subject}.
   */
  final Map<String, Subject> cache;

  /**
   * A lifetime of a cache entry in days.
   */
  private long lifetime = DEFAULT_LIFETIME;

  /**
   * Creates an empty cache.
   *
   * @return An empty cache.
   */
  public static SubjectCache empty() {
    return new SubjectCache(new HashMap<>());
  }

  /**
   * Initializes a new cache. The constructor is used for deserialization.
   *
   * @param cache A map with cache entries.
   */
  @JsonCreator
  private SubjectCache(@JsonProperty("cache") Map<String, Subject> cache) {
    this.cache = cache;
  }

  /**
   * Return a map with cache entries. The method is used for serialization.
   *
   * @return The map.
   */
  @JsonGetter("cache")
  private Map<String, Subject> cache() {
    return cache;
  }

  /**
   * Set a lifetime for cache entries.
   *
   * @param days The lifetime in days.
   * @return The same cache.
   */
  public SubjectCache lifetime(long days) {
    if (days < 1) {
      throw new IllegalArgumentException("Hey! You gave me a wrong life time for cache entries!");
    }
    lifetime = days;
    return this;
  }

  /**
   * Returns a size of the cache.
   *
   * @return A size of the cache.
   */
  public int size() {
    return cache.size();
  }

  /**
   * Add a new subject to the cache.
   *
   * @param subject The subject.
   * @return The same cache.
   */
  public SubjectCache add(Subject subject) {
    cache.put(subject.purl(), subject);
    return this;
  }

  /**
   * Returns a rating value for a subject if it's available in the cache.
   *
   * @param subject The subject.
   * @return An {@link Optional} with a rating value for the subject.
   */
  public Optional<RatingValue> cachedRatingValueFor(Subject subject) {
    Subject cached = cache.get(subject.purl());
    if (cached == null) {
      return Optional.empty();
    }
    if (!cached.ratingValue().isPresent() || !cached.ratingValueDate().isPresent()) {
      return Optional.empty();
    }
    RatingValue ratingValue = cached.ratingValue().get();
    long age = Duration.between(cached.ratingValueDate().get().toInstant(), Instant.now()).toDays();
    if (age >= lifetime) {
      return Optional.empty();
    }
    return Optional.of(ratingValue);
  }

  /**
   * Load a cache from a file.
   *
   * @param filename A path to the file.
   * @return A loaded cache.
   * @throws IOException If something went wrong.
   */
  public static SubjectCache load(String filename) throws IOException {
    return load(Paths.get(filename));
  }

  /**
   * Load a cache from a file.
   *
   * @param filename A path to the file.
   * @return A loaded cache.
   * @throws IOException If something went wrong.
   */
  public static SubjectCache load(Path filename) throws IOException {
    try (InputStream is = Files.newInputStream(filename)) {
      return load(is);
    }
  }

  /**
   * Load a cache from an input stream.
   *
   * @param is The input stream.
   * @return A loaded cache.
   * @throws IOException If something went wrong.
   */
  public static SubjectCache load(InputStream is) throws IOException {
    return Json.read(is, SubjectCache.class);
  }

  /**
   * Stores a cache of subjects to a file.
   *
   * @param filename A path to the file.
   * @throws IOException If something went wrong.
   */
  public void store(String filename) throws IOException {
    store(Paths.get(filename));
  }

  /**
   * Store the cache to a file.
   *
   * @param filename The file.
   * @throws IOException If something went wrong.
   */
  public void store(Path filename) throws IOException {
    Files.write(filename, Json.toBytes(this));
  }
}
