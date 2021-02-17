package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

public class ArtifactReleaseHistoryScoreTest {

  @Test
  public void smokeTest() {
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
  public void currentReleasedVersionBestHistory() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusDays(20));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    ScoreValue value = score.calculate(versions);
    System.out.println("\n\ncurrentReleasedVersionGoodHistory");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(9.5), value.get());
  }

  @Test
  public void currentReleasedVersionGoodHistory() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(5));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    ScoreValue value = score.calculate(versions);
    System.out.println("\n\ncurrentReleasedVersionGoodHistory");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(6.5), value.get());
  }

  @Test
  public void currentReleasedVersionBadHistory() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    ScoreValue value = score.calculate(versions);
    System.out.println("\n\ncurrentReleasedVersionBadHistory");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(5.5), value.get());
  }

  @Test
  public void twoMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    ScoreValue value = score.calculate(versions);
    System.out.println("\n\ntwoMonthOldVersion");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(7.0), value.get());
  }

  @Test
  public void twoMonthOldVersionStable() {
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
    System.out.println("\n\ntwoMonthOldVersionStable");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(8.6), value.get());
  }

  @Test
  public void twoMonthOldGoodVersionTrend() {
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
    System.out.println("\n\ntwoMonthOldGoodVersionTrend");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(5.8), value.get());
  }

  @Test
  public void twoMonthOldVersionBadVersionTrend() {
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
    System.out.println("\n\ntwoMonthOldVersionDecrease");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(5.2), value.get());
  }

  @Test
  public void eightMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(8));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    ScoreValue value = score.calculate(versions);
    System.out.println("\n\neightMonthOldVersion");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(5.0), value.get());
  }

  @Test
  public void fourteenMonthOldSingleVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));

    ArtifactReleaseHistoryScore score = new ArtifactReleaseHistoryScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100));
    ScoreValue value = score.calculate(versions);
    System.out.println("\n\nfourteenMonthOldSingleVersion");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(0.0), value.get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInfo() {
    new DependencyScanScore().calculate();
  }
}
