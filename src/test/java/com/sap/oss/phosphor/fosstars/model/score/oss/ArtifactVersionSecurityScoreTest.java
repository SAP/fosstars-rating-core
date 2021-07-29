package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_ARTIFACT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.PackageManager.MAVEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.TestUtils;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.Test;

public class ArtifactVersionSecurityScoreTest {

  private static final double DELTA = 0.01;
  private static final double CONFIDENCE_NO_VULNERABILITIES = 8.823529411764705;

  @Test
  public void testSerializeAndDeserialize() throws IOException {
    ObjectMapper mapper = Json.mapper();
    ArtifactVersionSecurityScore score = new ArtifactVersionSecurityScore();
    byte[] bytes = mapper.writeValueAsBytes(score);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    ArtifactVersionSecurityScore clone =
        mapper.readValue(bytes, ArtifactVersionSecurityScore.class);
    assertEquals(score, clone);
  }

  @Test
  public void testCalculateForAllUnknown() {
    Score score = new ArtifactVersionSecurityScore();
    ScoreValue scoreValue = score.calculate(Utils.allUnknown(score.allFeatures()));
    assertTrue(scoreValue.isUnknown());
  }

  @Test
  public void testCalculate() {
    ArtifactVersionSecurityScore score = new ArtifactVersionSecurityScore();
    Set<Value<?>> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(false)),
        ARTIFACT_VERSION.value(new ArtifactVersion("1.2.0", LocalDateTime.now())),
        SUPPORTED_BY_COMPANY.value(false),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities()),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(7, 7.50).contains(scoreValue.get()));
    assertEquals(CONFIDENCE_NO_VULNERABILITIES, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }


  @Test
  public void testCalculateWithOldVersion() {
    ArtifactVersionSecurityScore score = new ArtifactVersionSecurityScore();
    Set<Value<?>> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(false)),
        ARTIFACT_VERSION.value(new ArtifactVersion("1.0.0", LocalDateTime.now())),
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities()),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5),
        PACKAGE_MANAGERS.value(PackageManagers.from(MAVEN)));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(6, 7).contains(scoreValue.get()));
    assertEquals(CONFIDENCE_NO_VULNERABILITIES, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculateWith20() {
    ArtifactVersionSecurityScore score = new ArtifactVersionSecurityScore();
    Set<Value<?>> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value(new ArtifactVersion("1.2.0", LocalDateTime.now())),
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities()),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(7, 8).contains(scoreValue.get()));
    assertEquals(CONFIDENCE_NO_VULNERABILITIES, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculateWith20Used() {
    ArtifactVersionSecurityScore score = new ArtifactVersionSecurityScore();
    Set<Value<?>> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value(new ArtifactVersion("2.0.0", LocalDateTime.now())),
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities()),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(7, 8).contains(scoreValue.get()));
    assertEquals(CONFIDENCE_NO_VULNERABILITIES, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculateWith20UsedAndHighVulnerability() {
    ArtifactVersionSecurityScore score = new ArtifactVersionSecurityScore();
    Vulnerability vulnerability = TestUtils.createBasicVulnerability(10.0, "2.0.0", "2.0.0");
    Set<Value<?>> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value(new ArtifactVersion("2.0.0", LocalDateTime.now())),
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities(vulnerability)),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5));

    ScoreValue scoreValue = score.calculate(values);
    assertEquals(Score.MIN, scoreValue.get(), DELTA);
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculateWith20UsedAndLowVulnerability() {
    ArtifactVersionSecurityScore score = new ArtifactVersionSecurityScore();
    Vulnerability vulnerability = TestUtils.createBasicVulnerability(1.0, "2.0.0", "2.0.0");
    Set<Value<?>> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value(new ArtifactVersion("2.0.0", LocalDateTime.now())),
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities(vulnerability)),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(7, 8).contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculateWith20UsedAndOldVulnerability() {
    ArtifactVersionSecurityScore score = new ArtifactVersionSecurityScore();
    Vulnerability vulnerability = TestUtils.createBasicVulnerability(10.0, "1.0.0", "1.0.2");
    Set<Value<?>> values = setOf(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value(new ArtifactVersion("2.0.0", LocalDateTime.now())),
        SUPPORTED_BY_COMPANY.value(false),
        IS_APACHE.value(true),
        IS_ECLIPSE.value(false),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities(vulnerability)),
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(50),
        NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS.value(3),
        NUMBER_OF_GITHUB_STARS.value(10),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(5));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(8, 9).contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  private static ArtifactVersions testArtifactVersions(boolean with2xx) {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));
    ArtifactVersion version101 =
        new ArtifactVersion("1.0.1", LocalDateTime.now().minusMonths(13));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusMonths(6));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(72));
    if (with2xx) {
      ArtifactVersion version200 = new ArtifactVersion("2.0.0", LocalDateTime.now().minusDays(7));
      return ArtifactVersions.of(version100, version101, version110, version120, version200);
    }
    return ArtifactVersions.of(version100, version101, version110, version120);
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
