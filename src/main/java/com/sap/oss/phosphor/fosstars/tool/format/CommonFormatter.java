package com.sap.oss.phosphor.fosstars.tool.format;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
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
    FEATURE_TO_NAME.put(OssFeatures.USES_REUSE, "Does the project use REUSE?");
    FEATURE_TO_NAME.put(OssFeatures.README_HAS_REUSE_INFO, "Does README mention REUSE?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_REUSE_LICENSES,
        "Does it have LICENSES directory with licenses?");
    FEATURE_TO_NAME.put(OssFeatures.REGISTERED_IN_REUSE, "Is it registered in REUSE?");
    FEATURE_TO_NAME.put(OssFeatures.IS_REUSE_COMPLIANT, "Is it compliant with REUSE rules?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT,
        "Does the project have open pull requests from Dependabot?");
    FEATURE_TO_NAME.put(OssFeatures.PACKAGE_MANAGERS, "Package managers");
    FEATURE_TO_NAME.put(OssFeatures.LANGUAGES, "Programming languages");
    FEATURE_TO_NAME.put(OssFeatures.RUNS_CODEQL_SCANS, "Does it run CodeQL scans?");
    FEATURE_TO_NAME.put(OssFeatures.USES_CODEQL_CHECKS,
        "Does it use CodeQL checks for pull requests?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_LICENSE, "Does it have a license file?");
    FEATURE_TO_NAME.put(OssFeatures.ALLOWED_LICENSE, "Does it use an allowed license?");
    FEATURE_TO_NAME.put(OssFeatures.LICENSE_HAS_DISALLOWED_CONTENT,
        "Does the license have disallowed content?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_README, "Does it have a README file?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_CONTRIBUTING_GUIDELINE,
        "Does it have a contributing guideline?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE,
        "Does the contributing guideline have required text?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_ADMIN_TEAM_ON_GITHUB,
        "Does it have an admin team on GitHub?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_ENOUGH_ADMINS_ON_GITHUB,
        "Does it have enough admins on GitHub?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_ENOUGH_TEAMS_ON_GITHUB,
        "Does it have enough teams on GitHub?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB,
        "Does teams have enough members on GitHub?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB,
        "Does it have a team with push privileges on GitHub?");
    FEATURE_TO_NAME.put(OssFeatures.HAS_UNRESOLVED_VULNERABILITY_ALERTS,
        "Does it have unresolved vulnerability alerts?");
    FEATURE_TO_NAME.put(OssFeatures.ENABLED_VULNERABILITY_ALERTS_ON_GITHUB,
        "Are vulnerability alerts enabled?");

  }

  /**
   * A formatter for doubles.
   */
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

  static {
    DECIMAL_FORMAT.setMinimumFractionDigits(1);
    DECIMAL_FORMAT.setMaximumFractionDigits(2);
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
  public static String nameOf(Feature<?> feature) {
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

  /**
   * Loads a resource.
   *
   * @param resource A name of the resource.
   * @param clazz A class for loading the resource.
   * @return The content of the resource.
   */
  static String loadFrom(String resource, Class<?> clazz) {
    try (InputStream is = clazz.getResourceAsStream(resource)) {
      return IOUtils.toString(is, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException("Holy moly! Could not load template!", e);
    }
  }

  /**
   * Formats a double.
   *
   * @param n The double to be formatted.
   * @return A formatted string.
   */
  static String formatted(double n) {
    return DECIMAL_FORMAT.format(n);
  }
}
