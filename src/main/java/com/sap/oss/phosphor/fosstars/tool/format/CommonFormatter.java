package com.sap.oss.phosphor.fosstars.tool.format;

import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.score.oss.CommunityCommitmentScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.DependabotScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.DependencyScanScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.FindSecBugsScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.FuzzingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.LgtmScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.MemorySafetyTestingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.NoHttpToolScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OwaspDependencyScanScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectActivityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectPopularityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectSecurityAwarenessScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectSecurityTestingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.StaticAnalysisScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.UnpatchedVulnerabilitiesScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.VulnerabilityDiscoveryAndSecurityTestingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.VulnerabilityLifetimeScore;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckCvssThresholdValue;
import com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsageValue;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * The class contains common methods for formatters.
 */
public abstract class CommonFormatter implements Formatter {

  /**
   * Maps a class of feature to its shorter name which should be used in output.
   */
  private static final Map<Class<? extends Feature<?>>, String> FEATURE_CLASS_TO_NAME
      = new HashMap<>();

  static {
    FEATURE_CLASS_TO_NAME.put(OssSecurityScore.class, "Security of project");
    FEATURE_CLASS_TO_NAME.put(CommunityCommitmentScore.class, "Community commitment");
    FEATURE_CLASS_TO_NAME.put(ProjectActivityScore.class, "Project activity");
    FEATURE_CLASS_TO_NAME.put(ProjectPopularityScore.class, "Project popularity");
    FEATURE_CLASS_TO_NAME.put(ProjectSecurityAwarenessScore.class, "Security awareness");
    FEATURE_CLASS_TO_NAME.put(ProjectSecurityTestingScore.class, "Security testing");
    FEATURE_CLASS_TO_NAME.put(UnpatchedVulnerabilitiesScore.class, "Unpatched vulnerabilities");
    FEATURE_CLASS_TO_NAME.put(VulnerabilityLifetimeScore.class, "Vulnerability lifetime");
    FEATURE_CLASS_TO_NAME.put(MemorySafetyTestingScore.class, "Memory-safety testing");
    FEATURE_CLASS_TO_NAME.put(DependabotScore.class, "Dependabot score");
    FEATURE_CLASS_TO_NAME.put(OwaspDependencyScanScore.class, "OWASP Dependency Check score");
    FEATURE_CLASS_TO_NAME.put(DependencyScanScore.class, "Dependency testing");
    FEATURE_CLASS_TO_NAME.put(FuzzingScore.class, "Fuzzing");
    FEATURE_CLASS_TO_NAME.put(StaticAnalysisScore.class, "Static analysis");
    FEATURE_CLASS_TO_NAME.put(NoHttpToolScore.class, "nohttp tool");
    FEATURE_CLASS_TO_NAME.put(LgtmScore.class, "LGTM score");
    FEATURE_CLASS_TO_NAME.put(FindSecBugsScore.class, "FindSecBugs score");
    FEATURE_CLASS_TO_NAME.put(VulnerabilityDiscoveryAndSecurityTestingScore.class,
        "Vulnerability discovery and security testing");
  }

  /**
   * Maps a feature to its shorter name which should be used in output.
   */
  private static final Map<Feature<?>, String> FEATURE_TO_NAME = new HashMap<>();

