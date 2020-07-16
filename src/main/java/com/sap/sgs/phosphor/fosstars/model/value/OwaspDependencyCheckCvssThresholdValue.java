package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;

/**
 * A value of a feature which indicates the CVSS threshold used in OWASP Dependency Check.
 */
public class OwaspDependencyCheckCvssThresholdValue extends DoubleValue {

  /**
   * If a valid CVSS threshold is specified or not. 
   */
  private final boolean specified;

  /**
   * Initializes a {@link OwaspDependencyCheckCvssThresholdValue} of a specified feature.
   *
   * @param feature The feature.
   * @param number The value.
   * @param specified if the appropriate value is specified.
   */
  @JsonCreator
  public OwaspDependencyCheckCvssThresholdValue(
      @JsonProperty("feature") Feature<Double> feature,
      @JsonProperty("number") Double number,
      @JsonProperty("specified") boolean specified) {
    super(feature, number);
    this.specified = specified; 
  }

  /**
   * Returns if a valid CVSS threshold is specified or not.
   *  
   * @return True if a valid threshold is set, false otherwise.
   */
  @JsonGetter("specified")
  public boolean specified() {
    return specified;
  }

  @Override
  @JsonGetter("number")
  public Double get() {
    if (specified) {
      return number;
    }

    throw new IllegalArgumentException("The value has to be within range [0.0, 10.0]!!!");
  }
}