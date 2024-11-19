package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.jupiter.api.Test;

public class CodeqlScoreTest {

  private static final CodeqlScore SCORE = new CodeqlScore();

  @Test
  public void testBasics() {
    assertFalse(SCORE.name().isEmpty());
    assertEquals(3, SCORE.features().size());
    assertTrue(SCORE.features().contains(USES_CODEQL_CHECKS));
    assertTrue(SCORE.features().contains(RUNS_CODEQL_SCANS));
    assertTrue(SCORE.features().contains(LANGUAGES));
    assertTrue(SCORE.subScores().isEmpty());
  }

  @Test
  public void testWithAllUnknown() {
    ScoreValue scoreValue = SCORE.calculate(Utils.allUnknown(SCORE.allFeatures()));
    assertTrue(scoreValue.isUnknown());
  }

  @Test
  public void testCalculate() {
    assertScore(
        Score.INTERVAL,
        SCORE,
        setOf(
            USES_CODEQL_CHECKS.value(true),
            RUNS_CODEQL_SCANS.value(true),
            LANGUAGES.value(Languages.of(JAVA))));
  }

  @Test
  public void testCalculateWithoutUsesCodeqlChecksValue() {
    assertThrows(
        IllegalArgumentException.class,
        () -> SCORE.calculate(RUNS_CODEQL_SCANS.unknown(), LANGUAGES.unknown()));
  }

  @Test
  public void testCalculateWithoutRunsCodeqlChecksValue() {
    assertThrows(
        IllegalArgumentException.class,
        () -> SCORE.calculate(USES_CODEQL_CHECKS.unknown(), LANGUAGES.unknown()));
  }
}
