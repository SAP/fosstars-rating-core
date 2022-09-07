package com.sap.oss.phosphor.fosstars.tool.format;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ALLOWED_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ENABLED_VULNERABILITY_ALERTS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ADMIN_TEAM_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_BUG_BOUNTY_PROGRAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_CODE_OF_CONDUCT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_CONTRIBUTING_GUIDELINE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_ADMINS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_TEAMS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_EXECUTABLE_BINARIES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_LICENSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_SNYK;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_README;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REUSE_LICENSES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_UNRESOLVED_VULNERABILITY_ALERTS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.INCOMPLETE_README;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_REUSE_COMPLIANT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LICENSE_HAS_DISALLOWED_CONTENT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.README_HAS_REUSE_INFO;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.REGISTERED_IN_REUSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_GOSEC_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_MYPY_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_PYLINT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_WITH_RULES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MYPY_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_ENCODER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_PYLINT_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_REUSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SIGNED_COMMITS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SNYK;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_ARTIFACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_PROJECT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.AVAILABILITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.CONFIDENTIALITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.DATA_CONFIDENTIALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.FUNCTIONALITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.INTEGRITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.IS_ADOPTED;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.PROJECT_USAGE;

import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.oss.BanditScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.CommunityCommitmentScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.DependabotScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.DependencyScanScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.FindSecBugsScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.FuzzingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.GoSecScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.LgtmScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.MemorySafetyTestingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.NoHttpToolScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OwaspDependencyScanScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectActivityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectPopularityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectSecurityAwarenessScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.ProjectSecurityTestingScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.SecurityReviewScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.SnykDependencyScanScore;
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
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;

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
    add(OssSecurityScore.class, "Security of project");
    add(CommunityCommitmentScore.class, "Community commitment");
    add(ProjectActivityScore.class, "Project activity");
    add(ProjectPopularityScore.class, "Project popularity");
    add(ProjectSecurityAwarenessScore.class, "Security awareness");
    add(ProjectSecurityTestingScore.class, "Security testing");
    add(UnpatchedVulnerabilitiesScore.class, "Unpatched vulnerabilities");
    add(VulnerabilityLifetimeScore.class, "Vulnerability lifetime");
    add(MemorySafetyTestingScore.class, "Memory-safety testing");
    add(DependabotScore.class, "Dependabot score");
    add(SnykDependencyScanScore.class, "Snyk score");
    add(OwaspDependencyScanScore.class, "OWASP Dependency Check score");
    add(DependencyScanScore.class, "Dependency testing");
    add(FuzzingScore.class, "Fuzzing");
    add(StaticAnalysisScore.class, "Static analysis");
    add(NoHttpToolScore.class, "nohttp tool");
    add(BanditScore.class, "Bandit score");
    add(LgtmScore.class, "LGTM score");
    add(GoSecScore.class, "GoSec score");
    add(FindSecBugsScore.class, "FindSecBugs score");
    add(VulnerabilityDiscoveryAndSecurityTestingScore.class,
        "Vulnerability discovery and security testing");
    add(SecurityReviewScore.class, "Security reviews");
  }

  /**
   * Add a caption for a feature class to {@link #FEATURE_CLASS_TO_NAME}.
   *
   * @param clazz The feature class.
   * @param caption The caption.
   * @throws IllegalArgumentException If a caption for the feature has already been added.
   */
  private static void add(Class<? extends Feature<?>> clazz, String caption) {
    if (FEATURE_CLASS_TO_NAME.containsKey(clazz)) {
      throw new IllegalArgumentException(String.format(
          "Oops! This feature class has already been added: %s", clazz.getSimpleName()));
    }
    FEATURE_CLASS_TO_NAME.put(clazz, caption);
  }

  /**
   * Maps a feature to its shorter name which should be used in output.
   */
  private static final Map<Feature<?>, String> FEATURE_TO_NAME = new HashMap<>();

  static {
    add(HAS_SECURITY_TEAM, "Does it have a security team?");
    add(HAS_SECURITY_POLICY, "Does it have a security policy?");
    add(USES_SIGNED_COMMITS, "Does it use verified signed commits?");
    add(VULNERABILITIES_IN_PROJECT, "Info about vulnerabilities in the project");
    add(VULNERABILITIES_IN_ARTIFACT, "Info about vulnerabilities in the artifact");
    add(IS_APACHE, "Does it belong to Apache?");
    add(IS_ECLIPSE, "Does it belong to Eclipse?");
    add(SUPPORTED_BY_COMPANY, "Is it supported by a company?");
    add(SCANS_FOR_VULNERABLE_DEPENDENCIES, "Does it scan for vulnerable dependencies?");
    add(USES_GITHUB_FOR_DEVELOPMENT, "Does it use GitHub as the main development platform?");
    add(USES_ADDRESS_SANITIZER, "Does it use AddressSanitizer?");
    add(USES_MEMORY_SANITIZER, "Does it use MemorySanitizer?");
    add(USES_UNDEFINED_BEHAVIOR_SANITIZER, "Does it use UndefinedBehaviorSanitizer?");
    add(USES_NOHTTP, "Does it use nohttp?");
    add(USES_FIND_SEC_BUGS, "Does it use FindSecBugs?");
    add(USES_OWASP_ESAPI, "Does it use OWASP ESAPI?");
    add(USES_OWASP_JAVA_ENCODER, "Does it use OWASP Java Encoder?");
    add(USES_OWASP_JAVA_HTML_SANITIZER, "Does it use OWASP Java HTML Sanitizer?");
    add(USES_DEPENDABOT, "Does it use Dependabot?");
    add(USES_SNYK, "Does it use Snyk?");
    add(USES_LGTM_CHECKS, "Does it use LGTM checks?");
    add(HAS_BUG_BOUNTY_PROGRAM, "Does it have a bug bounty program?");
    add(SIGNS_ARTIFACTS, "Does it sign artifacts?");
    add(WORST_LGTM_GRADE, "The worst LGTM grade of the project");
    add(RUNS_GOSEC_SCANS, "Does it run GoSec scans?");
    add(USES_GOSEC_WITH_RULES, "Does it run GoSec scans with rules?");
    add(USES_GOSEC_SCAN_CHECKS, "Does it run GoSec scans on all pull requests?");
    add(FUZZED_IN_OSS_FUZZ, "Is it included to OSS-Fuzz?");
    add(OWASP_DEPENDENCY_CHECK_USAGE, "How is OWASP Dependency Check used?");
    add(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD,
        "What is the threshold for OWASP Dependency Check?");
    add(USES_REUSE, "Does the project use REUSE?");
    add(README_HAS_REUSE_INFO, "Does README mention REUSE?");
    add(HAS_REUSE_LICENSES, "Does it have LICENSES directory with licenses?");
    add(REGISTERED_IN_REUSE, "Is it registered in REUSE?");
    add(IS_REUSE_COMPLIANT, "Is it compliant with REUSE rules?");
    add(HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT,
        "Does the project have open pull requests from Dependabot?");
    add(HAS_OPEN_PULL_REQUEST_FROM_SNYK,
        "Does the project have open pull requests from Snyk?");
    add(PACKAGE_MANAGERS, "Package managers");
    add(LANGUAGES, "Programming languages");
    add(RUNS_CODEQL_SCANS, "Does it run CodeQL scans?");
    add(USES_CODEQL_CHECKS, "Does it use CodeQL checks for pull requests?");
    add(HAS_LICENSE, "Does it have a license file?");
    add(ALLOWED_LICENSE, "Does it use an allowed license?");
    add(LICENSE_HAS_DISALLOWED_CONTENT, "Does the license have disallowed content?");
    add(HAS_README, "Does it have a README file?");
    add(INCOMPLETE_README, "Is README incomplete?");
    add(HAS_CONTRIBUTING_GUIDELINE, "Does it have a contributing guideline?");
    add(HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE,
        "Does the contributing guideline have required text?");
    add(HAS_CODE_OF_CONDUCT, "Does it have a code of conduct guideline?");
    add(HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE,
            "Does the code of conduct guideline have required text?");
    add(HAS_ADMIN_TEAM_ON_GITHUB, "Does it have an admin team on GitHub?");
    add(HAS_ENOUGH_ADMINS_ON_GITHUB, "Does it have enough admins on GitHub?");
    add(HAS_ENOUGH_TEAMS_ON_GITHUB, "Does it have enough teams on GitHub?");
    add(HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB, "Does teams have enough members on GitHub?");
    add(HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB,
        "Does it have a team with push privileges on GitHub?");
    add(HAS_UNRESOLVED_VULNERABILITY_ALERTS, "Does it have unresolved vulnerability alerts?");
    add(ENABLED_VULNERABILITY_ALERTS_ON_GITHUB, "Are vulnerability alerts enabled?");
    add(SECURITY_REVIEWS, "Info about security reviews");
    add(PROJECT_USAGE, "How many components use it?");
    add(FUNCTIONALITY, "What kind of functionality does it provide?");
    add(HANDLING_UNTRUSTED_DATA_LIKELIHOOD, "How likely does it handle untrusted data?");
    add(IS_ADOPTED, "Is it adopted by any team?");
    add(DATA_CONFIDENTIALITY, "What kind of data does it process?");
    add(CONFIDENTIALITY_IMPACT,
        "What is potential confidentiality impact in case of a security problem?");
    add(INTEGRITY_IMPACT, "What is potential integrity impact in case of a security problem?");
    add(AVAILABILITY_IMPACT,
        "What is potential availability impact in case of a security problem?");
    add(HAS_EXECUTABLE_BINARIES, "Does it have executable binaries?");
    add(RUNS_PYLINT_SCANS, "Does it run Pylint scans?");
    add(USES_PYLINT_SCAN_CHECKS, "Does it run Pylint scans on all commits?");
    add(RUNS_MYPY_SCANS, "Does it run MyPy scans?");
    add(USES_MYPY_SCAN_CHECKS, "Does it run MyPy scans on all commits?");
  }

  /**
   * Add a caption for a feature to {@link #FEATURE_TO_NAME}.
   *
   * @param feature The feature.
   * @param caption The caption.
   * @throws IllegalArgumentException If a caption for the feature has already been added.
   */
  private static void add(Feature<?> feature, String caption) {
    if (FEATURE_TO_NAME.containsKey(feature)) {
      throw new IllegalArgumentException(
          String.format("Oops! This feature has already been added: %s", feature.name()));
    }
    FEATURE_TO_NAME.put(feature, caption);
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
   * An advisor for calculated ratings.
   */
  protected final Advisor advisor;

  /**
   * Create a new formatter.
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
    if (Precision.equals(confidence, Confidence.MAX)) {
      return "Max";
    }
    if (Precision.equals(confidence, Confidence.MIN)) {
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
  public String nameOf(Feature<?> feature) {
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
  public String actualValueOf(Value<?> value) {
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
