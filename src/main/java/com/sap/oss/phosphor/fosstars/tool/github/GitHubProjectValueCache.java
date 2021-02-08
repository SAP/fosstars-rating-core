package com.sap.oss.phosphor.fosstars.tool.github;

import com.sap.oss.phosphor.fosstars.data.StandardValueCache;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
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

  /**
   * Looks for a feature value for a specified project.
   *
   * @param project The project.
   * @param feature The feature.
   * @param <T> Type of data.
   * @return An {@link Optional} with the feature value if it's in the cache.
   */
  public <T> Optional<Value<T>> get(GitHubProject project, Feature<T> feature) {
    return cache.get(project.scm().toString(), feature);
  }

  @Override
  public Optional<ValueSet> get(GitHubProject project) {
    return cache.get(project.scm().toString());
  }

  /**
   * Store a value for a GitHub project.
   *
   * @param project The project.
   * @param value The value.
   * @param <T> A type of the value.
   */
  public <T> void put(GitHubProject project, Value<T> value) {
    cache.put(project.scm().toString(), value);
  }

  /**
   * Store a value with an expiration data for a GitHub project.
   *
   * @param project The project.
   * @param value The value.
   * @param expiration The expiration date.
   * @param <T> A type of the value.
   */
  public <T> void put(GitHubProject project, Value<T> value, Date expiration) {
    cache.put(project.scm().toString(), value, expiration);
  }

  @Override
  public void put(GitHubProject project, ValueSet set) {
    cache.put(project.scm().toString(), set);
  }

  @Override
  public void put(GitHubProject project, ValueSet set, Date expiration) {
    cache.put(project.scm().toString(), set, expiration);
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

  @Override
  public int size() {
    return cache.size();
  }
}
