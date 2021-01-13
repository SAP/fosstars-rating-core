package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.feature.OwaspDependencyCheckCvssThreshold;
import java.util.Objects;

/**
 * This feature value contains a CVSS threshold used in OWASP Dependency Check.
 */
public class OwaspDependencyCheckCvssThresholdValue extends DoubleValue {

  /**
   * Shows if a valid CVSS threshold is specified or not.
   */
  private final boolean specified;

  /**
   * Initializes a {@link OwaspDependencyCheckCvssThresholdValue} of a specified feature.
   *
   * @param feature The feature.
   * @param cvss A CVSS score.
   * @param specified Tells if the CVSS score is specified.
   */
  @JsonCreator
  public OwaspDependencyCheckCvssThresholdValue(
      @JsonProperty("feature") OwaspDependencyCheckCvssThreshold feature,
      @JsonProperty("number") Double cvss,
      @JsonProperty("specified") boolean specified) {

    super(feature, cvss);
    CVSS.check(cvss);
    this.specified = specified; 
  }

  /**
   * Checks if a valid CVSS threshold is specified or not.
   *  
   * @return True if a valid threshold is set, false otherwise.
   */
  @JsonGetter("specified")
  public boolean specified() {
    return specified;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof OwaspDependencyCheckCvssThresholdValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    OwaspDependencyCheckCvssThresholdValue value = (OwaspDependencyCheckCvssThresholdValue) o;
    return specified == value.specified;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), specified);
  }

  @Override
  public String toString() {
    return specified ? String.valueOf(number) : "Not specified";
  }
}