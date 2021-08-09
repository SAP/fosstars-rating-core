package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.NOT_USED;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.DOTNET;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.GRADLE;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.CVSS;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckCvssThresholdValue;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * The scores assesses how well an open-source project uses OWASP Dependency Check to scan
 * dependencies for known vulnerabilities. It is based on the following features:
 * <ul>
 *   <li>{@link OssFeatures#OWASP_DEPENDENCY_CHECK_USAGE}</li>
 *   <li>{@link OssFeatures#OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD}</li>
 * </ul>
 */
public class OwaspDependencyScanScore extends FeatureBasedScore {

  /**
   * The basic step score that is allotted in each step.
   */
  private static final double STEP_SCORE = 3.0;

  /**
   * Package ecosystems that are supported by OWASP Dependency Check.
   */
  private static final PackageManagers SUPPORTED_PACKAGE_MANAGERS
      = PackageManagers.from(MAVEN, GRADLE, DOTNET);

  /**
   * Initializes a new {@link OwaspDependencyScanScore}.
   */
  OwaspDependencyScanScore() {
    super("How a project uses OWASP Dependency Check to scan dependencies for vulnerabilities",
        OWASP_DEPENDENCY_CHECK_USAGE,
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Double> thresholdValue = find(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD, values);
    Value<OwaspDependencyCheckUsage> usageValue = find(OWASP_DEPENDENCY_CHECK_USAGE, values);
    Value<PackageManagers> packageManagersValue = find(PACKAGE_MANAGERS, values);

    ScoreValue scoreValue = scoreValue(MIN, usageValue, thresholdValue, packageManagersValue);

    if (allUnknown(scoreValue.usedValues())) {
      return scoreValue.makeUnknown();
    }

    if (!packageManagersValue.isUnknown()
        && !SUPPORTED_PACKAGE_MANAGERS.containsAny(packageManagersValue.get())) {

      return scoreValue.makeNotApplicable();
    }

    OwaspDependencyCheckUsage usage = usageValue.orElse(NOT_USED);
    switch (usage) {
      case NOT_USED:
        return scoreValue;
      case MANDATORY:
        scoreValue.set(MAX - STEP_SCORE);
        break;
      case OPTIONAL:
        scoreValue.set(STEP_SCORE);
        break;
      default:
        throw new IllegalArgumentException("Unexpected usage!");
    }

    if (thresholdValue.isUnknown()) {
      return scoreValue;
    }

    if (thresholdValue instanceof OwaspDependencyCheckCvssThresholdValue == false) {
      throw new IllegalArgumentException("Expected OwaspDependencyCheckCvssThresholdValue!");
    }

    OwaspDependencyCheckCvssThresholdValue value =
        (OwaspDependencyCheckCvssThresholdValue) thresholdValue;

    // the lower the threshold, the better
    if (value.specified()) {
      scoreValue.increase(STEP_SCORE * (CVSS.MAX - value.get()) / CVSS.MAX);
    }

    return scoreValue;
  }
}