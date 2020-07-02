package com.sap.sgs.phosphor.fosstars.tool.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
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
 * This is a cache of {@link GitHubProject}s.
 */
class GitHubProjectCache {

  /**
   * For serialization and deserialization.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * The default lifetime of a cache entry in days.
   */
  private static final int DEFAULT_LIFETIME = 7;

  /**
   * Maps a URL of a project to a {@link GitHubProject}.
   */
  final Map<String, GitHubProject> cache;

  /**
   * A lifetime of a cache entry in days.
   */
  private long lifetime = DEFAULT_LIFETIME;

  /**
   * Creates an empty cache.
   */
  static GitHubProjectCache empty() {
    return new GitHubProjectCache(new HashMap<>());
  }

  /**
   * Initializes a new cache. The constructor is used for deserialization.
   *
   * @param cache A map with cache entries.
   */
  @JsonCreator
  private GitHubProjectCache(@JsonProperty("cache") Map<String, GitHubProject> cache) {
    this.cache = cache;
  }

  /**
   * Return a map with cache entries. The method is used for serialization.
   *
   * @return The map.
   */
  @JsonGetter("cache")
  private Map<String, GitHubProject> cache() {
    return cache;
  }

  /**
   * Set a lifetime for cache entries.
   *
   * @param days The lifetime in days.
   * @return The same {@link GitHubProject}.
   */
  GitHubProjectCache lifetime(long days) {
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
  int size() {
    return cache.size();
  }

  /**
   * Add a new project to the cache.
   *
   * @param project The project.
   * @return The same {@link GitHubProject}.
   */
  GitHubProjectCache add(GitHubProject project) {
    cache.put(project.url().toString(), project);
    return this;
  }

  /**
   * Returns a rating value for a project if it's available in the cache.
   *
   * @param project The project.
   * @return An {@link Optional} with a rating value for the project.
   */
  Optional<RatingValue> cachedRatingValueFor(GitHubProject project) {
    GitHubProject cached = cache.get(project.url().toString());
    if (cached == null) {
      return Optional.empty();
    }
    if (!cached.ratingValue().isPresent()) {
      return Optional.empty();
    }
    RatingValue ratingValue = cached.ratingValue().get();
    long age = Duration.between(cached.ratingValueDate().toInstant(), Instant.now()).toDays();
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
  static GitHubProjectCache load(String filename) throws IOException {
    return load(Paths.get(filename));
  }

  /**
   * Load a cache from a file.
   *
   * @param filename A path to the file.
   * @return A loaded cache.
   * @throws IOException If something went wrong.
   */
  static GitHubProjectCache load(Path filename) throws IOException {
    return load(Files.newInputStream(filename));
  }

  /**
   * Load a cache from an input stream.
   *
   * @param is The input stream.
   * @return A loaded cache.
   * @throws IOException If something went wrong.
   */
  static GitHubProjectCache load(InputStream is) throws IOException {
    return MAPPER.readValue(is, GitHubProjectCache.class);
  }

  /**
   * Stores a cache of projects to a file.
   *
   * @param filename A path to the file.
   * @throws IOException If something went wrong.
   */
  void store(String filename) throws IOException {
    store(Paths.get(filename));
  }

  /**
   * Store the cache to a file.
   *
   * @param filename The file.
   * @throws IOException If something went wrong.
   */
  void store(Path filename) throws IOException {
    Files.write(
        filename,
        MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(this));
  }
}
