package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.data.StandardValueCache;
import com.sap.sgs.phosphor.fosstars.data.ValueCache;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * This is a cache of feature values for projects on GitHub.
 * The cache just wraps {@link StandardValueCache}.
 */
public class GitHubProjectValueCache implements ValueCache<GitHubProject> {

  /**
   * An underlying cache.
   */
  private final StandardValueCache cache;

  /**
   * Initializes a new cache.
   */
  public GitHubProjectValueCache() {
    this(new StandardValueCache());
  }

  /**
   * Initializes a new cache with a {@link StandardValueCache}.
   *
   * @param cache The {@link StandardValueCache}.
   */
  private GitHubProjectValueCache(StandardValueCache cache) {
    this.cache = Objects.requireNonNull(cache, "Unfortunately cache can't be null");
  }

  @Override
  public Optional<Value> get(GitHubProject project, Feature feature) {
    return cache.get(project.url().toString(), feature);
  }

  @Override
  public void put(GitHubProject project, Value value) {
    cache.put(project.url().toString(), value);
  }

  @Override
  public void put(GitHubProject project, Value value, Date expiration) {
    cache.put(project.url().toString(), value, expiration);
  }

  /**
   * Stores the cache to a file.
   *
   * @param filename A name of the file.
   * @throws IOException If something went wrong.
   */
  public void store(String filename) throws IOException {
    cache.store(filename);
  }

  /**
   * Loads a cache from a file.
   *
   * @param filename A name of the file.
   * @return The loaded cache.
   * @throws IOException If something went wrong.
   */
  public static GitHubProjectValueCache load(String filename) throws IOException {
    return new GitHubProjectValueCache(StandardValueCache.load(filename));
  }
}
