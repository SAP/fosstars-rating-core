package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Optional;

/**
 * The class holds a version range.
 */
public class VersionRange {

  /**
   * A start version.
   */
  private final String versionStart;

  /**
   * An end version.
   */
  private final String versionEnd;

  /**
   * Create a version range with start and end versions.
   * Both versions are including.
   * Start and end version may be null.
   *
   * @param versionStart The start version.
   * @param versionEnd The end version.
   */
  public VersionRange(
      @JsonProperty("versionStart") String versionStart,
      @JsonProperty("versionEnd") String versionEnd) {

    this.versionStart = versionStart;
    this.versionEnd = versionEnd;
  }

  /**
   * Returns the start version.
   *
   * @return The start version if available.
   */
  public Optional<String> start() {
    return Optional.ofNullable(versionStart);
  }

  /**
   * Returns the end version if available.
   *
   * @return The end version if available.
   */
  public Optional<String> end() {
    return Optional.ofNullable(versionEnd);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VersionRange that = (VersionRange) o;
    return Objects.equals(versionStart, that.versionStart)
        && Objects.equals(versionEnd, that.versionEnd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(versionStart, versionEnd);
  }

  /**
   * Returns the start version. This is for serialization with Jackson.
   *
   * @return The start version.
   */
  @JsonGetter("versionStart")
  private String versionStart() {
    return versionStart;
  }

  /**
   * Returns the end version. This is for serialization with Jackson.
   *
   * @return The end version.
   */
  @JsonGetter("versionEnd")
  private String versionEnd() {
    return versionEnd;
  }
}
