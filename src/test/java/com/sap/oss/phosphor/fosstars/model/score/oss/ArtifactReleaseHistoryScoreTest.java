package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.oss.ArtifactReleaseHistoryScore.VersionInfo;
import com.sap.oss.phosphor.fosstars.model.score.oss.ArtifactReleaseHistoryScore.VersionStats;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;

public class ArtifactReleaseHistoryScoreTest {

  private static final double DELTA = 0.2;

  @Test
  public void testBasicFunctionality() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(7));

    assertScore(
        Score.INTERVAL,
        new ArtifactReleaseHistoryScore(),
        setOf(
            RELEASED_ARTIFACT_VERSIONS.value(
                ArtifactVersions.of(version100, version110, version120)),
            ARTIFACT_VERSION.value(version110)
        ));
  }

  @Test
  public void testCalculateVersionStatistics() {
    Collection<VersionInfo> versionInfos = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    versionInfos.add(new VersionInfo(90,
        new ArtifactVersion("3.0.0", now)));
    versionInfos.add(new VersionInfo(180,
        new ArtifactVersion("2.0.0", now.minusDays(90))));
    versionInfos.add(new VersionInfo(250,
        new ArtifactVersion("1.5.0", now.minusDays(180))));
    versionInfos.add(new VersionInfo(-1,
        new ArtifactVersion("1.0.0", now.minusDays(250))));

    VersionStats stats = ArtifactReleaseHistoryScore.calculateVersionStats(versionInfos);
    assertEquals(173.33, stats.averageDaysBetweenReleases, DELTA);
    assertEquals(0.66, stats.releaseCycleTrend, DELTA);
  }

  @Test
  public void testCalculateVersionStatisticsFromArtifactVersions() {
    Collection<ArtifactVersion> artifactVersions = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    artifactVersions.add(new ArtifactVersion("3.0.0", now));
    artifactVersions.add(new ArtifactVersion("2.0.0", now.minusDays(90)));
    artifactVersions.add(new ArtifactVersion("1.5.0", now.minusDays(270)));
    artifactVersions.add(new ArtifactVersion("1.0.0", now.minusDays(520)));

    Collection<VersionInfo> versionInfo =
        ArtifactReleaseHistoryScore.versionInfo(artifactVersions);
    VersionStats stats = ArtifactReleaseHistoryScore.calculateVersionStats(versionInfo);
    assertEquals(173.33, stats.averageDaysBetweenReleases, DELTA);
    assertEquals(0.66, stats.releaseCycleTrend, DELTA);
  }

  @Test
  public void testCalculateVersionStatisticsFromArtifactVersionsSameDay() {
    Collection<ArtifactVersion> artifactVersions = new ArrayList<>();
    LocalDateTime now = LocalDateTime.now();
    artifactVersions.add(new ArtifactVersion("3.0.0", now));
    artifactVersions.add(new ArtifactVersion("2.0.0", now.minusDays(90)));
    artifactVersions.add(new ArtifactVersion("1.5.0", now.minusDays(90).minusHours(2)));

    Collection<VersionInfo> versionInfos =
        ArtifactReleaseHistoryScore.versionInfo(artifactVersions);
    VersionStats stats = ArtifactReleaseHistoryScore.calculateVersionStats(versionInfos);
    assertEquals(45.0, stats.averageDaysBetweenReleases, DELTA);
    assertEquals(-0.5, stats.releaseCycleTrend, DELTA);
  }

  @Test
  public void testCurrentReleasedVersionBestHistory() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusDays(60));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusDays(20));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(7));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.value(version110));
    assertEquals(9.5, value.get(), DELTA);
  }

  @Test
  public void testCurrentReleasedVersionGoodHistory() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusDays(150));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusDays(50));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(7));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.value(version110));
    assertEquals(6.5, value.get(), DELTA);
  }

  @Test
  public void testCurrentReleasedVersionBadHistory() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusDays(420));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusDays(50));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(7));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.value(version110));
    assertEquals(5.5, value.get(), DELTA);
  }

  @Test
  public void testTwoMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusDays(420));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusDays(50));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.value(version110));
    assertEquals(7.0, value.get(), DELTA);
  }

  @Test
  public void testTwoMonthOldVersionStable() {
    LocalDateTime now = LocalDateTime.now();
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", now.minusDays(7 * 31));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", now.minusDays(6 * 31));
    ArtifactVersion version120 =
        new ArtifactVersion("1.2.0", now.minusDays(5 * 31));
    ArtifactVersion version130 =
        new ArtifactVersion("1.3.0", now.minusDays(4 * 31));
    ArtifactVersion version140 =
        new ArtifactVersion("1.4.0", now.minusDays(3 * 31));
    ArtifactVersion version150 =
        new ArtifactVersion("1.5.0", now.minusDays(64));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions
            .of(version100, version110, version120, version130, version140, version150));
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.value(version130));
    assertEquals(8.8, value.get(), DELTA);
  }

  @Test
  public void testTwoMonthOldGoodVersionTrend() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusDays(1000));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusDays(780));
    ArtifactVersion version120 =
        new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(450));
    ArtifactVersion version130 =
        new ArtifactVersion("1.3.0", LocalDateTime.now().minusDays(300));
    ArtifactVersion version140 =
        new ArtifactVersion("1.4.0", LocalDateTime.now().minusDays(150));
    ArtifactVersion version150 =
        new ArtifactVersion("1.5.0", LocalDateTime.now().minusDays(60));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions
            .of(version100, version110, version120, version130, version140, version150));
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.value(version130));
    assertEquals(5.8, value.get(), DELTA);
  }

  @Test
  public void testTwoMonthOldVersionBadVersionTrend() {
    LocalDateTime now = LocalDateTime.now();
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", now.minusDays(18 * 31));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", now.minusDays(17 * 31));
    ArtifactVersion version120 =
        new ArtifactVersion("1.2.0", now.minusDays(15 * 31));
    ArtifactVersion version130 =
        new ArtifactVersion("1.3.0", now.minusDays(14 * 31));
    ArtifactVersion version140 =
        new ArtifactVersion("1.4.0", now.minusDays(12 * 31));
    ArtifactVersion version150 =
        new ArtifactVersion("1.5.0", now.minusDays(2 * 31));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions
            .of(version100, version110, version120, version130, version140, version150));
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.value(version150));
    assertEquals(5.2, value.get(), DELTA);
  }

  @Test
  public void testEightMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusDays(420));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusDays(240));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.value(version110));
    assertEquals(5.0, value.get(), DELTA);
  }

  @Test
  public void testFourteenMonthOldSingleVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100));
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.value(version100));
    assertEquals(0.0, value.get(), DELTA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInfo() {
    new ArtifactReleaseHistoryScore().calculate();
  }

  @Test
  public void testVersionsSelectionByComparingMajor() {
    LocalDateTime now = LocalDateTime.now();
    ArtifactVersion version100 = new ArtifactVersion("1.0.0", now.minusDays(13 * 31));
    ArtifactVersion version110 = new ArtifactVersion("1.1.0", now.minusDays(12 * 31));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", now.minusDays(11 * 31));
    ArtifactVersion version130 = new ArtifactVersion("1.3.0-MIGHTY", now.minusDays(10 * 31));
    ArtifactVersion version140 = new ArtifactVersion("1.4.0", now.minusDays(9 * 31));
    ArtifactVersion version150 = new ArtifactVersion("1.5.0", now.minusDays(8 * 31));
    ArtifactVersion version160 = new ArtifactVersion("1.6.0", now.minusDays(7 * 31));
    ArtifactVersion version170 = new ArtifactVersion("1.7.0", now.minusDays(6 * 31));
    ArtifactVersion version180 = new ArtifactVersion("1.8.0", now.minusDays(5 * 31));
    ArtifactVersion version190 = new ArtifactVersion("1.9.0", now.minusDays(4 * 31));
    ArtifactVersion version1100 = new ArtifactVersion("1.10.0", now.minusDays(3 * 31));
    ArtifactVersion version1110 = new ArtifactVersion("1.11.0", now.minusDays(2 * 31));
    ArtifactVersion version200 = new ArtifactVersion("2.0.0", now);
    ArtifactVersions artifactVersions =
        ArtifactVersions.of(version100, version110, version120, version130, version140, version150,
            version160, version170, version180, version190, version1100, version1110, version200);

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(artifactVersions);

    ScoreValue value100 = score.calculate(versions, ARTIFACT_VERSION.value(version100));
    assertEquals(8.0, value100.get(), DELTA);

    ScoreValue value120 = score.calculate(versions, ARTIFACT_VERSION.value(version120));
    assertEquals(8.0, value120.get(), DELTA);

    ScoreValue value150 = score.calculate(versions, ARTIFACT_VERSION.value(version150));
    assertEquals(8.0, value150.get(), DELTA);

    ScoreValue value1100 = score.calculate(versions, ARTIFACT_VERSION.value(version1100));
    assertEquals(8.0, value1100.get(), DELTA);

    ScoreValue value1110 = score.calculate(versions, ARTIFACT_VERSION.value(version1110));
    assertEquals(8.0, value1110.get(), DELTA);

    ScoreValue value130 = score.calculate(versions, ARTIFACT_VERSION.value(version130));
    assertEquals(8.0, value130.get(), DELTA);

    ScoreValue value200 = score.calculate(versions, ARTIFACT_VERSION.value(version200));
    assertEquals(7.1, value200.get(), DELTA);
  }

  @Test
  public void testVersionsSelectionIfNoClusterIsFound() {
    LocalDateTime now = LocalDateTime.now();
    ArtifactVersion version100 = new ArtifactVersion("1.0.0", now.minusDays(7 * 31));
    ArtifactVersion version110 = new ArtifactVersion("1.1.0", now.minusDays(6 * 31));
    ArtifactVersion version200 = new ArtifactVersion("2.0.0", now.minusDays(31));
    ArtifactVersions artifactVersions = ArtifactVersions.of(version100, version110, version200);

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(artifactVersions);

    ScoreValue value200 = score.calculate(versions, ARTIFACT_VERSION.value(version200));
    assertEquals(5.5, value200.get(), DELTA);

    ScoreValue value100 = score.calculate(versions, ARTIFACT_VERSION.value(version100));
    assertEquals(5.5, value100.get(), DELTA);

    ScoreValue value150 = score.calculate(versions, ARTIFACT_VERSION.value(version110));
    assertEquals(5.5, value150.get(), DELTA);
  }

  @Test
  public void testVersionsSelectionIfNotSemanticVersionAndNoClusterExists() {
    LocalDateTime now = LocalDateTime.now();
    ArtifactVersion nonSematicVersion = new ArtifactVersion("MIGHTY-1.0", now.minusDays(4 * 31));
    ArtifactVersion version100 = new ArtifactVersion("1.0.0", now.minusDays(3 * 31));
    ArtifactVersion version110 = new ArtifactVersion("1.1.0", now.minusDays(2 * 31));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", now.minusDays(31));
    ArtifactVersion version200 = new ArtifactVersion("2.0.0", now.plusDays(2 * 31));

    ArtifactVersions artifactVersions =
        ArtifactVersions.of(version100, nonSematicVersion, version110, version120, version200);
    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(artifactVersions);

    ScoreValue value110 = score.calculate(versions, ARTIFACT_VERSION.value(version110));
    assertEquals(7.25, value110.get(), DELTA);

    ScoreValue value100 = score.calculate(versions, ARTIFACT_VERSION.value(version100));
    assertEquals(7.25, value100.get(), DELTA);

    ScoreValue value120 = score.calculate(versions, ARTIFACT_VERSION.value(version120));
    assertEquals(7.25, value120.get(), DELTA);

    ScoreValue value200 = score.calculate(versions, ARTIFACT_VERSION.value(version200));
    assertEquals(7.25, value200.get(), DELTA);

    ScoreValue nonSmenaticValue =
        score.calculate(versions, ARTIFACT_VERSION.value(nonSematicVersion));
    assertEquals(7.25, nonSmenaticValue.get(), DELTA);
  }

  @Test
  public void testVersionsSelectionIfNotSemanticVersionAndClusterExists() {
    LocalDateTime now = LocalDateTime.now();
    ArtifactVersion nonSematicVersion = new ArtifactVersion("MIGHTY-1.0", now.minusDays(14 * 31));
    ArtifactVersion version100 = new ArtifactVersion("1.0.0", now.minusDays(13 * 31));
    ArtifactVersion version110 = new ArtifactVersion("1.1.0", now.minusDays(12 * 31));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", now.minusDays(11 * 31));
    ArtifactVersion version130 = new ArtifactVersion("1.3.0-MIGHTY", now.minusDays(10 * 31));
    ArtifactVersion version140 = new ArtifactVersion("1.4.0", now.minusDays(9 * 31));
    ArtifactVersion version150 = new ArtifactVersion("1.5.0", now.minusDays(8 * 31));
    ArtifactVersion version160 = new ArtifactVersion("1.6.0", now.minusDays(7 * 31));
    ArtifactVersion version170 = new ArtifactVersion("1.7.0", now.minusDays(6 * 31));
    ArtifactVersion version180 = new ArtifactVersion("1.8.0", now.minusDays(5 * 31));
    ArtifactVersion version190 = new ArtifactVersion("1.9.0", now.minusDays(4 * 31));
    ArtifactVersion version1100 = new ArtifactVersion("1.10.0", now.minusDays(3 * 31));
    ArtifactVersion version1110 = new ArtifactVersion("1.11.0", now.minusDays(2 * 31));
    ArtifactVersion version200 = new ArtifactVersion("2.0.0", now.plusDays(2 * 31));

    ArtifactVersions artifactVersions = ArtifactVersions.of(version100, nonSematicVersion,
        version110, version120, version130, version140, version150, version160, version170,
        version180, version190, version1100, version1110, version200);
    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(artifactVersions);

    ScoreValue value110 = score.calculate(versions, ARTIFACT_VERSION.value(version110));
    assertEquals(8.0, value110.get(), DELTA);

    ScoreValue value100 = score.calculate(versions, ARTIFACT_VERSION.value(version100));
    assertEquals(8.0, value100.get(), DELTA);

    ScoreValue value150 = score.calculate(versions, ARTIFACT_VERSION.value(version150));
    assertEquals(8.0, value150.get(), DELTA);

    ScoreValue value1100 = score.calculate(versions, ARTIFACT_VERSION.value(version1100));
    assertEquals(8.0, value1100.get(), DELTA);

    ScoreValue value1110 = score.calculate(versions, ARTIFACT_VERSION.value(version1110));
    assertEquals(8.0, value1110.get(), DELTA);

    ScoreValue value200 = score.calculate(versions, ARTIFACT_VERSION.value(version200));
    assertEquals(7.25, value200.get(), DELTA);

    ScoreValue nonSmenaticValue =
        score.calculate(versions, ARTIFACT_VERSION.value(nonSematicVersion));
    assertEquals(7.25, nonSmenaticValue.get(), DELTA);
  }

  @Test
  public void testVersionsSelectionIfArtifactIsNotSemantic() {
    LocalDateTime now = LocalDateTime.now();
    ArtifactVersion version100 = new ArtifactVersion("1.0.0", now.minusDays(6 * 31));
    ArtifactVersion versionMighty = new ArtifactVersion("MIGHTY-1.0", now.minusDays(4 * 31));
    ArtifactVersion version110 = new ArtifactVersion("1.1.0", now.minusDays(3 * 31));
    ArtifactVersion version200 = new ArtifactVersion("2.0.0", now.minusDays(2 * 31));

    ArtifactVersions artifactVersions =
        ArtifactVersions.of(version100, versionMighty, version110, version200);
    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(artifactVersions);

    ScoreValue valueForNotSemanticVersion =
        score.calculate(versions, ARTIFACT_VERSION.value(versionMighty));
    assertEquals(8.3, valueForNotSemanticVersion.get(), DELTA);

    ScoreValue valueForSemanticVersion =
        score.calculate(versions, ARTIFACT_VERSION.value(version200));
    assertEquals(8.3, valueForSemanticVersion.get(), DELTA);
  }

  @Test
  public void testBasicFunctionalityWithUnknownArtifactVersions() {
    ArtifactVersion version100 = new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    ScoreValue value =
        score.calculate(RELEASED_ARTIFACT_VERSIONS.unknown(), ARTIFACT_VERSION.value(version100));
    assertTrue(value.isUnknown());
    assertEquals(Confidence.MIN, value.confidence(), DELTA);
  }

  @Test
  public void testBasicFunctionalityWithUnknownValues() {
    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    ScoreValue value =
        score.calculate(RELEASED_ARTIFACT_VERSIONS.unknown(), ARTIFACT_VERSION.unknown());
    assertTrue(value.isUnknown());
    assertEquals(Confidence.MIN, value.confidence(), DELTA);
  }

  @Test
  public void testBasicFunctionalityWithUnknownArtifactVersion() {
    LocalDateTime now = LocalDateTime.now();
    ArtifactVersion version100 = new ArtifactVersion("1.0.0", now.minusDays(6 * 31));
    ArtifactVersion version110 = new ArtifactVersion("1.1.0", now.minusDays(4 * 31));
    ArtifactVersion version200 = new ArtifactVersion("2.0.0", now.minusDays(2 * 31));

    ArtifactVersions artifactVersions = ArtifactVersions.of(version100, version110, version200);
    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(artifactVersions);
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.unknown());
    assertEquals(6.0, value.get(), DELTA);
    assertTrue(value.confidence() < Confidence.MAX);
  }
}