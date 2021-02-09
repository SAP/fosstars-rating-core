package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
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

public class ArtifactMaintenanceScoreTest {

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
        new ArtifactMaintenanceScore(),
        setOf(
            RELEASED_ARTIFACT_VERSIONS.value(
                ArtifactVersions.of(version100, version110, version120)),
            ARTIFACT_VERSION.value("1.2.0")
        ));
  }

  @Test
  public void currentVersionUnknown() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    ArtifactMaintenanceScore score = new ArtifactMaintenanceScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    Value<String> version = ARTIFACT_VERSION.value("2.0.0");
    ScoreValue value = score.calculate(versions, version);
    System.out.println("\n\ncurrentVersionUnknown");
    System.out.printf("Available versions: %s;%nChecked version %s;%nrating: %2.2f%n", versions, version, value.get());
    Assert.assertEquals(Double.valueOf(0.0), value.get());
  }

  @Test
  public void currentReleasedVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    ArtifactMaintenanceScore score = new ArtifactMaintenanceScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    Value<String> version = ARTIFACT_VERSION.value("1.2.0");
    ScoreValue value = score.calculate(versions, version);
    System.out.println("\n\ncurrentReleasedVersion");
    System.out.printf("Available versions: %s;%nChecked version %s;%nrating: %2.2f%n", versions, version, value.get());
    Assert.assertEquals(Double.valueOf(10.0), value.get());
  }

  @Test
  public void twoMonthOldVersionButNewerAvailable() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(7));

    ArtifactMaintenanceScore score = new ArtifactMaintenanceScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    Value<String> version = ARTIFACT_VERSION.value("1.1.0");
    ScoreValue value = score.calculate(versions, version);
    System.out.println("\n\ntwoMonthOldVersionButNewerAvailable");
    System.out.printf("Available versions: %s;%nChecked version %s;%nrating: %2.2f%n", versions, version, value.get());
    Assert.assertEquals(Double.valueOf(7.0), value.get());
  }

  @Test
  public void twoMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(2));

    ArtifactMaintenanceScore score = new ArtifactMaintenanceScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    Value<String> version = ARTIFACT_VERSION.value("1.1.0");
    ScoreValue value = score.calculate(versions, version);
    System.out.println("\n\ntwoMonthOldVersion");
    System.out.printf("Available versions: %s;%nChecked version %s;%nrating: %2.2f%n", versions, version, value.get());
    Assert.assertEquals(Double.valueOf(9.0), value.get());
  }

  @Test
  public void eightMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(8));

    ArtifactMaintenanceScore score = new ArtifactMaintenanceScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    Value<String> version = ARTIFACT_VERSION.value("1.1.0");
    ScoreValue value = score.calculate(versions, version);
    System.out.println("\n\neightMonthOldVersion");
    System.out.printf("Available versions: %s;%nChecked version %s;%nrating: %2.2f%n", versions, version, value.get());
    Assert.assertEquals(Double.valueOf(6.0), value.get());
  }

  @Test
  public void fourteenMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));

    ArtifactMaintenanceScore score = new ArtifactMaintenanceScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100));
    Value<String> version = ARTIFACT_VERSION.value("1.0.0");
    ScoreValue value = score.calculate(versions, version);
    System.out.println("\n\nfourteenMonthOldVersion");
    System.out.printf("Available versions: %s;%nChecked version %s;%nrating: %2.2f%n", versions, version, value.get());
    Assert.assertEquals(Double.valueOf(2.0), value.get());
  }

  @Test
  public void fourteenMonthOldVersionNewerAvailable() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDate.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDate.now().minusMonths(8));

    ArtifactMaintenanceScore score = new ArtifactMaintenanceScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    Value<String> version = ARTIFACT_VERSION.value("1.0.0");
    ScoreValue value = score.calculate(versions, version);
    System.out.println("\n\nfourteenMonthOldVersion");
    System.out.printf("Available versions: %s;%nChecked version %s;%nrating: %2.2f%n", versions, version, value.get());
    Assert.assertEquals(Double.valueOf(0.0), value.get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInfo() {
    new DependencyScanScore().calculate();
  }
}
