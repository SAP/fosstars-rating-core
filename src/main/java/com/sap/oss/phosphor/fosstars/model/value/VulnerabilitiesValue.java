package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * This is a value that contains information about vulnerabilities.
 */
public class VulnerabilitiesValue extends AbstractKnownValue<Vulnerabilities> {

  /**
   * Vulnerabilities.
   */
  private final Vulnerabilities vulnerabilities;

  /**
   * Initializes a new value with a collection of vulnerabilities.
   *
   * @param feature The vulnerabilities feature.
   * @param vulnerabilities The vulnerabilities.
   */
  @JsonCreator
  public VulnerabilitiesValue(
      @JsonProperty("feature") Feature<Vulnerabilities> feature,
      @JsonProperty("vulnerabilities") Vulnerabilities vulnerabilities) {
    super(feature);

    Objects.requireNonNull(vulnerabilities, "Vulnerabilities can't be null!");
    this.vulnerabilities = vulnerabilities;
  }

  @Override
  @JsonGetter("vulnerabilities")
  public final Vulnerabilities get() {
    return vulnerabilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof VulnerabilitiesValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    VulnerabilitiesValue that = (VulnerabilitiesValue) o;
    return Objects.equals(vulnerabilities, that.vulnerabilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), vulnerabilities);
  }

  @Override
  public String toString() {
    return vulnerabilities.toString();
  }
}
