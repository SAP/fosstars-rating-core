package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;

public class NoHttpToolScoreTest {

  private static final NoHttpToolScore SCORE = new NoHttpToolScore();

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutUsesNoHttpValue() {
    SCORE.calculate(PACKAGE_MANAGERS.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutPackageManagersValue() {
    SCORE.calculate(USES_NOHTTP.unknown());
  }

  @Test
  public void testWithoutAllUnknown() {
    assertTrue(SCORE.calculate(allUnknown(SCORE.allFeatures())).isUnknown());
  }

  @Test
  public void testVerification() throws VerificationFailedException, IOException {
    TestVectors vectors = new TestVectors(
        newTestVector()
            .alias("test")
            .expectUnknownScore()
            .set(USES_NOHTTP.unknown())
            .set(PACKAGE_MANAGERS.unknown())
            .make()
    );

    Path file = Files.createTempFile("NoHttpToolScoreTestVectors", "test");
    try {
      vectors.storeToYaml(file);

      ScoreVerification verification = new ScoreVerification(
          SCORE,
          TestVectors.loadFromYaml(file));

      verification.run();
    } finally {
      Files.delete(file);
    }
  }
}