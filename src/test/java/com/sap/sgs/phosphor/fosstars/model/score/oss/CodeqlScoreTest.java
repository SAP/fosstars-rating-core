package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.sgs.phosphor.fosstars.model.value.Language.JAVA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.value.Languages;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class CodeqlScoreTest {

  @Test
  public void smokeTest() {
    CodeqlScore score = new CodeqlScore();
    assertFalse(score.name().isEmpty());
    assertEquals(4, score.features().size());
    assertTrue(score.features().contains(USES_LGTM_CHECKS));
    assertTrue(score.features().contains(USES_CODEQL_CHECKS));
    assertTrue(score.features().contains(RUNS_CODEQL_SCANS));
    assertTrue(score.features().contains(LANGUAGES));
    assertTrue(score.subScores().isEmpty());
  }

  @Test
  public void testCalculate() {
    CodeqlScore score = new CodeqlScore();
    ScoreValue scoreValue = score.calculate(
        USES_CODEQL_CHECKS.unknown(),
        USES_LGTM_CHECKS.unknown(),
        RUNS_CODEQL_SCANS.unknown(),
        LANGUAGES.unknown());
    assertTrue(scoreValue.isUnknown());

    assertScore(
        Score.INTERVAL,
        score,
        setOf(
            USES_CODEQL_CHECKS.value(true),
            USES_LGTM_CHECKS.value(true),
            RUNS_CODEQL_SCANS.value(true),
            LANGUAGES.value(Languages.of(JAVA))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutUsesLgtmChecksValue() {
    new CodeqlScore().calculate(
        USES_CODEQL_CHECKS.unknown(), RUNS_CODEQL_SCANS.unknown(), LANGUAGES.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutUsesCodeqlChecksValue() {
    new CodeqlScore().calculate(
        USES_LGTM_CHECKS.unknown(), RUNS_CODEQL_SCANS.unknown(), LANGUAGES.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutRunsCodeqlChecksValue() {
    new CodeqlScore().calculate(
        USES_CODEQL_CHECKS.unknown(), USES_LGTM_CHECKS.unknown(), LANGUAGES.unknown());
  }
}