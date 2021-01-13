package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_ADDRESS_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MEMORY_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_UNDEFINED_BEHAVIOR_SANITIZER;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static com.sap.oss.phosphor.fosstars.model.value.Language.CPP;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;

public class MemorySafetyTestingScoreTest {

  @Test(expected = IllegalArgumentException.class)
  public void testWithLackOfValues() {
    new MemorySafetyTestingScore().calculate(LANGUAGES.value(Languages.of(CPP)));
  }

  @Test
  public void testWithoutAllUnknown() {
    assertScore(
        Score.INTERVAL,
        new MemorySafetyTestingScore(),
        setOf(
            LANGUAGES.unknown(),
            USES_ADDRESS_SANITIZER.unknown(),
            USES_MEMORY_SANITIZER.unknown(),
            USES_UNDEFINED_BEHAVIOR_SANITIZER.unknown()));
  }

  @Test
  public void testVerification() throws VerificationFailedException, IOException {
    TestVectors vectors = new TestVectors(
        newTestVector()
            .alias("1")
            .expectedScore(Score.INTERVAL)
            .set(LANGUAGES.unknown())
            .set(USES_ADDRESS_SANITIZER.unknown())
            .set(USES_MEMORY_SANITIZER.unknown())
            .set(USES_UNDEFINED_BEHAVIOR_SANITIZER.unknown())
            .make()
    );

    Path file = Files.createTempFile(getClass().getName(), "test");
    try {
      vectors.storeToYaml(file);

      ScoreVerification verification = new ScoreVerification(
          new MemorySafetyTestingScore(),
          TestVectors.loadFromYaml(file));

      verification.run();
    } finally {
      Files.delete(file);
    }
  }
}