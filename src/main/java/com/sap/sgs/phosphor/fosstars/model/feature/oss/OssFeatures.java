package com.sap.sgs.phosphor.fosstars.model.feature.oss;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.BoundedIntegerFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.DateFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.LgtmGradeFeature;
import com.sap.sgs.phosphor.fosstars.model.feature.PositiveIntegerFeature;
import com.sap.sgs.phosphor.fosstars.model.value.FuzzingAttempts;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviews;
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
   * Shows how many commits have been integrated last three months.
   */
  public static final Feature<Integer> NUMBER_OF_COMMITS_LAST_THREE_MONTHS
      = new PositiveIntegerFeature("Number of commits last three months");

  /**
   * Shows how many people contributed to an open-source project last three months.
   */
  public static final Feature<Integer> NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS
      = new PositiveIntegerFeature("Number of contributors last three months");

  /**
   * Shows when the latest version of an open-source project was released.
   */
  public static final Feature<Date> DATE_OF_LATEST_RELEASE
      = new DateFeature("Date of latest release");

  /**
   * Shows how many projects use an open-source project.
   * For GitHub projects, the "used by" number may be used.
   */
  public static final Feature<Integer> NUMBER_OF_DEPENDENT_PROJECTS
      = new PositiveIntegerFeature("Number of other projects which use an open-source project");

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
   * Shows if an open-source project uses one of the memory-unsafe languages such as C or C++.
   */
  public static final Feature<Boolean> USES_MEMORY_UNSAFE_LANGUAGES
      = new BooleanFeature("If an open-source project uses C or C++");

  /**
   * Shows if an open-source project has been scanned in Coverity Scan project.
   * See <a href="https://scan.coverity.com/projects">https://scan.coverity.com/projects</a>.
   * The project provides additions metrics which may be also useful.
   */
  public static final Feature<Boolean> SCANNED_WITH_COVERITY
      = new BooleanFeature("If an open-source project scanned in Coverity Scan project");

  /**
   * Shows if an open-source project uses AddressSanitizer,
   * for example, for running tests, fuzzing, and so on.
   */
  public static final Feature<Boolean> USES_ADDRESS_SANITIZER
      = new BooleanFeature("If an open-source project uses AddressSanitizer (ASan)");

  /**
   * Shows if an open-source project uses UndefinedBehaviorSanitizer,
   * for example, for running tests, fuzzing, and so on.
   */
  public static final Feature<Boolean> USES_UNDEFINED_BEHAVIOR_SANITIZER
      = new BooleanFeature("If an open-source project uses UndefinedBehaviorSanitizer (UBSan)");

  /**
   * Shows if an open-source project is added to Google's open-source-Fuzz project
   * to be regularly fuzzed.
   * See <a href="https://github.com/google/oss-fuzz">https://github.com/google/oss-fuzz</a>.
   */
  public static final Feature<Boolean> FUZZED_IN_OSS_FUZZ
      = new BooleanFeature("If an open-source project is included to Google's OSS-Fuzz project");

  /**
   * Provides information about vulnerabilities.
   */
  public static final Feature<Vulnerabilities> VULNERABILITIES = new VulnerabilitiesInProject();

  /**
   * Provides information about security reviews which have been done for an open-source project.
   */
  public static final Feature<SecurityReviews> SECURITY_REVIEWS_DONE = new SecurityReviewsDone();

  /**
   * Provides information about fuzz testing which have been done for an open-source project.
   */
  public static final Feature<FuzzingAttempts> FUZZING_ATTEMPTS_DONE = new FuzzingAttemptsDone();

  public static final Feature<Boolean> SCANS_FOR_VULNERABLE_DEPENDENCIES
      = new BooleanFeature(
          "If an open-source project is regularly scanned for vulnerable dependencies");

  // Below are features specific to GitHub

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
   * Shows if the vulnerability alerts feature is enabled for an open-source project on GitHub.
   */
  public static final Feature<Boolean> ENABLED_VULNERABILITY_ALERTS_ON_GITHUB
      = new BooleanFeature("GitHub vulnerability alerts enabled");

  /**
   * Holds an overall health score for an open-source project on GitHub.
   * See <a href="https://developer.github.com/v3/repos/community">https://developer.github.com/v3/repos/community</a>.
   */
  public static final Feature<Integer> GITHUB_OVERALL_HEALTH_SCORE
      = new BoundedIntegerFeature("Overall health score for a GitHub repository", 0, 100);

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
   * It mey be used to estimate a moment when the project started.
   */
  public static final Feature<Date> FIRST_COMMIT_DATE
      = new DateFeature("When first commit was done");

  /**
   * Shows if an open-source project uses <a href="https://lgtm.com">LGTM</a> for static analysis.
   */
  public static final Feature<Boolean> USES_LGTM = new BooleanFeature("If a project uses LGTM");

  /**
   * Holds the worse grade assigned by <a href="https://lgtm.com">LGTM</a>.
   */
  public static final Feature<LgtmGrade> WORSE_LGTM_GRADE = new LgtmGradeFeature(
      "The worse LGTM grade of a project");
}
