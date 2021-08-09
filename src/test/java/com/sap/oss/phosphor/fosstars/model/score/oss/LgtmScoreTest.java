package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.LANGUAGES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.value.Language.JAVA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class LgtmScoreTest {

  private static final LgtmScore SCORE = new LgtmScore();

  @Test
  public void testCalculate() {
    ScoreValue scoreValue = SCORE.calculate(WORST_LGTM_GRADE.unknown(), LANGUAGES.unknown());
    assertTrue(scoreValue.isUnknown());

    assertFalse(SCORE.name().isEmpty());
    assertEquals(2, SCORE.features().size());
    assertTrue(SCORE.features().contains(WORST_LGTM_GRADE));
    assertTrue(SCORE.features().contains(LANGUAGES));
    assertTrue(SCORE.subScores().isEmpty());

    assertScore(Score.INTERVAL, SCORE,
        setOf(WORST_LGTM_GRADE.value(LgtmGrade.A_PLUS), LANGUAGES.value(Languages.of(JAVA))));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutUsesWorseLgtmGradeValue() {
    SCORE.calculate(LANGUAGES.unknown());
  }
}