package com.sap.sgs.phosphor.fosstars.model.feature;

import static com.sap.sgs.phosphor.fosstars.model.value.CVSS.MAX;
import static com.sap.sgs.phosphor.fosstars.model.value.CVSS.MIN;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckCvssThresholdValue;

/**
 * <p>The feature holds a CVSS threshold for OWASP Dependency Check to fail the build.</p>
 * 
 * @see <a href=
 *      "https://jeremylong.github.io/DependencyCheck/dependency-check-maven/configuration.html">
 *      OWASP Dependency Check configuration</a>
 */
public class OwaspDependencyCheckCvssThreshold extends BoundedDoubleFeature {

  /**
   * A junk value, x < 0.0 or x > 10.0.
   */
  // TODO: this constant will need to be removed,
  //       and the feature/value need to check in n belongs to [0. 10]
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
   * Creates an instance of {@link OwaspDependencyCheckCvssThresholdValue} that shows that
   * no threshold specified.
   * 
   * @return An instance of {@link OwaspDependencyCheckCvssThresholdValue}.
   */
  public OwaspDependencyCheckCvssThresholdValue notSpecifiedValue() {
    return new OwaspDependencyCheckCvssThresholdValue(this, JUNK_VALUE, false);
  }
}