  static {
    FEATURE_TO_NAME.put(OssFeatures.HAS_SECURITY_TEAM, "Does it have a security team?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_SECURITY_POLICY, "Does it have a security policy?");
    FEATURE_TO_NAME.put(OssFeatures.USES_SIGNED_COMMITS, "Does it use verified signed commits?");
    FEATURE_TO_NAME.put(OssFeatures.VULNERABILITIES, "Info about vulnerabilities");
    FEATURE_TO_NAME.put(OssFeatures.IS_APACHE, "Does it belong to Apache?");
    FEATURE_TO_NAME.put(OssFeatures.IS_ECLIPSE, "Does it belong to Eclipse?");
    FEATURE_TO_NAME.put(OssFeatures.SUPPORTED_BY_COMPANY, "Is it supported by a company?");
    FEATURE_TO_NAME.put(OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES,
        "Does it scan for vulnerable dependencies?");
    FEATURE_TO_NAME.put(OssFeatures.USES_GITHUB_FOR_DEVELOPMENT,
        "Does it use GitHub as the main development platform?");
    FEATURE_TO_NAME.put(OssFeatures.USES_ADDRESS_SANITIZER, "Does it use AddressSanitizer?");
    FEATURE_TO_NAME.put(OssFeatures.USES_MEMORY_SANITIZER, "Does it use MemorySanitizer?");
    FEATURE_TO_NAME.put(OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER,
        "Does it use UndefinedBehaviorSanitizer?");
    FEATURE_TO_NAME.put(OssFeatures.USES_NOHTTP, "Does it use nohttp?");
    FEATURE_TO_NAME.put(OssFeatures.USES_FIND_SEC_BUGS, "Does it use FindSecBugs?");
    FEATURE_TO_NAME.put(OssFeatures.USES_OWASP_ESAPI, "Does it use OWASP ESAPI?");
    FEATURE_TO_NAME.put(OssFeatures.USES_OWASP_JAVA_ENCODER, "Does it use OWASP Java Encoder?");
    FEATURE_TO_NAME.put(OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER,
        "Does it use OWASP Java HTML Sanitizer?");
    FEATURE_TO_NAME.put(OssFeatures.USES_DEPENDABOT, "Does it use Dependabot?");
    FEATURE_TO_NAME.put(OssFeatures.USES_LGTM_CHECKS, "Does it use LGTM checks?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_BUG_BOUNTY_PROGRAM, "Does it have a bug bounty program?");
    FEATURE_TO_NAME.put(OssFeatures.SIGNS_ARTIFACTS, "Does it sign artifacts?");
    FEATURE_TO_NAME.put(OssFeatures.WORST_LGTM_GRADE, "The worst LGTM grade of the project");
    FEATURE_TO_NAME.put(OssFeatures.FUZZED_IN_OSS_FUZZ, "Is it included to OSS-Fuzz?");
    FEATURE_TO_NAME.put(OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE,
        "How is OWASP Dependency Check used?");
    FEATURE_TO_NAME.put(OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD,
        "What is the threshold for OWASP Dependency Check?");
  }

  /**
   * An advisor for calculated ratings.
   */
  protected final Advisor advisor;

  /**
   * Initialize a new formatter.
   *
   * @param advisor An advisor for calculated ratings.
   */
  protected CommonFormatter(Advisor advisor) {
    this.advisor = Objects.requireNonNull(advisor, "Oh no! Advisor is null!");
  }

  /**
   * Returns a human-readable label for a confidence.
   *
   * @param confidence The confidence.
   * @return A human-readable label.
   */
  static String confidenceLabelFor(double confidence) {
    if (Double.compare(confidence, Confidence.MAX) == 0) {
      return "Max";
    }
    if (Double.compare(confidence, Confidence.MIN) == 0) {
      return "Min";
    }
    return confidence >= 9.0 ? "High" : "Low";
  }

  /**
   * Returns a human-readable label for a weight.
   *
   * @param weight The weight.
   * @return A human-readable label.
   */
  static String weightLabel(double weight) {
    if (weight < 0.3) {
      return "Low";
    }
    if (weight < 0.66) {
      return "Medium";
    }
    return "High";
  }

  /**
   * Figures out how a name of a feature should be printed out.
   *
   * @param feature The feature.
   * @return A name of the feature.
   */
  static String nameOf(Feature<?> feature) {
    for (Map.Entry<Class<? extends Feature<?>>, String> entry : FEATURE_CLASS_TO_NAME.entrySet()) {
      if (feature.getClass() == entry.getKey()) {
        return entry.getValue();
      }
    }

    for (Map.Entry<Feature<?>, String> entry : FEATURE_TO_NAME.entrySet()) {
      if (feature.equals(entry.getKey())) {
        return entry.getValue();
      }
    }

    return feature.name();
  }

  /**
   * Formats a specified value.
   *
   * @param value The value to be printed out.
   * @return A formatted value.
   */
  protected String actualValueOf(Value<?> value) {
    if (value.isUnknown()) {
      return "unknown";
    }

    if (value instanceof BooleanValue) {
      BooleanValue booleanValue = (BooleanValue) value;
      return booleanValue.get() ? "Yes" : "No";
    }

    if (value instanceof OwaspDependencyCheckUsageValue) {
      OwaspDependencyCheckUsageValue usageValue = (OwaspDependencyCheckUsageValue) value;
      return StringUtils.capitalize(
          usageValue.get().toString().replace("_", " ").toLowerCase());
    }

    if (value instanceof OwaspDependencyCheckCvssThresholdValue) {
      OwaspDependencyCheckCvssThresholdValue threshold
          = (OwaspDependencyCheckCvssThresholdValue) value;
      return threshold.specified() ? String.valueOf(threshold.get()) : "Not specified";
    }

    return value.get().toString();
  }
}
