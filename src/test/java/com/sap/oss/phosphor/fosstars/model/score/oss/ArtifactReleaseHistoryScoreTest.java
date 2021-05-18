package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

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
import org.junit.Assert;
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
            ARTIFACT_VERSION.unknown()
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
    Assert.assertEquals(173.33333333333334, stats.averageDaysBetweenReleases, DELTA);
    Assert.assertEquals(0.6666666666666666, stats.releaseCycleTrend, DELTA);
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
    Assert.assertEquals(173.33333333333334, stats.averageDaysBetweenReleases, DELTA);
    Assert.assertEquals(0.6666666666666666, stats.releaseCycleTrend, DELTA);
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
    Assert.assertEquals(45.0, stats.averageDaysBetweenReleases, DELTA);
    Assert.assertEquals(-0.5, stats.releaseCycleTrend, DELTA);
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
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.unknown());
    Assert.assertEquals(8.5, value.get(), DELTA);
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
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.unknown());
    Assert.assertEquals(5.5, value.get(), DELTA);
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
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.unknown());
    Assert.assertEquals(4.5, value.get(), DELTA);
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
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.unknown());
    Assert.assertEquals(6.0, value.get(), DELTA);
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
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.unknown());
    Assert.assertEquals(7.8, value.get(), DELTA);
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
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.unknown());
    Assert.assertEquals(4.8, value.get(), DELTA);
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
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.unknown());
    Assert.assertEquals(4.2, value.get(), DELTA);
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
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.unknown());
    Assert.assertEquals(4.0, value.get(), DELTA);
  }

  @Test
  public void testFourteenMonthOldSingleVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100));
    ScoreValue value = score.calculate(versions, ARTIFACT_VERSION.unknown());
    Assert.assertEquals(0.0, value.get(), DELTA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInfo() {
    new ArtifactReleaseHistoryScore().calculate();
  }

  @Test
  public void testVersionRangeSelection() {
    LocalDateTime now = LocalDateTime.now();
    ArtifactVersion version100 = new ArtifactVersion("1.0.0", now.minusDays(7 * 31));
    ArtifactVersion version110 = new ArtifactVersion("1.1.0", now.minusDays(6 * 31));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", now.minusDays(5 * 31));
    ArtifactVersion version130 = new ArtifactVersion("1.3.0", now.minusDays(4 * 31));
    ArtifactVersion version140 = new ArtifactVersion("1.4.0", now.minusDays(3 * 31));
    ArtifactVersion version150 = new ArtifactVersion("1.5.0", now.minusDays(64));
    ArtifactVersion version200 = new ArtifactVersion("2.0.0", now.minusDays(31));
    ArtifactVersion version210 = new ArtifactVersion("2.1.0", now);

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions =
        RELEASED_ARTIFACT_VERSIONS.value(ArtifactVersions.of(version100, version110, version120,
            version130, version140, version150, version200, version210));
    Value<ArtifactVersion> version = ARTIFACT_VERSION.value(version120);
    ScoreValue value = score.calculate(versions, version);
    Assert.assertEquals(8.8, value.get(), DELTA);
  }
}
