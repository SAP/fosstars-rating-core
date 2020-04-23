package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PACKAGE_MANAGERS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.sgs.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.score.oss.NoHttpToolScore.Verification;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
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
    List<TestVector> vectors = Arrays.asList(
        newTestVector()
            .alias("1")
            .expectedScore(Score.INTERVAL)
            .set(USES_NOHTTP.unknown())
            .set(PACKAGE_MANAGERS.unknown())
            .make()
    );

    Path file = Files.createTempFile("NoHttpToolScoreTestVectors", "test");
    try {
      Verification.storeTestVectorsToYaml(vectors, file);

      Verification verification = new Verification(
          new NoHttpToolScore(),
          Verification.loadTestVectorsFromYaml(file));

      verification.run();
    } finally {
      Files.delete(file);
    }
  }
}