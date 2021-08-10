package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
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

public class CodeqlScoreTest {

  private static final CodeqlScore SCORE = new CodeqlScore();

  @Test
  public void testBasics() {
    assertFalse(SCORE.name().isEmpty());
    assertEquals(4, SCORE.features().size());
    assertTrue(SCORE.features().contains(USES_LGTM_CHECKS));
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
            USES_LGTM_CHECKS.value(true),
            RUNS_CODEQL_SCANS.value(true),
            LANGUAGES.value(Languages.of(JAVA))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutUsesLgtmChecksValue() {
    SCORE.calculate(USES_CODEQL_CHECKS.unknown(), RUNS_CODEQL_SCANS.unknown(), LANGUAGES.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutUsesCodeqlChecksValue() {
    SCORE.calculate(USES_LGTM_CHECKS.unknown(), RUNS_CODEQL_SCANS.unknown(), LANGUAGES.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutRunsCodeqlChecksValue() {
    SCORE.calculate(USES_CODEQL_CHECKS.unknown(), USES_LGTM_CHECKS.unknown(), LANGUAGES.unknown());
  }
}