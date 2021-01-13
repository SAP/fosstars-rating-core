package com.sap.oss.phosphor.fosstars.data.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.io.FileUtils;

/**
 * The class holds info about a local repository.
 */
public class LocalRepositoryInfo {

  /**
   * A date when the repository was updated.
   */
  private Date updated;

  /**
   * A path to the repository.
   */
  private final Path path;

  /**
   * A URL to the repository.
   */
  private final URL url;

  /**
   * Holds a size of repository (may be outdated).
   */
  @JsonIgnore
  private BigInteger cachedRepositorySize;

  /**
   * Initializes a repository.
   *
   * @param updated A date when the repository was updated.
   * @param path A path to the repository.
   * @param url A URL to the repository.
   */
  @JsonCreator
  public LocalRepositoryInfo(
      @JsonProperty("path") Path path,
      @JsonProperty("updated") Date updated,
      @JsonProperty("url") URL url) {

    Objects.requireNonNull(path, "Hey! Path can't be null!");
    Objects.requireNonNull(updated, "Hey! Date can't be null!");
    Objects.requireNonNull(url, "Hey! URL can't be null!");

    this.path = path;
    this.updated = updated;
    this.url = url;
  }

  /**
   * Get a date when the repository was updated.
   *
   * @return A date when the repository was updated.
   */
  @JsonGetter("updated")
  public Date updated() {
    return updated;
  }

  /**
   * Sets a date when the repository was updated.
   *
   * @param date The date to be set.
   */
  public void updated(Date date) {
    Objects.requireNonNull(updated, "Hey! Date can't be null!");
    updated = date;
  }

  /**
   * Get a path to the repository.
   *
   * @return A path to the repository.
   */
  @JsonGetter("path")
  public Path path() {
    return path;
  }

  /**
   * Get a URL to the repository.
   *
   * @return A URL to the repository.
   */
  @JsonGetter("url")
  public URL url() {
    return url;
  }

  /**
   * Get a cached size of the repository if it's available. If not, it calculates the size.
   *
   * @return A size of the repository.
   */
  @JsonIgnore
  public BigInteger cachedRepositorySize() {
    if (cachedRepositorySize != null) {
      return cachedRepositorySize;
    }
    return repositorySize();
  }

  /**
   * Get a size of the repository.
   *
   * @return A size of the repository.
   */
  @JsonIgnore
  public BigInteger repositorySize() {
    return cachedRepositorySize = FileUtils.sizeOfAsBigInteger(path.toFile());
  }
}
