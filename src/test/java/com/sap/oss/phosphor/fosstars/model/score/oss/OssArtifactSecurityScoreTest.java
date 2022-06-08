package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.getDefaultValues;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_ARTIFACT;
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
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.Test;

public class OssArtifactSecurityScoreTest {

  private static final double DELTA = 0.2;
  private static final double CONFIDENCE_NO_VULNERABILITY = 9.411764705882351;

  @Test
  public void testSerializeAndDeserialize() throws IOException {
    ObjectMapper mapper = Json.mapper();
    ArtifactVersionSecurityScore artifactVersionSecurityScore = new ArtifactVersionSecurityScore();
    OssSecurityScore ossSecurityScore = new OssSecurityScore();
    OssArtifactSecurityScore score =
        new OssArtifactSecurityScore(artifactVersionSecurityScore, ossSecurityScore);
    byte[] bytes = mapper.writeValueAsBytes(score);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    OssArtifactSecurityScore clone = mapper.readValue(bytes, OssArtifactSecurityScore.class);
    assertEquals(score, clone);
  }

  @Test
  public void testCalculateForAllUnknown() {
    Score score = new OssArtifactSecurityScore();
    ScoreValue scoreValue = score.calculate(Utils.allUnknown(score.allFeatures()));
    assertTrue(scoreValue.isUnknown());
  }
  
  @Test
  public void testCalculateOssSecurityScoreUnknown() {
    Score ossSecurityScore = new OssSecurityScore();
    Set<Value<?>> values = Utils.allUnknown(ossSecurityScore.allFeatures());
    
    OssArtifactSecurityScore ossArtifactSecurityScore = new OssArtifactSecurityScore();
    values.add(RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(false)));
    values.add(ARTIFACT_VERSION.value(new ArtifactVersion("1.2.0", LocalDateTime.now())));
    values.add(VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities()));

    ScoreValue scoreValue = ossArtifactSecurityScore.calculate(values);
    assertTrue(scoreValue.isUnknown());
  }

  @Test
  public void testCalculateWithCurrentVersion() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(false)),
        ARTIFACT_VERSION.value(new ArtifactVersion("1.2.0", LocalDateTime.now())));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(5, 6).contains(scoreValue.get()));
    assertEquals(CONFIDENCE_NO_VULNERABILITY, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculateWithOldVersion() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(false)),
        ARTIFACT_VERSION.value(new ArtifactVersion("1.0.0", LocalDateTime.now())));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(4.5, 5.5).contains(scoreValue.get()));
    assertEquals(CONFIDENCE_NO_VULNERABILITY, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculateWithCurrentAndV200() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value(new ArtifactVersion("1.2.0", LocalDateTime.now())));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(5, 6).contains(scoreValue.get()));
    assertEquals(CONFIDENCE_NO_VULNERABILITY, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculateWithV200Used() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        ARTIFACT_VERSION.value(new ArtifactVersion("2.0.0", LocalDateTime.now())));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(5, 6).contains(scoreValue.get()));
    assertEquals(CONFIDENCE_NO_VULNERABILITY, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculateWithV200UsedAndOldVulnerability() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Vulnerability vulnerability = createBasicVulnerability();

    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities(vulnerability)),
        ARTIFACT_VERSION.value(new ArtifactVersion("2.0.0", LocalDateTime.now())));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(6, 7).contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testCalculateWith20UsedAndVulnerability() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Vulnerability vulnerability = TestUtils.createBasicVulnerability(10.0, "2.0.0", "2.0.0");

    Set<Value<?>> values = getDefaultValues(
        RELEASED_ARTIFACT_VERSIONS.value(testArtifactVersions(true)),
        VULNERABILITIES_IN_ARTIFACT.value(new Vulnerabilities(vulnerability)),
        ARTIFACT_VERSION.value(new ArtifactVersion("2.0.0", LocalDateTime.now())));

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(0, 1).contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  @Test
  public void testMaxScore() {
    OssArtifactSecurityScore score = new OssArtifactSecurityScore();
    Set<Value<?>> values = TestUtils.getBestValues();

    ScoreValue scoreValue = score.calculate(values);
    assertTrue(DoubleInterval.closed(10, 10).contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    checkUsedValues(scoreValue);
  }

  private Vulnerability createBasicVulnerability() {
    return TestUtils.createBasicVulnerability(9.0, "1.0.0", "1.2.0");
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
    for (Value value : scoreValue.usedValues()) {
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
