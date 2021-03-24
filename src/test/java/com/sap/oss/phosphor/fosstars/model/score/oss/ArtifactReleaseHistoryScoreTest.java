package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.oss.ArtifactReleaseHistoryScore.VersionInfo;
import com.sap.oss.phosphor.fosstars.model.score.oss.ArtifactReleaseHistoryScore.VersionStats;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;

public class ArtifactReleaseHistoryScoreTest {

  private static final double DELTA = 0.2;

  @Test
  public void testBasicFunctionality() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    assertScore(
        Score.INTERVAL,
        new ArtifactReleaseHistoryScore(),
        setOf(
            RELEASED_ARTIFACT_VERSIONS.value(
                ArtifactVersions.of(version100, version110, version120))
        ));
  }

  @Test
  public void testCalculateVersionStatistics() {
    Collection<VersionInfo> versionInfos = new ArrayList<>();
    versionInfos.add(new VersionInfo(90,
        new ArtifactVersion("3.0.0", LocalDate.now())));
    versionInfos.add(new VersionInfo(180,
        new ArtifactVersion("2.0.0", LocalDate.now().minusDays(90))));
    versionInfos.add(new VersionInfo(250,
        new ArtifactVersion("1.5.0", LocalDate.now().minusDays(180))));
    versionInfos.add(new VersionInfo(-1,
        new ArtifactVersion("1.0.0", LocalDate.now().minusDays(250))));

    VersionStats stats = ArtifactReleaseHistoryScore.calculateVersionStats(versionInfos);
    Assert.assertEquals(173.33333333333334, stats.averageDaysBetweenReleases, DELTA);
    Assert.assertEquals(0.6666666666666666, stats.releaseCycleTrend, DELTA);
  }

  @Test
  public void testCalculateVersionStatisticsFromArtifactVersions() {
    Collection<ArtifactVersion> artifactVersions = new ArrayList<>();
    artifactVersions.add(new ArtifactVersion("3.0.0", LocalDate.now()));
    artifactVersions.add(new ArtifactVersion("2.0.0", LocalDate.now().minusDays(90)));
    artifactVersions.add(new ArtifactVersion("1.5.0", LocalDate.now().minusDays(270)));
    artifactVersions.add(new ArtifactVersion("1.0.0", LocalDate.now().minusDays(520)));

    Collection<VersionInfo> versionInfos =
        ArtifactReleaseHistoryScore.createVersionInfos(artifactVersions);
    VersionStats stats = ArtifactReleaseHistoryScore.calculateVersionStats(versionInfos);
    Assert.assertEquals(173.33333333333334, stats.averageDaysBetweenReleases, DELTA);
    Assert.assertEquals(0.6666666666666666, stats.releaseCycleTrend, DELTA);
  }

  @Test
  public void testCurrentReleasedVersionBestHistory() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusDays(20));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    ScoreValue value = score.calculate(versions);
    Assert.assertEquals(9.5, value.get(), DELTA);
  }

  @Test
  public void testCurrentReleasedVersionGoodHistory() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(5));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    ScoreValue value = score.calculate(versions);
    Assert.assertEquals(6.5, value.get(), DELTA);
  }

  @Test
  public void testCurrentReleasedVersionBadHistory() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    ScoreValue value = score.calculate(versions);
    Assert.assertEquals(5.5, value.get(), DELTA);
  }

  @Test
  public void testTwoMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    ScoreValue value = score.calculate(versions);
    Assert.assertEquals(7.0, value.get(), DELTA);
  }

  @Test
  public void testTwoMonthOldVersionStable() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(7));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(6));
    ArtifactVersion version120 =
        new ArtifactVersion("1.2.0", LocalDate.now().minusMonths(5));
    ArtifactVersion version130 =
        new ArtifactVersion("1.3.0", LocalDate.now().minusMonths(4));
    ArtifactVersion version140 =
        new ArtifactVersion("1.4.0", LocalDate.now().minusMonths(3));
    ArtifactVersion version150 =
        new ArtifactVersion("1.5.0", LocalDate.now().minusMonths(2));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions
            .of(version100, version110, version120, version130, version140, version150));
    ScoreValue value = score.calculate(versions);
    Assert.assertEquals(7.6, value.get(), DELTA);
  }

  @Test
  public void testTwoMonthOldGoodVersionTrend() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(37));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(26));
    ArtifactVersion version120 =
        new ArtifactVersion("1.2.0", LocalDate.now().minusMonths(15));
    ArtifactVersion version130 =
        new ArtifactVersion("1.3.0", LocalDate.now().minusMonths(10));
    ArtifactVersion version140 =
        new ArtifactVersion("1.4.0", LocalDate.now().minusMonths(5));
    ArtifactVersion version150 =
        new ArtifactVersion("1.5.0", LocalDate.now().minusMonths(2));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions
            .of(version100, version110, version120, version130, version140, version150));
    ScoreValue value = score.calculate(versions);
    Assert.assertEquals(5.8, value.get(), DELTA);
  }

  @Test
  public void testTwoMonthOldVersionBadVersionTrend() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(18));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(17));
    ArtifactVersion version120 =
        new ArtifactVersion("1.2.0", LocalDate.now().minusMonths(15));
    ArtifactVersion version130 =
        new ArtifactVersion("1.3.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version140 =
        new ArtifactVersion("1.4.0", LocalDate.now().minusMonths(12));
    ArtifactVersion version150 =
        new ArtifactVersion("1.5.0", LocalDate.now().minusMonths(2));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions
            .of(version100, version110, version120, version130, version140, version150));
    ScoreValue value = score.calculate(versions);
    Assert.assertEquals(5.2, value.get(), DELTA);
  }

  @Test
  public void testEightMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(8));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    ScoreValue value = score.calculate(versions);
    Assert.assertEquals(5.0, value.get(), DELTA);
  }

  @Test
  public void testFourteenMonthOldSingleVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100));
    ScoreValue value = score.calculate(versions);
    Assert.assertEquals(0.0, value.get(), DELTA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInfo() {
    new ArtifactReleaseHistoryScore().calculate();
  }
}
