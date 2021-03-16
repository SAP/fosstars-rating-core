package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.getDefaultValues;
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
import com.sap.oss.phosphor.fosstars.TestUtils;
import com.sap.oss.phosphor.fosstars.advice.Advisor;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssArtifactSecurityRating.Thresholds;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.CVSS;
import com.sap.oss.phosphor.fosstars.model.value.CVSS.Version;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.Reference;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.VersionRange;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.oss.phosphor.fosstars.tool.format.PrettyPrinter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Ignore;
import org.junit.Test;

public class OssArtifactSecurityScoreTest {

  private static final double DELTA = 0.2;
  private static final double CONFIDENCE_WO_CVE = 9.411764705882351;

  @Test
  @Ignore
  public void serializeAndDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    byte[] bytes = mapper.writeValueAsBytes(score);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    OssArtifactSecurityScore clone = mapper.readValue(bytes, OssArtifactSecurityScore.class);
    assertEquals(score, clone);
  }

  @Test
  @Ignore
  public void calculateForAllUnknown() {
    Score score = new OssArtifactSecurityScore();
    ScoreValue scoreValue = score.calculate(Utils.allUnknown(score.allFeatures()));
    assertEquals(Score.MIN, scoreValue.get(), DELTA);
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void calculate() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(false)),
        ARTIFACT_VERSION.value("1.2.0"));

    ScoreValue scoreValue = score.calculate(values);
    assertEquals(5.946669683257918, scoreValue.get(), DELTA);
    assertEquals(CONFIDENCE_WO_CVE, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void calculateWithOldVersion() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(false)),
        ARTIFACT_VERSION.value("1.0.0"));

    ScoreValue scoreValue = score.calculate(values);
    assertEquals(5.567095022624433, scoreValue.get(), DELTA);
    assertEquals(CONFIDENCE_WO_CVE, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void calculateWith20() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value("1.2.0"));

    ScoreValue scoreValue = score.calculate(values);
    assertEquals(6.125913273001508, scoreValue.get(), DELTA);
    assertEquals(CONFIDENCE_WO_CVE, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void calculateWith20Used() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value("2.0.0"));

    ScoreValue scoreValue = score.calculate(values);
    assertEquals(6.252438159879335, scoreValue.get(), DELTA);
    assertEquals(CONFIDENCE_WO_CVE, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void calculateWith20UsedAndOldVulnerability() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Vulnerability vulnerability = createBasicVulnerability();

    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        VULNERABILITIES.value(new Vulnerabilities(vulnerability)),
        ARTIFACT_VERSION.value("2.0.0"));

    ScoreValue scoreValue = score.calculate(values);
    assertEquals(7.095937405731523, scoreValue.get(), DELTA);
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void calculateWith20UsedAndVulnerability() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Vulnerability vulnerability = TestUtils.createBasicVulnerability(10.0, "2.0.0", "2.0.0");

    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        VULNERABILITIES.value(new Vulnerabilities(vulnerability)),
        ARTIFACT_VERSION.value("2.0.0"));

    ScoreValue scoreValue = score.calculate(values);
    assertEquals(0.716974358974359, scoreValue.get(), DELTA);
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  private Vulnerability createBasicVulnerability() {
    return TestUtils.createBasicVulnerability(9.0, "1.0.0", "1.2.0");
  }

  private static ArtifactVersions testArtifactVersions(boolean with2xx) {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version101 =
        new ArtifactVersion("1.0.1", LocalDate.now().minusMonths(13));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(6));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(72));
    if (with2xx) {
      ArtifactVersion version200 = new ArtifactVersion("2.0.0", LocalDate.now().minusDays(7));
      return ArtifactVersions.of(version100, version101, version110, version120, version200);
    }
    return ArtifactVersions.of(version100, version101, version110, version120);
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
