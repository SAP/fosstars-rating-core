package com.sap.sgs.phosphor.fosstars.model.feature;

import static com.sap.sgs.phosphor.fosstars.model.value.CVSS.MAX;
import static com.sap.sgs.phosphor.fosstars.model.value.CVSS.MIN;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckCvssThresholdValue;

/**
 * <p>A feature which holds a {@link DoubleFeature} value. Specific to CVSS margin values 
 * [0.0, 10.0]. But we consider a {@link #JUNK_VALUE} if not in the specific range. If the 
 * CVSS threshold is out of range, it would not be considered during score calculation.</p>
 * 
 * @see <a href=
 *      "https://jeremylong.github.io/DependencyCheck/dependency-check-maven/configuration.html">
 *      OWASP Dependency Check configuration</a>
 */
public class OwaspDependencyCheckCvssThreshold extends BoundedDoubleFeature {

  /**
   * A junk value, x < 0.0 or x > 10.0.
   */
  private static final double JUNK_VALUE = 11.0;

  /**
   * Initializes a feature.
   */
  @JsonCreator
  public OwaspDependencyCheckCvssThreshold() {
    super("A CVSS threshold for OWASP Dependency Check to fail the build", MIN, MAX);
  }

  @Override
  public OwaspDependencyCheckCvssThresholdValue value(Double n) {
    return new OwaspDependencyCheckCvssThresholdValue(this, n, true);
  }

  /**
   * An instance of {@link OwaspDependencyCheckCvssThresholdValue} if n is not in the given range of
   * [0.0, 10.0].
   * 
   * @return New instance of {@link OwaspDependencyCheckCvssThresholdValue} with a
   *         {@link #JUNK_VALUE}.
   */
  public OwaspDependencyCheckCvssThresholdValue notSpecifiedValue() {
    return new OwaspDependencyCheckCvssThresholdValue(this, JUNK_VALUE, false);
  }
}