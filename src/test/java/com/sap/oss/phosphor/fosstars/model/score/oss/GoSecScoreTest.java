package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_GOSEC_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GOSEC_WITH_RULES;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.GO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class GoSecScoreTest {

  private static final GoSecScore SCORE = new GoSecScore();

  @Test
  public void testBasics() {
    assertFalse(SCORE.name().isEmpty());
    assertEquals(4, SCORE.features().size());
    assertTrue(SCORE.features().contains(USES_GOSEC_SCAN_CHECKS));
    assertTrue(SCORE.features().contains(USES_GOSEC_WITH_RULES));
    assertTrue(SCORE.features().contains(RUNS_GOSEC_SCANS));
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
            USES_GOSEC_SCAN_CHECKS.value(true),
            USES_GOSEC_WITH_RULES.value(true),
            RUNS_GOSEC_SCANS.value(true),
            LANGUAGES.value(Languages.of(GO))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutUsesGoSecChecksValue() {
    SCORE.calculate(RUNS_GOSEC_SCANS.unknown(), LANGUAGES.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutRunsGoSecScanChecksValue() {
    SCORE.calculate(USES_GOSEC_SCAN_CHECKS.unknown(), LANGUAGES.unknown());
  }

  @Test
  public void testCalculateWithAllUnknownValues() {
    assertTrue(SCORE.calculate(
        USES_GOSEC_SCAN_CHECKS.unknown(),
        USES_GOSEC_WITH_RULES.unknown(),
        RUNS_GOSEC_SCANS.unknown(),
        LANGUAGES.unknown()).isUnknown());
  }
}