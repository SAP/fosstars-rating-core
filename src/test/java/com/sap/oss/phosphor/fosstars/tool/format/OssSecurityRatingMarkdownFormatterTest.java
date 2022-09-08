package com.sap.oss.phosphor.fosstars.tool.format;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FIRST_COMMIT_DATE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_BUG_BOUNTY_PROGRAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_EXECUTABLE_BINARIES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_BANDIT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_GOSEC_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_MYPY_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_PYLINT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_BANDIT_SCAN_CHECKS;
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
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SIGNED_COMMITS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SNYK;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_PROJECT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.C;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.NOT_USED;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.sap.oss.phosphor.fosstars.advice.oss.github.OssSecurityGithubAdvisor;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReview;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviews;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;

public class OssSecurityRatingMarkdownFormatterTest {

  private static final GitHubProject PROJECT = new GitHubProject("org", "test");

  private static final OssSecurityRating RATING
      = RatingRepository.INSTANCE.rating(OssSecurityRating.class);

  private static final Set<Value<?>> TEST_VALUES = setOf(
      SUPPORTED_BY_COMPANY.value(false),
      IS_APACHE.value(true),
      IS_ECLIPSE.value(false),
      NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
      NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
      NUMBER_OF_GITHUB_STARS.value(10),
      NUMBER_OF_WATCHERS_ON_GITHUB.value(5),
      NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(50),
      HAS_SECURITY_TEAM.value(false),
      HAS_SECURITY_POLICY.value(false),
      HAS_BUG_BOUNTY_PROGRAM.value(false),
      SIGNS_ARTIFACTS.value(false),
      VULNERABILITIES_IN_PROJECT.value(new Vulnerabilities()),
      PROJECT_START_DATE.value(new Date()),
      FIRST_COMMIT_DATE.value(new Date()),
      USES_SIGNED_COMMITS.value(false),
      USES_CODEQL_CHECKS.value(false),
      RUNS_CODEQL_SCANS.value(false),
      USES_BANDIT_SCAN_CHECKS.value(false),
      RUNS_BANDIT_SCANS.value(false),
      RUNS_GOSEC_SCANS.value(false),
      USES_GOSEC_WITH_RULES.value(false),
      USES_GOSEC_SCAN_CHECKS.value(false),
      RUNS_MYPY_SCANS.value(false),
      USES_MYPY_SCAN_CHECKS.value(false),
      RUNS_PYLINT_SCANS.value(false),
      USES_PYLINT_SCAN_CHECKS.value(false),
      USES_LGTM_CHECKS.value(true),
      WORST_LGTM_GRADE.value(LgtmGrade.A),
      USES_GITHUB_FOR_DEVELOPMENT.value(false),
      USES_NOHTTP.value(false),
      USES_DEPENDABOT.value(false),
      USES_SNYK.value(false),
      USES_ADDRESS_SANITIZER.value(false),
      USES_MEMORY_SANITIZER.value(false),
      USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false),
      FUZZED_IN_OSS_FUZZ.value(false),
      LANGUAGES.value(Languages.of(C)),
      USES_FIND_SEC_BUGS.value(false),
      USES_OWASP_ESAPI.value(false),
      USES_OWASP_JAVA_ENCODER.value(false),
      USES_OWASP_JAVA_HTML_SANITIZER.value(false),
      OWASP_DEPENDENCY_CHECK_USAGE.value(NOT_USED),
      OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.notSpecifiedValue(),
      PACKAGE_MANAGERS.value(new PackageManagers(MAVEN)),
      SECURITY_REVIEWS.value(new SecurityReviews(new SecurityReview(new Date(), 0.0))),
      HAS_EXECUTABLE_BINARIES.value(false)
  );

  @Test
  public void testWithDefaultTemplate() {
    RatingValue ratingValue = RATING.calculate(TEST_VALUES);
    PROJECT.set(ratingValue);

    OssSecurityRatingMarkdownFormatter formatter
        = new OssSecurityRatingMarkdownFormatter(new OssSecurityGithubAdvisor());
    String text = formatter.print(PROJECT);

    assertNotNull(text);
    assertFalse(text.isEmpty());
  }

  @Test
  public void testWithCustomTemplate() {
    
    // Needed to ensure that decimal points are exactly as expected below...
    Locale.setDefault(new Locale("en", "US"));
    
    RatingValue ratingValue = RATING.calculate(TEST_VALUES);
    GitHubProject project = new GitHubProject("org", "test");
    project.set(ratingValue);

    String template = "%RATING_LABEL%|%SCORE_VALUE%|%MAX_SCORE%|%CONFIDENCE_LABEL%"
        + "|%CONFIDENCE_VALUE%|%MAX_CONFIDENCE%|%MAIN_SCORE_NAME%"
        + "|%MAIN_SCORE_DESCRIPTION%|%MAIN_SCORE_EXPLANATION%";

    OssSecurityRatingMarkdownFormatter formatter
        = new OssSecurityRatingMarkdownFormatter(new OssSecurityGithubAdvisor(), template);
    String text = formatter.print(project);

    assertNotNull(text);
    assertEquals("BAD|3.99|10.0|Max|10.0|10.0|security score for open-source projects||", text);
  }
}