package com.sap.sgs.phosphor.fosstars.tool.format;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FIRST_COMMIT_DATE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_BUG_BOUNTY_PROGRAM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_ENCODER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SIGNED_COMMITS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.C;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.NOT_USED;
import static com.sap.sgs.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.advice.oss.OssSecurityAdvisor;
import com.sap.sgs.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.PackageManagers;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import java.util.Date;
import java.util.Set;
import org.junit.Test;

public class MarkdownFormatterTest {

  private static final OssSecurityRating RATING
      = RatingRepository.INSTANCE.rating(OssSecurityRating.class);

  private static final Set<Value> TEST_VALUES = setOf(
      SUPPORTED_BY_COMPANY.value(false),
      IS_APACHE.value(true),
      IS_ECLIPSE.value(false),
      NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
      NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
      NUMBER_OF_GITHUB_STARS.value(10),
      NUMBER_OF_WATCHERS_ON_GITHUB.value(5),
      HAS_SECURITY_TEAM.value(false),
      HAS_SECURITY_POLICY.value(false),
      HAS_BUG_BOUNTY_PROGRAM.value(false),
      SIGNS_ARTIFACTS.value(false),
      VULNERABILITIES.value(new Vulnerabilities()),
      PROJECT_START_DATE.value(new Date()),
      FIRST_COMMIT_DATE.value(new Date()),
      USES_SIGNED_COMMITS.value(false),
      USES_CODEQL_CHECKS.value(false),
      RUNS_CODEQL_SCANS.value(false),
      USES_LGTM_CHECKS.value(true),
      WORST_LGTM_GRADE.value(LgtmGrade.A),
      USES_GITHUB_FOR_DEVELOPMENT.value(false),
      USES_NOHTTP.value(false),
      USES_DEPENDABOT.value(false),
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
      PACKAGE_MANAGERS.value(new PackageManagers(MAVEN)));

  @Test
  public void testPrint() {
    RatingValue ratingValue = RATING.calculate(TEST_VALUES);
    GitHubProject project = new GitHubProject("org", "test");
    project.set(ratingValue);

    MarkdownFormatter formatter = new MarkdownFormatter(new OssSecurityAdvisor());
    String text = formatter.print(project);

    assertNotNull(text);
    assertFalse(text.isEmpty());
    System.out.println(text);
  }
}