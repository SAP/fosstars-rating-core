package com.sap.sgs.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;

/**
 * A feature which holds a {@link BoundedDoubleFeature} value. Specific to CVSS margin values [0.0,
 * 10.0]. But we consider an out of bound value 11.0. If the cvss threshold is out of bound, it
 * would not be considered during score calculation.
 * 
 * @see <a
 *      href="https://jeremylong.github.io/DependencyCheck/dependency-check-maven/configuration.html ">OWASP
 *      Dependency Check configuration</a>
 */
public class OwaspDependencyCheckCvssThreshold extends BoundedDoubleFeature {

  /**
   * The default out of bound CVSS score. The number is greater than 10.0
   */
  public static final double OUT_OF_BOUND = 11.0;

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  @JsonCreator
  public OwaspDependencyCheckCvssThreshold(@JsonProperty("name") String name) {
    super(name, CVSS.MIN, CVSS.MAX);
  }

  @Override
  protected Double check(Double n) {
    if (n < CVSS.MIN || n > CVSS.MAX) {
      return OUT_OF_BOUND;
    }

    return n;
  }
}
