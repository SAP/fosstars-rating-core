package com.sap.oss.phosphor.fosstars.data.github;

import com.sap.oss.phosphor.fosstars.data.Cache;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.map.LRUMap;

/**
 * This is a cache for data of GitHub projects.
 */
public class GitHubDataCache<T> implements Cache<GitHubProject, T> {

  /**
   * This flag doesn't allow exceeding the maximum cache size.
   */
  private static final boolean SCAN_UNTIL_REMOVABLE = true;
  
  /**
   * Maximum size of the cache.
   */
  private static final int CACHE_CAPACITY = 5;
  
  /**
   * This constant means that the value has no expiration date.
   */
  private static final Date NO_EXPIRATION = null;

  /**
   * A map of cache entries.
   */
  private final Map<GitHubProject, GitHubData<T>> entries;

  /**
   * The default constructor.
   */
  public GitHubDataCache() {
    this.entries = Collections.synchronizedMap(new LRUMap<>(CACHE_CAPACITY, SCAN_UNTIL_REMOVABLE));
  }

  @Override
  public Optional<T> get(GitHubProject project) {
    GitHubData<T> value = entries.get(project);

    if (value == null || value.expired()) {
      return Optional.empty();
    }

    return Optional.ofNullable(value.get());
  }

  @Override
  public void put(GitHubProject project, T value) {
    put(project, value, NO_EXPIRATION);
  }

  @Override
  public void put(GitHubProject project, T value, Date expiration) {
    entries.put(project, new GitHubData<>(value, expiration));
  }

  /**
   * Get a size of the cache.
   *
   * @return A size of the cache.
   */
  public int size() {
    return entries.size();
  }

  /**
   * Get the maximum size of the cache.
   *
   * @return The maximum size of the cache.
   */
  public int maxSize() {
    return CACHE_CAPACITY;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof GitHubDataCache == false) {
      return false;
    }
    GitHubDataCache<T> cache = (GitHubDataCache<T>) o;
    return Objects.equals(entries, cache.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(entries);
  }

  /**
   * Clear the cache.
   */
  public void clear() {
    entries.clear();
  }

  /**
   * This is a GitHub data wrapper class. The data to be stored in the cache would be assigned with
   * an expiration flag. Hence, this wrapper would be used.
   */
  private static class GitHubData<T> {

    /**
     * Expiration date, null by default.
     */
    private final Date expiration;

    /**
     * The GitHub data to be cached.
     */
    private final T data;

    /**
     * Constructor for GitHubData.
     * 
     * @param data from GitHub project.
     * @param expiration date decided from {@link GitHubDataCache}.
     */
    private GitHubData(T data, Date expiration) {
      this.data = data;
      this.expiration = expiration;
    }

    /**
     * Checks if the cached value is expired.
     * 
     * @return true if expired. Otherwise, false.
     */
    private boolean expired() {
      Date now = new Date();
      return expiration != null && now.after(expiration);
    }

    /**
     * Get the cached data.
     *
     * @return The cached GitHub data.
     */
    private T get() {
      return data;
    }
  }
}
