package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;

public class NoHttpToolScoreTest {

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutUsesNoHttpValue() {
    new NoHttpToolScore().calculate(PACKAGE_MANAGERS.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutPackageManagersValue() {
    new NoHttpToolScore().calculate(USES_NOHTTP.unknown());
  }

  @Test
  public void testWithoutAllUnknown() {
    assertScore(
        Score.INTERVAL,
        new NoHttpToolScore(),
        setOf(USES_NOHTTP.unknown(), PACKAGE_MANAGERS.unknown()));
  }

  @Test
  public void testVerification() throws VerificationFailedException, IOException {
    TestVectors vectors = new TestVectors(
        newTestVector()
            .alias("1")
            .expectedScore(Score.INTERVAL)
            .set(USES_NOHTTP.unknown())
            .set(PACKAGE_MANAGERS.unknown())
            .make()
    );

    Path file = Files.createTempFile("NoHttpToolScoreTestVectors", "test");
    try {
      vectors.storeToYaml(file);

      ScoreVerification verification = new ScoreVerification(
          new NoHttpToolScore(),
          TestVectors.loadFromYaml(file));

      verification.run();
    } finally {
      Files.delete(file);
    }
  }
}