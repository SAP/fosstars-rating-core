package com.sap.sgs.phosphor.fosstars.model.feature.oss;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.DateFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.LgtmGradeFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.OwaspDependencyCheckCvssThreshold;
import com.sap.sgs.phosphor.fosstars.model.feature.OwaspDependencyCheckUsageFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.PositiveIntegerFeature;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import java.util.Date;

/**
 * This class holds a list of features for open-source projects.
 */
public class OssFeatures {

  /**
   * Don't allow creating instances of this class.
   */
  private OssFeatures() {

  }

  /**
   * Shows how many commits have been integrated in the last three months.
   */
  public static final Feature<Integer> NUMBER_OF_COMMITS_LAST_THREE_MONTHS
      = new PositiveIntegerFeature("Number of commits in the last three months");

  /**
   * Shows how many people contributed to an open-source project in the last three months.
   */
  public static final Feature<Integer> NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS
      = new PositiveIntegerFeature("Number of contributors in the last three months");

  /**
   * Shows if an open-source project has a dedicated security team. It may be a team
   * which receives vulnerability reports from external researchers,
   * and coordinates patching and disclosures.
   */
  public static final Feature<Boolean> HAS_SECURITY_TEAM
      = new BooleanFeature("If an open-source project has a security team");

  /**
   * Shows if an open-source project has a security policy
   * which describes how a vulnerability should be reported,
   * how security problems are getting fixed, etc.
   *
   * @see <a href="https://help.github.com/en/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository">
   *   GitHub: Adding a security policy to your repository</a>
   */
  public static final Feature<Boolean> HAS_SECURITY_POLICY
      = new BooleanFeature("If an open-source project has a security policy");

  /**
   * Shows if an open-source project is maintained and supported by a company.
   */
  public static final Feature<Boolean> SUPPORTED_BY_COMPANY
      = new BooleanFeature("If an open-source project is supported by a company");

  /**
   * Shows if an open-source project belongs to Apache Software Foundation.
   */
  public static final Feature<Boolean> IS_APACHE
      = new BooleanFeature("If an open-source project belongs to Apache Foundation");

  /**
   * Shows if an open-source project belongs to Eclipse Foundation.
   */
  public static final Feature<Boolean> IS_ECLIPSE
      = new BooleanFeature("If an open-source project belongs to Eclipse Foundation");

  /**
   * Contains a set of programming languages that are used in an open-source project.
   */
  public static final Feature<Languages> LANGUAGES
      = new LanguagesFeature("A set of programming languages");

  /**
   * Contains a set of programming languages that are used in an open-source project.
   */
  public static final Feature<PackageManagers> PACKAGE_MANAGERS
      = new PackageManagersFeature("A set of package managers");

  /**
   * Shows if an open-source project uses <a href="https://find-sec-bugs.github.io/">FindSecBugs</a>.
   */
  public static final Feature<Boolean> USES_FIND_SEC_BUGS
      = new BooleanFeature("If an open-source project uses FindSecBugs");

  /**
   * Shows if an open-source project uses
   * <a href="https://github.com/google/sanitizers/wiki/AddressSanitizer">AddressSanitizer</a>.
   */
  public static final Feature<Boolean> USES_ADDRESS_SANITIZER
      = new BooleanFeature("If an open-source project uses AddressSanitizer");

  /**
   * Shows if an open-source project uses
   * <a href="https://github.com/google/sanitizers/wiki/MemorySanitizer">MemorySanitizer</a>.
   */
  public static final Feature<Boolean> USES_MEMORY_SANITIZER
      = new BooleanFeature("If an open-source project uses MemorySanitizer");

  /**
   * Shows if an open-source project uses
   * <a href="https://clang.llvm.org/docs/UndefinedBehaviorSanitizer.html">UndefinedBehaviorSanitizer</a>.
   */
  public static final Feature<Boolean> USES_UNDEFINED_BEHAVIOR_SANITIZER
      = new BooleanFeature("If an open-source project uses UndefinedBehaviorSanitizer");

  /**
   * Shows if an open-source project is added to <a href="https://github.com/google/oss-fuzz">OSS-Fuzz</a> project
   * to be regularly fuzzed.
   */
  public static final Feature<Boolean> FUZZED_IN_OSS_FUZZ
      = new BooleanFeature("If an open-source project is included to OSS-Fuzz project");

  /**
   * Provides information about vulnerabilities.
   */
  public static final Feature<Vulnerabilities> VULNERABILITIES = new VulnerabilitiesInProject(
      "Info about vulnerabilities in open-source project");

