package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
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
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static com.sap.oss.phosphor.fosstars.model.value.SecurityReviews.noReviews;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Date;
import java.util.Set;
import org.junit.Test;

public class OssSecurityScoreTest {

  @Test
  public void testSerializeAndDeserialize() throws IOException {
    OssSecurityScore score = new OssSecurityScore();
    byte[] bytes = Json.toBytes(score);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    OssSecurityScore clone = Json.read(bytes, OssSecurityScore.class);
    assertEquals(score, clone);
  }

  @Test
  public void testCalculateForAllUnknown() {
    Score score = new OssSecurityScore();
    ScoreValue scoreValue = score.calculate(Utils.allUnknown(score.allFeatures()));
    assertTrue(scoreValue.isUnknown());
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculate() {
    Score score = new OssSecurityScore();
    ScoreValue scoreValue = score.calculate(defaultValues());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  public static Set<Value<?>> defaultValues() {
    return setOf(
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5),
        NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(20),
        HAS_SECURITY_TEAM.value(false),
        HAS_SECURITY_POLICY.value(false),
        HAS_BUG_BOUNTY_PROGRAM.value(false),
        SIGNS_ARTIFACTS.value(true),
        VULNERABILITIES_IN_PROJECT.value(new Vulnerabilities()),
        PROJECT_START_DATE.value(new Date()),
        USES_SIGNED_COMMITS.value(false),
        RUNS_CODEQL_SCANS.value(false),
        USES_CODEQL_CHECKS.value(false),
        RUNS_BANDIT_SCANS.value(false),
        USES_BANDIT_SCAN_CHECKS.value(false),
        RUNS_GOSEC_SCANS.value(false),
        USES_GOSEC_WITH_RULES.value(false),
        USES_GOSEC_SCAN_CHECKS.value(false),
        RUNS_PYLINT_SCANS.value(false),
        USES_PYLINT_SCAN_CHECKS.value(false),
        RUNS_MYPY_SCANS.value(false),
        USES_MYPY_SCAN_CHECKS.value(false),
        USES_LGTM_CHECKS.value(true),
        WORST_LGTM_GRADE.value(LgtmGrade.B),
        USES_NOHTTP.value(true),
        USES_DEPENDABOT.value(true),
        USES_SNYK.value(false),
        USES_GITHUB_FOR_DEVELOPMENT.value(true),
        LANGUAGES.value(Languages.of(JAVA)),
        USES_ADDRESS_SANITIZER.value(false),
        USES_MEMORY_SANITIZER.value(false),
        USES_UNDEFINED_BEHAVIOR_SANITIZER.value(false),
        USES_OWASP_ESAPI.value(false),
        USES_OWASP_JAVA_ENCODER.value(false),
        USES_OWASP_JAVA_HTML_SANITIZER.value(false),
        FUZZED_IN_OSS_FUZZ.value(false),
        USES_FIND_SEC_BUGS.value(true),
        OWASP_DEPENDENCY_CHECK_USAGE.value(MANDATORY),
        OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD.value(7.0),
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)),
        SECURITY_REVIEWS.value(noReviews()),
        HAS_EXECUTABLE_BINARIES.value(false));
  }

  private static void checkUsedValues(ScoreValue scoreValue) {
    assertEquals(scoreValue.score().subScores().size(), scoreValue.usedValues().size());
    for (Value<?> value : scoreValue.usedValues()) {
      boolean found = false;
      for (Score subScore : scoreValue.score().subScores()) {
        if (value.feature().getClass() == subScore.getClass()) {
          found = true;
          break;
        }
      }
      if (!found) {
        fail("Unexpected value: " + value.feature().getClass());
      }
    }
  }

}