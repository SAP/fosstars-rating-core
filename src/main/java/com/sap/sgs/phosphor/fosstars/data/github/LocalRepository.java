package com.sap.sgs.phosphor.fosstars.data.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 * The class holds information about a cloned repository.
 */
public class LocalRepository {

  /**
   * A date when the repository was updated.
   */
  private Date updated;

  /**
   * A path to the repository.
   */
  private final Path path;

  /**
   * Initializes a repository.
   */
  public LocalRepository(Path path) {
    this(path, Date.from(Instant.now()));
  }

  /**
   * Initializes a repository.
   *
   * @param updated A date when the repository was updated.
   * @param path A path to the repository.
   */
  @JsonCreator
  public LocalRepository(
      @JsonProperty("path") Path path,
      @JsonProperty("updated") Date updated) {

    Objects.requireNonNull(path, "Hey! Path can't be null!");
    Objects.requireNonNull(updated, "Hey! Date can't be null!");

    this.path = path;
    this.updated = updated;
  }

  /**
   * Returns a date when the repository was updated.
   */
  @JsonGetter("updated")
  public Date updated() {
    return updated;
  }

  /**
   * Updates the date when the repository was updated.
   *
   * @param date A new date.
   */
  public void updated(Date date) {
    updated = date;
  }

  /**
   * Returns a path to the repository.
   */
  @JsonGetter("path")
  public Path path() {
    return path;
  }
}
