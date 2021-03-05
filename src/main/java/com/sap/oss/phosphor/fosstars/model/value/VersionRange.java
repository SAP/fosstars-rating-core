package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

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
   * Related CPE.
   */
  private final String cpe;

  /**
   * Create a version range with start and end version and related CPE.
   * Both versions are including.
   *
   * @param versionStart start version (including)
   * @param versionEnd end version (including)
   * @param cpe realted CPE
   */
  public VersionRange(
      @JsonProperty("versionStart") String versionStart,
      @JsonProperty("versionEnd") String versionEnd,
      @JsonProperty("cpe") String cpe) {
    this.versionStart = versionStart;
    this.versionEnd = versionEnd;
    this.cpe = cpe;
  }

  @JsonGetter("versionStart")
  public String versionStart() {
    return versionStart;
  }

  @JsonGetter("versionEnd")
  public String versionEnd() {
    return versionEnd;
  }

  @JsonGetter("cpe")
  public String cpe() {
    return cpe;
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
    return Objects.equals(versionStart, that.versionStart) && Objects
        .equals(versionEnd, that.versionEnd) && Objects.equals(cpe, that.cpe);
  }

  @Override
  public int hashCode() {
    return Objects.hash(versionStart, versionEnd, cpe);
  }
}
