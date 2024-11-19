package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_PYLINT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_PYLINT_SCAN_CHECKS;
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

public class PylintScoreTest {

  private static final PylintScore SCORE = new PylintScore();

  @Test
  public void testBasics() {
    assertFalse(SCORE.name().isEmpty());
    assertEquals(3, SCORE.features().size());
    assertTrue(SCORE.features().contains(USES_PYLINT_SCAN_CHECKS));
    assertTrue(SCORE.features().contains(RUNS_PYLINT_SCANS));
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
            USES_PYLINT_SCAN_CHECKS.value(true),
            RUNS_PYLINT_SCANS.value(true),
            LANGUAGES.value(Languages.of(JAVA))));
  }

  @Test
  public void testCalculateWithoutUsesPylintChecksValue() {
    assertThrows(
        IllegalArgumentException.class,
        () -> SCORE.calculate(RUNS_PYLINT_SCANS.unknown(), LANGUAGES.unknown()));
  }

  @Test
  public void testCalculateWithoutRunsPylintScanChecksValue() {
    assertThrows(
        IllegalArgumentException.class,
        () -> SCORE.calculate(USES_PYLINT_SCAN_CHECKS.unknown(), LANGUAGES.unknown()));
  }

  @Test
  public void testCalculateWithAllUnknownValues() {
    assertTrue(
        SCORE
            .calculate(
                USES_PYLINT_SCAN_CHECKS.unknown(), RUNS_PYLINT_SCANS.unknown(), LANGUAGES.unknown())
            .isUnknown());
  }
}
