package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_BANDIT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_BANDIT_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class BanditScoreTest {

  private static final BanditScore SCORE = new BanditScore();

  @Test
  public void testBasics() {
    assertFalse(SCORE.name().isEmpty());
    assertEquals(3, SCORE.features().size());
    assertTrue(SCORE.features().contains(USES_BANDIT_SCAN_CHECKS));
    assertTrue(SCORE.features().contains(RUNS_BANDIT_SCANS));
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
            USES_BANDIT_SCAN_CHECKS.value(true),
            RUNS_BANDIT_SCANS.value(true),
            LANGUAGES.value(Languages.of(JAVA))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutUsesBanditChecksValue() {
    SCORE.calculate(RUNS_BANDIT_SCANS.unknown(), LANGUAGES.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutRunsBanditScanChecksValue() {
    SCORE.calculate(USES_BANDIT_SCAN_CHECKS.unknown(), LANGUAGES.unknown());
  }

  @Test
  public void testCalculateWithAllUnknownValues() {
    assertTrue(SCORE.calculate(
        USES_BANDIT_SCAN_CHECKS.unknown(),
        RUNS_BANDIT_SCANS.unknown(),
        LANGUAGES.unknown()).isUnknown());
  }
}