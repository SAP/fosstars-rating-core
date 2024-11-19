package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_FIND_SEC_BUGS;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class FindSecBugsScoreTest {

  private static final FindSecBugsScore SCORE = new FindSecBugsScore();

  @Test
  public void testWithoutUsesFindSecBugs() {
    assertThrows(IllegalArgumentException.class, () -> SCORE.calculate(LANGUAGES.unknown()));
  }

  @Test
  public void testWithoutLanguagesValue() {
    assertThrows(
        IllegalArgumentException.class, () -> SCORE.calculate(USES_FIND_SEC_BUGS.unknown()));
  }

  @Test
  public void testWithAllUnknown() {
    FindSecBugsScore score = SCORE;
    ScoreValue scoreValue = score.calculate(USES_FIND_SEC_BUGS.unknown(), LANGUAGES.unknown());
    assertTrue(scoreValue.isUnknown());
  }

  @Test
  public void testVerification() throws VerificationFailedException, IOException {
    TestVectors vectors =
        new TestVectors(
            newTestVector()
                .alias("test")
                .expectedScore(Score.INTERVAL)
                .set(USES_FIND_SEC_BUGS.value(true))
                .set(LANGUAGES.value(Languages.of(JAVA)))
                .make());

    Path file = Files.createTempFile(getClass().getName(), "test");
    try {
      vectors.storeToYaml(file);

      ScoreVerification verification = new ScoreVerification(SCORE, TestVectors.loadFromYaml(file));

      verification.run();
    } finally {
      Files.delete(file);
    }
  }
}
