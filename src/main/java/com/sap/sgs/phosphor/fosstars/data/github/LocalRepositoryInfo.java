package com.sap.sgs.phosphor.fosstars.data.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.Objects;

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
   * Returns a date when the repository was updated.
   */
  @JsonGetter("updated")
  public Date updated() {
    return updated;
  }

  /**
   * Sets a date when the repository was updated.
   */
  public void updated(Date date) {
    Objects.requireNonNull(updated, "Hey! Date can't be null!");
    updated = date;
  }

  /**
   * Returns a path to the repository.
   */
  @JsonGetter("path")
  public Path path() {
    return path;
  }

  /**
   * Returns a URL to the repository.
   */
  @JsonGetter("url")
  public URL url() {
    return url;
  }
}
