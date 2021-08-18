package com.sap.oss.phosphor.fosstars.model.value;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;
import java.util.Optional;

/**
 * The class holds a CVSS score for a vulnerability.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class CVSS {

  /**
   * CVSS version 2.
   */
  public static class V2 extends CVSS {

    /**
     * Unknown impact.
     */
    public static final Impact UNKNOWN_IMPACT = null;

    /**
     * Possible impact values.
     */
    public enum Impact {
      NONE, PARTIAL, COMPLETE
    }

    /**
     * Confidentiality impact.
     */
    @JsonProperty("confidentialityImpact")
    private final Impact confidentialityImpact;

    /**
     * Integrity impact.
     */
    @JsonProperty("integrityImpact")
    private final Impact integrityImpact;

    /**
     * Availability impact.
     */
    @JsonProperty("availabilityImpact")
    private final Impact availabilityImpact;

    /**
     * Initializes a CVSS score v3.
     *
     * @param value A score in the interval [0, 10].
     * @param confidentialityImpact Confidentiality impact.
     * @param integrityImpact Integrity impact.
     * @param availabilityImpact Availability impact.
     */
    public V2(
        @JsonProperty("value") Double value,
        @JsonProperty("confidentialityImpact") Impact confidentialityImpact,
        @JsonProperty("integrityImpact") Impact integrityImpact,
        @JsonProperty("availabilityImpact") Impact availabilityImpact) {

      super(value);
      this.confidentialityImpact = confidentialityImpact;
      this.integrityImpact = integrityImpact;
      this.availabilityImpact = availabilityImpact;
    }

    /**
     * Get the confidentiality impact.
     *
     * @return The confidentiality impact if available.
     */
    public Optional<Impact> confidentialityImpact() {
      return Optional.ofNullable(confidentialityImpact);
    }

    /**
     * Get the integrity impact.
     *
     * @return The integrity impact if available.
     */
    public Optional<Impact> integrityImpact() {
      return Optional.ofNullable(integrityImpact);
    }

    /**
     * Get the availability impact.
     *
     * @return The availability impact if available.
     */
    public Optional<Impact> availabilityImpact() {
      return Optional.ofNullable(availabilityImpact);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!V2.class.isAssignableFrom(o.getClass())) {
        return false;
      }
      if (!super.equals(o)) {
        return false;
      }
      V2 v2 = (V2) o;
      return confidentialityImpact == v2.confidentialityImpact
          && integrityImpact == v2.integrityImpact
          && availabilityImpact == v2.availabilityImpact;
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(),
          confidentialityImpact, integrityImpact, availabilityImpact);
    }
  }

  /**
   * CVSS version 3.
   */
  public static class V3 extends CVSS {

    /**
     * Unknown impact.
     */
    public static final Impact UNKNOWN_IMPACT = null;

    /**
     * Possible impact values.
     */
    public enum Impact {
      NONE, LOW, HIGH
    }

    /**
     * Confidentiality impact.
     */
    @JsonProperty("confidentialityImpact")
    private final Impact confidentialityImpact;

    /**
     * Integrity impact.
     */
    @JsonProperty("integrityImpact")
    private final Impact integrityImpact;

    /**
     * Availability impact.
     */
    @JsonProperty("availabilityImpact")
    private final Impact availabilityImpact;

    /**
     * Initializes a CVSS score v3.
     *
     * @param value A score in the interval [0, 10].
     * @param confidentialityImpact Confidentiality impact.
     * @param integrityImpact Integrity impact.
     * @param availabilityImpact Availability impact.
     */
    public V3(
        @JsonProperty("value") Double value,
        @JsonProperty("confidentialityImpact") Impact confidentialityImpact,
        @JsonProperty("integrityImpact") Impact integrityImpact,
        @JsonProperty("availabilityImpact") Impact availabilityImpact) {

      super(value);
      this.confidentialityImpact = confidentialityImpact;
      this.integrityImpact = integrityImpact;
      this.availabilityImpact = availabilityImpact;
    }

    /**
     * Get the confidentiality impact.
     *
     * @return The confidentiality impact if available.
     */
    public Optional<Impact> confidentialityImpact() {
      return Optional.ofNullable(confidentialityImpact);
    }

    /**
     * Get the integrity impact.
     *
     * @return The integrity impact if available.
     */
    public Optional<Impact> integrityImpact() {
      return Optional.ofNullable(integrityImpact);
    }

    /**
     * Get the availability impact.
     *
     * @return The availability impact if available.
     */
    public Optional<Impact> availabilityImpact() {
      return Optional.ofNullable(availabilityImpact);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!V3.class.isAssignableFrom(o.getClass())) {
        return false;
      }
      if (!super.equals(o)) {
        return false;
      }
      V3 v3 = (V3) o;
      return confidentialityImpact == v3.confidentialityImpact
          && integrityImpact == v3.integrityImpact
          && availabilityImpact == v3.availabilityImpact;
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(),
          confidentialityImpact, integrityImpact, availabilityImpact);
    }
  }

  /**
   * The minimum CVSS score.
   */
  public static final double MIN = 0.0;

  /**
   * The maximum CVSS score.
   */
  public static final double MAX = 10.0;

  /**
   * CVSS version.
   */
  public enum Version {
    V2, V3
  }

  /**
   * A score.
   */
  private final Double value;

  /**
   * Initializes a CVSS score.
   *
   * @param value A score in the interval [0, 10].
   */
  public CVSS(@JsonProperty("value") Double value) {
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
   * Get the confidentiality impact.
   *
   * @return The confidentiality impact if available.
   */
  public abstract Optional<?> confidentialityImpact();

  /**
   * Get the integrity impact.
   *
   * @return The integrity impact if available.
   */
  public abstract Optional<?> integrityImpact();

  /**
   * Get the availability impact.
   *
   * @return The availability impact if available.
   */
  public abstract Optional<?> availabilityImpact();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!CVSS.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    CVSS cvss = (CVSS) o;
    return Objects.equals(value, cvss.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  /**
   * Checks that a score belongs to the interval [0, 10], it's null.
   *
   * @param value The score value to be checked.
   * @return The score value if it's valid or null.
   * @throws IllegalArgumentException If the score value is invalid.
   */
  public static Double check(Double value) {
    if (value == null) {
      return null;
    }

    if (Double.compare(value, MIN) < 0 || Double.compare(value, MAX) > 0) {
      throw new IllegalArgumentException(format(
          "What the heck? %f doesn't look like a CVSS score!", value));
    }

    return value;
  }

}