  /**
   * Shows if an open-source project scans for known vulnerabilities in its dependencies.
   */
  public static final Feature<Boolean> SCANS_FOR_VULNERABLE_DEPENDENCIES
      = new BooleanFeature(
          "If an open-source project is regularly scanned for vulnerable dependencies");

  /**
   * <p>Shows if a project uses Dependabot.</p>
   * <p><a href="https://dependabot.com/">Dependabot</a> offers automatic dependency updates.
   * In particular, when Dependabot finds a vulnerability in dependencies,
   * it opens a pull request to update the vulnerable dependency to the safe version.</p>
   */
  public static final Feature<Boolean> USES_DEPENDABOT
      = new BooleanFeature("If a project uses Dependabot");

  /**
   * Shows how many GitHub users starred an open-source project.
   */
  public static final Feature<Integer> NUMBER_OF_GITHUB_STARS
      = new PositiveIntegerFeature("Number of stars for a GitHub repository");

  /**
   * Shows how many GitHub users watch an open-source project.
   * See https://developer.github.com/v3/activity/watching/
   */
  public static final Feature<Integer> NUMBER_OF_WATCHERS_ON_GITHUB
      = new PositiveIntegerFeature("Number of watchers for a GitHub repository");

  /**
   * Show how many collaborators an open-source project has on GitHub.
   * See <a href="https://developer.github.com/v3/repos/collaborators">https://developer.github.com/v3/repos/collaborators/</a>.
   */
  public static final Feature<Integer> NUMBER_OF_COLLABORATORS
      = new PositiveIntegerFeature("Number of collaborators for a GitHub repository");

  /**
   * Holds a date when an open-source project started.
   */
  public static final Feature<Date> PROJECT_START_DATE
      = new DateFeature("When a project started");

  /**
   * Holds a date of the first commit to the source repository.
   * It may be used to estimate a moment when the project started.
   */
  public static final Feature<Date> FIRST_COMMIT_DATE
      = new DateFeature("When first commit was done");

  /**
   * Shows if an open-source project uses <a href="https://lgtm.com">LGTM</a> checks for commits.
   */
  public static final Feature<Boolean> USES_LGTM_CHECKS
      = new BooleanFeature("If a project uses LGTM checks for commits");

  /**
   * Holds the worst grade assigned by <a href="https://lgtm.com">LGTM</a>.
   */
  public static final Feature<LgtmGrade> WORST_LGTM_GRADE = new LgtmGradeFeature(
      "The worst LGTM grade of a project");

  /**
   * Shows if an open-source project uses signed commits.
   */
  public static final Feature<Boolean> USES_SIGNED_COMMITS =
      new BooleanFeature("If a project uses signed commits");
  
  /**
   * Shows if an open-source project uses GitHub as a main development platform. More precisely, it
   * means that a repository on GitHub is the main one (not a mirror).
   */
  public static final Feature<Boolean> USES_GITHUB_FOR_DEVELOPMENT =
      new BooleanFeature("If a project uses GitHub as the main development platform");

  /**
   * Shows if an open-source project uses <a href="https://github.com/spring-io/nohttp">nohttp</a>
   * tool to make sure that plain HTTP is not used.
   */
  public static final Feature<Boolean> USES_NOHTTP =
      new BooleanFeature("If a project uses nohttp tool");

  /**
   * Shows if an open-source project has a bug bounty program.
   */
  public static final Feature<Boolean> HAS_BUG_BOUNTY_PROGRAM
      = new BooleanFeature("If a project has a bug bounty program");

  /**
   * Shows if an open-source project signs its artifacts (for example, jar files).
   */
  public static final Feature<Boolean> SIGNS_ARTIFACTS
      = new BooleanFeature("If a project signs artifacts");

  /**
   * Shows if OWASP Dependency Check is used to scan a project. It is either used as a mandatory
   * step, optional step or not used at all.
   */
  public static final OwaspDependencyCheckUsageFeature OWASP_DEPENDENCY_CHECK_USAGE =
      new OwaspDependencyCheckUsageFeature();

  /**
   * Shows if OWASP Dependency Check is configured to fail the build if vulnerabilities are found.
   * The feature contains a threshold for CVSS score that is used by the tool.
   *
   * @see <a href="https://jeremylong.github.io/DependencyCheck/">OWASP Dependency Check
   *      documentation</a>
   */
  public static final OwaspDependencyCheckCvssThreshold OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD =
      new OwaspDependencyCheckCvssThreshold();
}