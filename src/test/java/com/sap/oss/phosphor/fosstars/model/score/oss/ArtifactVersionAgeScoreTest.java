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

public class ArtifactVersionAgeScoreTest {

  @Test
  public void smokeTest() {
    long monthMilliseconds = 2629800000L;
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    assertScore(
        Score.INTERVAL,
        new ArtifactVersionAgeScore(),
        setOf(
            RELEASED_ARTIFACT_VERSIONS.value(
                ArtifactVersions.of(version100, version110, version120))
        ));
  }

  @Test
  public void currentReleasedVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    ArtifactVersionAgeScore score = new ArtifactVersionAgeScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    ScoreValue value = score.calculate(versions);
    System.out.println("\n\ncurrentReleasedVersion");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(10.0), value.get());
  }

  @Test
  public void twoMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));

    ArtifactVersionAgeScore score = new ArtifactVersionAgeScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    ScoreValue value = score.calculate(versions);
    System.out.println("\n\ntwoMonthOldVersion");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(5.0), value.get());
  }

  @Test
  public void eightMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(8));

    ArtifactVersionAgeScore score = new ArtifactVersionAgeScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    ScoreValue value = score.calculate(versions);
    System.out.println("\n\neightMonthOldVersion");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(2.0), value.get());
  }

  @Test
  public void fourteenMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));

    ArtifactVersionAgeScore score = new ArtifactVersionAgeScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100));
    ScoreValue value = score.calculate(versions);
    System.out.println("\n\nfourteenMonthOldVersion");
    System.out.printf("Available versions: %s;%nrating: %2.2f%n", versions, value.get());
    Assert.assertEquals(Double.valueOf(0.0), value.get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInfo() {
    new DependencyScanScore().calculate();
  }
}
