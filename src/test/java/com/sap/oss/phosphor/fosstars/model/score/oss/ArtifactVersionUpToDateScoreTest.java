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
import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

public class ArtifactVersionUpToDateScoreTest {

  @Test
  public void testBasicFunctionality() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(7));

    assertScore(
        Score.INTERVAL,
        new ArtifactVersionUpToDateScore(),
        setOf(
            RELEASED_ARTIFACT_VERSIONS.value(
                ArtifactVersions.of(version100, version110, version120)),
            ARTIFACT_VERSION.value(new ArtifactVersion("1.1.0", LocalDateTime.now()))
        ));
  }

  @Test
  public void testCurrentVersionUnknown() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(7));

    ArtifactVersionUpToDateScore score = new ArtifactVersionUpToDateScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    Value<ArtifactVersion> version =
        ARTIFACT_VERSION.value(new ArtifactVersion("2.0.0", LocalDateTime.now()));
    ScoreValue value = score.calculate(versions, version);
    Assert.assertTrue(value.isUnknown());
  }

  @Test
  public void testCurrentReleasedVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(7));

    ArtifactVersionUpToDateScore score = new ArtifactVersionUpToDateScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    Value<ArtifactVersion> version =
        ARTIFACT_VERSION.value(new ArtifactVersion("1.2.0", LocalDateTime.now()));
    ScoreValue value = score.calculate(versions, version);
    Assert.assertEquals(Double.valueOf(10.0), value.get());
  }

  @Test
  public void testTwoMonthOldVersionButNewerAvailable() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusMonths(2));
    ArtifactVersion version120 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(7));

    ArtifactVersionUpToDateScore score = new ArtifactVersionUpToDateScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110, version120));
    Value<ArtifactVersion> version =
        ARTIFACT_VERSION.value(new ArtifactVersion("1.1.0", LocalDateTime.now()));
    ScoreValue value = score.calculate(versions, version);
    Assert.assertEquals(Double.valueOf(7.0), value.get());
  }

  @Test
  public void testTwoMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusMonths(2));

    ArtifactVersionUpToDateScore score = new ArtifactVersionUpToDateScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    Value<ArtifactVersion> version =
        ARTIFACT_VERSION.value(new ArtifactVersion("1.1.0", LocalDateTime.now()));
    ScoreValue value = score.calculate(versions, version);
    Assert.assertEquals(Double.valueOf(9.0), value.get());
  }

  @Test
  public void testEightMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusMonths(8));

    ArtifactVersionUpToDateScore score = new ArtifactVersionUpToDateScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    Value<ArtifactVersion> version =
        ARTIFACT_VERSION.value(new ArtifactVersion("1.1.0", LocalDateTime.now()));
    ScoreValue value = score.calculate(versions, version);
    Assert.assertEquals(Double.valueOf(6.0), value.get());
  }

  @Test
  public void testFourteenMonthOldVersion() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));

    ArtifactVersionUpToDateScore score = new ArtifactVersionUpToDateScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100));
    Value<ArtifactVersion> version =
        ARTIFACT_VERSION.value(new ArtifactVersion("1.0.0", LocalDateTime.now()));
    ScoreValue value = score.calculate(versions, version);
    Assert.assertEquals(Double.valueOf(2.0), value.get());
  }

  @Test
  public void testFourteenMonthOldVersionNewerAvailable() {
    ArtifactVersion version100 =
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusMonths(14));
    ArtifactVersion version110 =
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusMonths(8));

    ArtifactVersionUpToDateScore score = new ArtifactVersionUpToDateScore();
    Value<ArtifactVersions> versions = RELEASED_ARTIFACT_VERSIONS.value(
        ArtifactVersions.of(version100, version110));
    Value<ArtifactVersion> version =
        ARTIFACT_VERSION.value(new ArtifactVersion("1.0.0", LocalDateTime.now()));
    ScoreValue value = score.calculate(versions, version);
    Assert.assertEquals(Double.valueOf(0.0), value.get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoInfo() {
    new ArtifactVersionUpToDateScore().calculate();
  }
}