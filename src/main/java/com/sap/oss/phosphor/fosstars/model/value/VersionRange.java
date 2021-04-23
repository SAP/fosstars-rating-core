package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Optional;

/**
 * The class holds a start and end version.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class VersionRange {

  /**
   * First vulnerable version (including).
   */
  private final String versionStartIncluding;

  /**
   * Last vulnerable version (including).
   */
  private final String versionEndIncluding;

  /**
   * First vulnerable version (excluding).
   */
  private final String versionStartExcluding;

  /**
   * Last vulnerable version (excluding).
   */
  private final String versionEndExcluding;

  /**
   * Create a version range with start and end version. Version may be null.
   *
   * @param versionStartIncluding start version (including)
   * @param versionStartExcluding start version (excluding)
   * @param versionEndIncluding end version (including)
   * @param versionEndExcluding end version (excluding)
   */
  public VersionRange(@JsonProperty("versionStartIncluding") String versionStartIncluding,
      @JsonProperty("versionStartExcluding") String versionStartExcluding,
      @JsonProperty("versionEndIncluding") String versionEndIncluding,
      @JsonProperty("versionEndExcluding") String versionEndExcluding) {
    this.versionStartIncluding = versionStartIncluding;
    this.versionStartExcluding = versionStartExcluding;
    this.versionEndIncluding = versionEndIncluding;
    this.versionEndExcluding = versionEndExcluding;
  }

  public Optional<String> versionStartIncluding() {
    return Optional.ofNullable(versionStartIncluding);
  }

  public Optional<String> versionStartExcluding() {
    return Optional.ofNullable(versionStartExcluding);
  }

  public Optional<String> versionEndIncluding() {
    return Optional.ofNullable(versionEndIncluding);
  }

  public Optional<String> versionEndExcluding() {
    return Optional.ofNullable(versionEndExcluding);
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
    return Objects.equals(versionStartIncluding, that.versionStartIncluding)
        && Objects.equals(versionEndIncluding, that.versionEndIncluding)
        && Objects.equals(versionStartExcluding, that.versionStartExcluding)
        && Objects.equals(versionEndExcluding, that.versionEndExcluding);
  }

  @Override
  public int hashCode() {
    return Objects.hash(versionStartIncluding, versionStartExcluding, versionEndIncluding,
        versionEndExcluding);
  }
}
