package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * The class holds a CVSS score for a vulnerability.
 */
public class CVSS {

  /**
   * The minimum CVSS score.
   */
  public static final double MIN = 0.0;

  /**
   * The maximum CVSS score.
   */
  public static final double MAX = 10.0;

  /**
   * An unknown CVSS score.
   */
  public static final CVSS UNKNOWN = new CVSS(Version.UNKNOWN, null);

  /**
   * CVSS version.
   */
  public enum Version {
    V2, V3, UNKNOWN
  }

  /**
   * CVSS version.
   */
  private final Version version;

  /**
   * A score.
   */
  private final Double value;

  /**
   * Initializes a CVSS score.
   *
   * @param version CVSS version.
   * @param value A score in the interval [0, 10].
   */
  public CVSS(
      @JsonProperty("version") Version version,
      @JsonProperty("value") Double value) {

    Objects.requireNonNull(version, "Hey! CVSS version can't be null!");

    this.version = version;
    this.value = check(value);
  }

  /**
   * Get the CVSS score.
   *
   * @return The CVSS score.
   */
  @JsonGetter("value")
  public Double value() {
    return value;
  }

  /**
   * Get the CVSS version.
   *
   * @return The CVSS version.
   */
  @JsonGetter("version")
  public Version version() {
    return version;
  }

  /**
   * Tells whether the score is unknown or not.
   *
   * @return True if the score is unknown, false otherwise.
   */
  @JsonIgnore
  public boolean isUnknown() {
    return value == null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof CVSS == false) {
      return false;
    }
    CVSS cvss = (CVSS) o;
    return version == cvss.version && Objects.equals(value, cvss.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, value);
  }

  /**
   * Creates a CVSS score of version 2.
   *
   * @param value The score value.
   * @return An instance of {@link CVSS}
   */
  public static CVSS v2(double value) {
    return new CVSS(Version.V2, value);
  }

  /**
   * Creates a CVSS score of version 3.
   *
   * @param value The score value.
   * @return An instance of {@link CVSS}
   */
  public static CVSS v3(double value) {
    return new CVSS(Version.V3, value);
  }

  /**
   * Checks that a score belongs to the interval [0, 10], it's null.
   *
   * @param value The score value to be checked.
   * @return The score value if it's valid or null.
   * @throws IllegalArgumentException If the score value is invalid.
   */
  private static Double check(Double value) {
    if (value == null) {
      return null;
    }
    if (value < MIN || value > MAX) {
      throw new IllegalArgumentException(String.format(
          "What the heck? %s doesn't look like a CVSS score!", value));
    }
    return value;
  }

}
