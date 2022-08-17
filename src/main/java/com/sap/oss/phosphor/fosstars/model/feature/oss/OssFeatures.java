package com.sap.oss.phosphor.fosstars.model.feature.oss;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.feature.DateFeature;
import com.sap.oss.phosphor.fosstars.model.feature.LgtmGradeFeature;
import com.sap.oss.phosphor.fosstars.model.feature.OwaspDependencyCheckCvssThreshold;
import com.sap.oss.phosphor.fosstars.model.feature.OwaspDependencyCheckUsageFeature;
import com.sap.oss.phosphor.fosstars.model.feature.PositiveIntegerFeature;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
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
   * Holds a list of artifact versions released by an open source project.
   */
  public static final Feature<ArtifactVersions> RELEASED_ARTIFACT_VERSIONS
      = new ArtifactVersionsFeature("Released artifact versions");

  /**
   * Holds a artifact version.
   */
  public static final Feature<ArtifactVersion> ARTIFACT_VERSION
      = new ArtifactVersionFeature("Artifact version");

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
   * Shows if an open-source project is added to
   * <a href="https://github.com/google/oss-fuzz">OSS-Fuzz</a> project to be regularly fuzzed.
   */
  public static final Feature<Boolean> FUZZED_IN_OSS_FUZZ
      = new BooleanFeature("If an open-source project is included to OSS-Fuzz project");

  /**
   * Provides information about vulnerabilities in project.
   */
  public static final Feature<Vulnerabilities> VULNERABILITIES_IN_PROJECT
      = new VulnerabilitiesFeature("Info about vulnerabilities in open-source project");

  /**
   * Provides information about vulnerabilities in artifact.
   */
  public static final Feature<Vulnerabilities> VULNERABILITIES_IN_ARTIFACT
      = new VulnerabilitiesFeature("Info about vulnerabilities in open-source artifact");

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
   * Shows if an open source project has open pull requests from Dependabot which means that
   * there are dependencies with known vulnerabilities.
   *
   * @see <a href="https://dependabot.com/">Dependabot</a>
   */
  public static final BooleanFeature HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT
      = new BooleanFeature("If a project has open pull requests from Dependabot");

  /**
   * <p>Shows if a project uses Snyk.</p>
   * <p><a href="https://snyk.io/">Snyk introduction</a> offers</p>
   * <ul>
   *   <li>Static Application Security Testing (SAST)</li>
   *   <li>Automatic dependency updates</li>
   * </ul>
   *<p> In particular for automatic dependency updates,
   * when Snyk finds a vulnerability in dependencies,
   * it opens a pull request to update the vulnerable dependency to the safe version.</p>
   */
  public static final Feature<Boolean> USES_SNYK
      = new BooleanFeature("If a project uses Snyk");

  /**
   * Shows if an open source project has open pull requests from Snyk which means that
   * there are dependencies with known vulnerabilities.
   *
   * @see <a href="https://snyk.io/">Snyk</a>
   */
  public static final BooleanFeature HAS_OPEN_PULL_REQUEST_FROM_SNYK
      = new BooleanFeature("If a project has open pull requests from Snyk");

  /**
   * Shows how many GitHub users starred an open-source project.
   */
  public static final Feature<Integer> NUMBER_OF_GITHUB_STARS
      = new PositiveIntegerFeature("Number of stars for a GitHub repository");

  /**
   * Shows how many GitHub users watch an open-source project.
   *
   * @see <a href="https://developer.github.com/v3/activity/watching/">Watching</a>
   */
  public static final Feature<Integer> NUMBER_OF_WATCHERS_ON_GITHUB
      = new PositiveIntegerFeature("Number of watchers for a GitHub repository");

  /**
   * Show how many collaborators an open-source project has on GitHub.
   *
   * @see <a href="https://developer.github.com/v3/repos/collaborators">Collaborators</a>
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
   * Shows if an open-source project runs CodeQL scans.
   *
   * @see <a href="https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository">Enabling code scanning for a repository</a>
   */
  public static final Feature<Boolean> RUNS_CODEQL_SCANS
      = new BooleanFeature("If a project runs CodeQL scans");

  /**
   * Shows if an open-source project runs CodeQL checks for commits.
   *
   * @see <a href="https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository">Enabling code scanning for a repository</a>
   */
  public static final Feature<Boolean> USES_CODEQL_CHECKS
      = new BooleanFeature("If a project runs CodeQL checks for commits");

  /**
   * Shows if an open-source project runs Bandit scans.
   *
   * @see <a href="https://github.com/PyCQA/bandit#usage">Trigger Bandit code scanning for a repository</a>
   */
  public static final Feature<Boolean> RUNS_BANDIT_SCANS
      = new BooleanFeature("If a project runs Bandit scans");

  /**
   * Shows if an open-source project runs Bandit checks for commits.
   *
   * @see <a href="https://github.com/PyCQA/bandit#usage">Trigger Bandit code scanning job on every PR of a project</a>
   */
  public static final Feature<Boolean> USES_BANDIT_SCAN_CHECKS
      = new BooleanFeature("If a project runs Bandit scan checks for commits");

  /**
   * Shows if an open-source project runs GoSec scans.
   *
   * @see <a href="https://github.com/securego/gosec#gosec---golang-security-checker">Trigger GoSec code scanning for a repository</a>
   */
  public static final Feature<Boolean> RUNS_GOSEC_SCANS
      = new BooleanFeature("If a project runs GoSec scans");

  /**
   * Shows if an open-source project runs GoSec checks for commits.
   *
   * @see <a href="https://github.com/securego/gosec#usage">Trigger GoSec code scanning job on every PR of a project</a>
   */
  public static final Feature<Boolean> USES_GOSEC_SCAN_CHECKS
      = new BooleanFeature("If a project runs GoSec scan checks for commits");

  /**
   * Shows if an open-source project runs GoSec scans with the selected rules.
   *
   * @see <a href="https://github.com/securego/gosec#usage">Trigger GoSec code scanning for a repository with rules</a>
   */
  public static final Feature<Boolean> USES_GOSEC_WITH_RULES
      = new BooleanFeature("If a project runs GoSec scans with rules");

  /**
   * Shows if an open-source project runs Pylint scans.
   *
   * @see <a href="https://pylint.pycqa.org/en/latest/user_guide/installation/index.html">Trigger
   *      Pylint code scanning for a repository</a>
   */
  public static final Feature<Boolean> RUNS_PYLINT_SCANS =
      new BooleanFeature("If a project runs Pylint scans");

  /**
   * Shows if an open-source project runs Pylint checks before commits.
   *
   * @see <a href=
   *      "https://pylint.pycqa.org/en/latest/user_guide/installation/pre-commit-integration.html">Trigger
   *      Pylint code scanning job before every commit to a repository</a>
   */
  public static final Feature<Boolean> USES_PYLINT_SCAN_CHECKS =
      new BooleanFeature("If a project runs Pylint scan checks for commits");
  
  /**
   * Shows if an open-source project runs Mypy scans.
   *
   * @see <a href="https://github.com/marketplace/actions/mypy-action">Trigger Mypy code scanning
   *      for a repository</a>
   */
  public static final Feature<Boolean> RUNS_MYPY_SCANS =
      new BooleanFeature("If a project runs Mypy scans");

  /**
   * Shows if an open-source project runs Mypy checks before commits.
   *
   * @see <a href="https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository">Enabling
   *      code scanning for a repository</a>
   */
  public static final Feature<Boolean> USES_MYPY_SCAN_CHECKS =
      new BooleanFeature("If a project runs Mypy scan checks for commits");

  /**
   * Shows if an open-source project uses <a href="https://lgtm.com">LGTM</a> checks for commits.
   */
  public static final Feature<Boolean> USES_LGTM_CHECKS
      = new BooleanFeature("If a project uses LGTM checks for commits");

  /**
   * Holds the worst grade assigned by <a href="https://lgtm.com">LGTM</a>.
   */
  public static final Feature<LgtmGrade> WORST_LGTM_GRADE
      = new LgtmGradeFeature("The worst LGTM grade of a project");

  /**
   * Shows if an open-source project uses signed commits.
   */
  public static final Feature<Boolean> USES_SIGNED_COMMITS
      = new BooleanFeature("If a project uses signed commits");
  
  /**
   * Shows if an open-source project uses GitHub as a main development platform. More precisely, it
   * means that a repository on GitHub is the main one (not a mirror).
   */
  public static final Feature<Boolean> USES_GITHUB_FOR_DEVELOPMENT
      = new BooleanFeature("If a project uses GitHub as the main development platform");

  /**
   * Shows if an open-source project uses <a href="https://github.com/spring-io/nohttp">nohttp</a>
   * tool to make sure that plain HTTP is not used.
   */
  public static final Feature<Boolean> USES_NOHTTP
      = new BooleanFeature("If a project uses nohttp tool");

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
   * Shows if an open-source project has executable binaries (for example, .class, .pyc, .exe).
   */
  public static final Feature<Boolean> HAS_EXECUTABLE_BINARIES
      = new BooleanFeature("If a project has executable binaries");

  /**
   * Shows if OWASP Dependency Check is used to scan a project. It is either used as a mandatory
   * step, optional step or not used at all.
   */
  public static final OwaspDependencyCheckUsageFeature OWASP_DEPENDENCY_CHECK_USAGE
      = new OwaspDependencyCheckUsageFeature();

  /**
   * Shows if OWASP Dependency Check is configured to fail the build if vulnerabilities are found.
   * The feature contains a threshold for CVSS score that is used by the tool.
   *
   * @see <a href="https://jeremylong.github.io/DependencyCheck/">
   *   OWASP Dependency Check documentation</a>
   */
  public static final OwaspDependencyCheckCvssThreshold OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD
      = new OwaspDependencyCheckCvssThreshold();

  /**
   * Shows if an open-source project uses OWASP Enterprise Security API (ESAPI).
   *
   * @see <a href="https://owasp.org/www-project-enterprise-security-api/">
   *   OWASP Enterprise Security API (ESAPI)</a>
   */
  public static final BooleanFeature USES_OWASP_ESAPI
      = new BooleanFeature("If a project uses OWASP Enterprise Security API (ESAPI)");

  /**
   * Shows if an open-source project uses OWASP Java HTML Sanitizer.
   *
   * @see <a href="https://github.com/OWASP/java-html-sanitizer">OWASP Java HTML Sanitizer</a>
   */
  public static final BooleanFeature USES_OWASP_JAVA_HTML_SANITIZER
      = new BooleanFeature("If a project uses OWASP Java HTML Sanitizer");

  /**
   * Shows if an open-source project uses OWASP Java Encoder.
   *
   * @see <a href="https://github.com/OWASP/owasp-java-encoder">OWASP Java Encoder</a>
   */
  public static final BooleanFeature USES_OWASP_JAVA_ENCODER
      = new BooleanFeature("If a project uses OWASP Java Encoder");

  /**
   * Shows if an open source project uses REUSE tool.
   *
   * @see <a href="https://REUSE.software/">REUSE Software</a>
   */
  public static final BooleanFeature USES_REUSE
      = new BooleanFeature("If a project uses REUSE tool");

  /**
   * Shows whether a project has info about REUSE in its README file or not.
   */
  public static final BooleanFeature README_HAS_REUSE_INFO
      = new BooleanFeature("If project's README has info about REUSE");

  /**
   * Shows whether a project has a LICENSES folder with licenses or not.
   */
  public static final BooleanFeature HAS_REUSE_LICENSES
      = new BooleanFeature("If a project has a LICENSES folder with licenses");

  /**
   * Shows whether a project is registered in REUSE or not.
   *
   * @see <a href="https://api.REUSE.software/register">Register a project in REUSE</a>
   */
  public static final BooleanFeature REGISTERED_IN_REUSE
      = new BooleanFeature("If a project is registered in REUSE");

  /**
   * Shows whether a project is compliant with REUSE rules or not.
   */
  public static final BooleanFeature IS_REUSE_COMPLIANT
      = new BooleanFeature("If a project is compliant with REUSE rules");

  /**
   * Shows whether an open source project has a licence or not.
   */
  public static final BooleanFeature HAS_LICENSE
      = new BooleanFeature("If a project has a license");
  /**
   * Shows whether an open source project has an allowed licence or not.
   */
  public static final BooleanFeature ALLOWED_LICENSE
      = new BooleanFeature("If a project uses an allowed license");

  /**
   * Shows whether a licence has disallowed text or not.
   */
  public static final BooleanFeature LICENSE_HAS_DISALLOWED_CONTENT
      = new BooleanFeature("If a license has disallowed text");

  /**
   * Shows whether an open source project has a README file or not.
   */
  public static final BooleanFeature HAS_README
      = new BooleanFeature("If a project has a README file");

  /**
   * Shows whether a project's README contains required information or not.
   */
  public static final BooleanFeature INCOMPLETE_README
      = new BooleanFeature("If a project's README doesn't contain required info");

  /** Shows whether an open source project has a Code of conduct file or not.
   */
  public static final BooleanFeature HAS_CODE_OF_CONDUCT
      = new BooleanFeature("If a project has a code of conduct file");

  /**
   * Shows whether a project's code of conduct guideline has required text or not.
   */
  public static final BooleanFeature HAS_REQUIRED_TEXT_IN_CODE_OF_CONDUCT_GUIDELINE
        = new BooleanFeature("If a project's code of conduct guideline has required text");

  /**
   * Shows whether an open source project has a contributing guideline or not.
   */
  public static final BooleanFeature HAS_CONTRIBUTING_GUIDELINE
      = new BooleanFeature("If a project has a contributing guideline");

  /**
   * Shows whether a project's contributing guideline has required text or not.
   */
  public static final BooleanFeature HAS_REQUIRED_TEXT_IN_CONTRIBUTING_GUIDELINE
      = new BooleanFeature("If a project's contributing guideline has required text");

  /**
   * Shows whether a project has enough teams on GitHub or not.
   */
  public static final BooleanFeature HAS_ENOUGH_TEAMS_ON_GITHUB
      = new BooleanFeature("If a project has enough teams on GitHub");

  /**
   * Shows whether a project has an admin team on GitHub or not.
   */
  public static final BooleanFeature HAS_ADMIN_TEAM_ON_GITHUB
      = new BooleanFeature("If a project has an admin team on GitHub");

  /**
   * Shows whether a project has enough admins on GitHub or not.
   */
  public static final BooleanFeature HAS_ENOUGH_ADMINS_ON_GITHUB
      = new BooleanFeature("If a project has enough admins on GitHub");

  /**
   * Shows whether a project has a team with push privileges on GitHub.
   */
  public static final BooleanFeature HAS_TEAM_WITH_PUSH_PRIVILEGES_ON_GITHUB
      = new BooleanFeature("If a project has a team with push privileges");

  /**
   * Shows whether a project has enough team members on GitHub.
   */
  public static final BooleanFeature HAS_ENOUGH_TEAM_MEMBERS_ON_GITHUB
      = new BooleanFeature("If a project has enough team members on GitHub");

  /**
   * Shows whether vulnerability alerts are enabled for a project on GitHub.
   *
   * @see <a href="https://docs.github.com/en/github/managing-security-vulnerabilities/about-alerts-for-vulnerable-dependencies">About alerts for vulnerable dependencies</a>
   */
  public static final BooleanFeature ENABLED_VULNERABILITY_ALERTS_ON_GITHUB
      = new BooleanFeature("If vulnerability alerts are enabled for a project on GitHub");

  /**
   * Shows whether a project has unresolved vulnerability alerts or not.
   */
  public static final BooleanFeature HAS_UNRESOLVED_VULNERABILITY_ALERTS
      = new BooleanFeature("If a project has unresolved vulnerability alerts");

  /**
   * Contains a set of security reviews that have been done for an open source project.
   */
  public static final SecurityReviewsFeature SECURITY_REVIEWS
      = new SecurityReviewsFeature("Security reviews for a project");

  /**
   * Shows how many projects use a project on GitHub.
   */
  public static final PositiveIntegerFeature NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB
      = new PositiveIntegerFeature("Number of projects on GitHub that use an open source project");
}
