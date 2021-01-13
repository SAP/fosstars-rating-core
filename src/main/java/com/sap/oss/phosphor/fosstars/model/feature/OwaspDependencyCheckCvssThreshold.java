package com.sap.oss.phosphor.fosstars.model.feature;

import static com.sap.oss.phosphor.fosstars.model.value.CVSS.MAX;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.MIN;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckCvssThresholdValue;

/**
 * <p>The feature holds a CVSS threshold for OWASP Dependency Check to fail the build.</p>
 * 
 * @see <a href=
 *      "https://jeremylong.github.io/DependencyCheck/dependency-check-maven/configuration.html">
 *      OWASP Dependency Check configuration</a>
 */
public class OwaspDependencyCheckCvssThreshold extends BoundedDoubleFeature {

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
    // it doesn't matter what CVSS score value is used here because the created value throws
    // and exception if the get() method is called
    // therefore, it just uses MIN value
    return new OwaspDependencyCheckCvssThresholdValue(this, MIN, false);
  }
}