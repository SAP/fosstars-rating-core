package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.sgs.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.score.oss.FindSecBugsScore.Verification;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;

public class FindSecBugsScoreTest {

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutUsesFindSecBugs() {
    new FindSecBugsScore().calculate(LANGUAGES.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutLanguagesValue() {
    new FindSecBugsScore().calculate(USES_FIND_SEC_BUGS.unknown());
  }

  @Test
  public void testWithAllUnknown() {
    FindSecBugsScore score = new FindSecBugsScore();
    ScoreValue scoreValue = score.calculate(USES_FIND_SEC_BUGS.unknown(), LANGUAGES.unknown());
    assertTrue(scoreValue.isUnknown());
  }

  @Test
  public void testVerification() throws VerificationFailedException, IOException {
    TestVectors vectors = new TestVectors(
        newTestVector()
            .alias("1")
            .expectedScore(Score.INTERVAL)
            .set(USES_FIND_SEC_BUGS.unknown())
            .set(LANGUAGES.unknown())
            .make()
    );

    Path file = Files.createTempFile(getClass().getName(), "test");
    try {
      vectors.storeToYaml(file);

      Verification verification = new Verification(
          new FindSecBugsScore(),
          TestVectors.loadFromYaml(file));

      verification.run();
    } finally {
      Files.delete(file);
    }
  }
}