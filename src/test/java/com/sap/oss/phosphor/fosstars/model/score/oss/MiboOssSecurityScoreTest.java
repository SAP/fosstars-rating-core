package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_BUG_BOUNTY_PROGRAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_ENCODER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SIGNED_COMMITS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static com.sap.oss.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.Thresholds;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.tool.format.PrettyPrinter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;
import org.junit.Test;

public class MiboOssSecurityScoreTest {

  private static final double DELTA = 0.01;

  @Test
  public void serializeAndDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    OssSecurityScore score = new OssSecurityScore();
    byte[] bytes = mapper.writeValueAsBytes(score);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    OssSecurityScore clone = mapper.readValue(bytes, OssSecurityScore.class);
    assertEquals(score, clone);
  }

  @Test
  public void calculateForAllUnknown() {
    Score score = new OssSecurityScore();
    ScoreValue scoreValue = score.calculate(Utils.allUnknown(score.allFeatures()));
    assertEquals(Score.MIN, scoreValue.get(), 0.01);
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void calculate() {
    OssSecurityScore score = new OssSecurityScore();
    Set<Value> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(false)),
        ARTIFACT_VERSION.value("1.2.0"),
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
        SIGNS_ARTIFACTS.value(true),
        VULNERABILITIES.value(new Vulnerabilities()),
        PROJECT_START_DATE.value(new Date()),
        USES_SIGNED_COMMITS.value(false),
        RUNS_CODEQL_SCANS.value(false),
        USES_CODEQL_CHECKS.value(false),
        USES_LGTM_CHECKS.value(true),
        WORST_LGTM_GRADE.value(LgtmGrade.B),
        USES_NOHTTP.value(true),
        USES_DEPENDABOT.value(true),
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
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)));

    OssSecurityRating rating = new OssSecurityRating(score, Thresholds.DEFAULT);

    RatingValue ratingValue = rating.calculate(values);
    System.out.println(PrettyPrinter.withoutVerboseOutput().print(ratingValue));
  }

  private static ArtifactVersions testArtifactVersions(boolean with2xx) {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(6));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(72));
    if (with2xx) {
      ArtifactVersion version200 = new ArtifactVersion("2.0.0", LocalDate.now().minusDays(7));
      return ArtifactVersions.of(version100, version110, version120, version200);
    }
    return ArtifactVersions.of(version100, version110, version120);
  }

  private static void checkUsedValues(ScoreValue scoreValue) {
    assertEquals(scoreValue.score().subScores().size(), scoreValue.usedValues().size());
    for (Value value : scoreValue.usedValues()) {
      boolean found = false;
      for (Score subScore : scoreValue.score().subScores()) {
        if (value.feature().getClass() == subScore.getClass()) {
          found = true;
        }
      }
      if (!found) {
        fail("Unexpected value: " + value.feature().getClass());
      }
    }
  }

}
