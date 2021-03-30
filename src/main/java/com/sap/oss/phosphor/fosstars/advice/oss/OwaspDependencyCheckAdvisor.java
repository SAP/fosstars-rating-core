package com.sap.oss.phosphor.fosstars.advice.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;

import com.sap.oss.phosphor.fosstars.advice.Advice;
import com.sap.oss.phosphor.fosstars.advice.oss.OssAdviceContentYamlStorage.OssAdviceContext;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.oss.OwaspDependencyScanScore;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckCvssThresholdValue;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsageValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * An advisor for features related to OWASP Dependency Check.
 */
public class OwaspDependencyCheckAdvisor extends AbstractOssAdvisor {

  /**
   * Create a new advisor.
   *
   * @param contextFactory A factory that provides contexts for advice.
   */
  public OwaspDependencyCheckAdvisor(OssAdviceContextFactory contextFactory) {
    super(OssAdviceContentYamlStorage.DEFAULT, contextFactory);
  }

  @Override
  protected List<Advice> adviceFor(
      Subject subject, List<Value<?>> usedValues, OssAdviceContext context)
      throws MalformedURLException {

    Optional<ScoreValue> owaspDependencyScanScore
        = findSubScoreValue(subject, OwaspDependencyScanScore.class);

    if (!owaspDependencyScanScore.isPresent() || owaspDependencyScanScore.get().isNotApplicable()) {
      return Collections.emptyList();
    }

    List<Advice> advice = new ArrayList<>();

    advice.addAll(
        adviceForFeature(usedValues, OWASP_DEPENDENCY_CHECK_USAGE, subject, context,
            OwaspDependencyCheckAdvisor::notMandatoryOwaspDependencyCheck));

    advice.addAll(
        adviceForFeature(usedValues, OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD, subject, context,
            OwaspDependencyCheckAdvisor::noThresholdForOwaspDependencyCheck));

    return advice;
  }

  /**
   * Checks if a value is
   * {@link com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsageValue}
   * and it is equal to
   * {@link com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage#MANDATORY}.
   *
   * @param value The value to be checked.
   * @return True is the value is equal to
   *         {@link com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage#MANDATORY},
   *         false otherwise.
   */
  private static boolean notMandatoryOwaspDependencyCheck(Value<?> value) {
    return !value.isUnknown()
        && value instanceof OwaspDependencyCheckUsageValue
        && MANDATORY != value.get();
  }

  /**
   * Check if a value is {@link OwaspDependencyCheckCvssThresholdValue}
   * and a threshold is specified.
   *
   * @param value The value to be checked.
   * @return True if the threshold for OWASP Dependency Check is specified.
   */
  private static boolean noThresholdForOwaspDependencyCheck(Value<?> value) {
    if (value.isUnknown()) {
      return false;
    }

    if (value instanceof OwaspDependencyCheckCvssThresholdValue) {
      OwaspDependencyCheckCvssThresholdValue thresholdValue
          = (OwaspDependencyCheckCvssThresholdValue) value;
      return !thresholdValue.specified();
    }

    return false;
  }
}
