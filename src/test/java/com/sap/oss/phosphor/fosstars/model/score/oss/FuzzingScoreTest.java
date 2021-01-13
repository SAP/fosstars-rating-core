package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
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

public class FuzzingScoreTest {

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutLanguage() {
    new FuzzingScore().calculate(LANGUAGES.value(Languages.of(CPP)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutOssFuzz() {
    new FuzzingScore().calculate(FUZZED_IN_OSS_FUZZ.unknown());
  }

  @Test
  public void testWithAllUnknown() {
    assertScore(
        Score.INTERVAL,
        new FuzzingScore(),
        setOf(
            LANGUAGES.unknown(),
            FUZZED_IN_OSS_FUZZ.unknown()));
  }

  @Test
  public void testVerification() throws VerificationFailedException, IOException {
    TestVectors vectors = new TestVectors(
        newTestVector()
            .alias("1")
            .expectedScore(Score.INTERVAL)
            .set(LANGUAGES.unknown())
            .set(FUZZED_IN_OSS_FUZZ.unknown())
            .make()
    );

    Path file = Files.createTempFile(getClass().getName(), "test");
    try {
      vectors.storeToYaml(file);

      ScoreVerification verification = new ScoreVerification(
          new FuzzingScore(),
          TestVectors.loadFromYaml(file));

      verification.run();
    } finally {
      Files.delete(file);
    }
  }
}