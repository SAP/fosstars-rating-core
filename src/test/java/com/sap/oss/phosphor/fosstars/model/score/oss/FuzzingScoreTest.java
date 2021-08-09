package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.allUnknown;
import static com.sap.oss.phosphor.fosstars.model.qa.TestVectorBuilder.newTestVector;
import static com.sap.oss.phosphor.fosstars.model.value.Language.CPP;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Test;

public class FuzzingScoreTest {

  private static final FuzzingScore SCORE = new FuzzingScore();

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutLanguage() {
    SCORE.calculate(LANGUAGES.value(Languages.of(CPP)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutOssFuzz() {
    SCORE.calculate(FUZZED_IN_OSS_FUZZ.unknown());
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
            .set(LANGUAGES.unknown())
            .set(FUZZED_IN_OSS_FUZZ.unknown())
            .make()
    );

    Path file = Files.createTempFile(getClass().getName(), "test");
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