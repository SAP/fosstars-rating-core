package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * The class holds a start and end version.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VersionRange {

  /**
   * First vulnerable version (including).
   */
  private final String versionStart;

  /**
   * Last vulnerable version (including).
   */
  private final String versionEnd;

  /**
   * Create a version range with start and end version.
   * Both versions are including.
   * Start and end version may be null.
   *
   * @param versionStart start version (including)
   * @param versionEnd end version (including)
   */
  public VersionRange(
      @JsonProperty("versionStart") String versionStart,
      @JsonProperty("versionEnd") String versionEnd) {

    this.versionStart = versionStart;
    this.versionEnd = versionEnd;
  }

  @JsonGetter("versionStart")
  public String versionStart() {
    return versionStart;
  }

  @JsonGetter("versionEnd")
  public String versionEnd() {
    return versionEnd;
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
}
