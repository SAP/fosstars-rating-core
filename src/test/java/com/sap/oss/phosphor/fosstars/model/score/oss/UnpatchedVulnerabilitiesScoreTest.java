package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_PROJECT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import org.junit.Test;

public class UnpatchedVulnerabilitiesScoreTest {

  private static final UnpatchedVulnerabilitiesScore SCORE = new UnpatchedVulnerabilitiesScore();

  @Test
  public void testCalculateForAllUnknown() {
    assertTrue(SCORE.calculate(Utils.allUnknown(SCORE.allFeatures())).isUnknown());
  }

  @Test
  public void testCalculate() {
    assertScore(
        Score.INTERVAL,
        SCORE,
        setOf(VULNERABILITIES_IN_PROJECT.value(new Vulnerabilities())));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutInfoAboutVulnerabilities() {
    assertScore(Score.INTERVAL, SCORE, setOf());
  }

  @Test
  public void testExplanation() {
    assertNotNull(SCORE.description());
    assertTrue(SCORE.description().isEmpty());
    ScoreValue value = SCORE.calculate(VULNERABILITIES_IN_PROJECT.value(new Vulnerabilities()));
    assertNotNull(value);
    assertFalse(value.explanation().isEmpty());
  }
}