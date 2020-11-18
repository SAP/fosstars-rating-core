package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class LgtmScoreTest {

  @Test
  public void smokeTest() {
    LgtmScore score = new LgtmScore();
    assertFalse(score.name().isEmpty());
    assertEquals(1, score.features().size());
    assertTrue(score.features().contains(WORST_LGTM_GRADE));
    assertTrue(score.subScores().isEmpty());
  }

  @Test
  public void testCalculate() {
    LgtmScore score = new LgtmScore();
    ScoreValue scoreValue = score.calculate(WORST_LGTM_GRADE.unknown());
    assertTrue(scoreValue.isUnknown());
    assertScore(Score.INTERVAL, score, setOf(WORST_LGTM_GRADE.value(LgtmGrade.A_PLUS)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutUsesWorseLgtmGradeValue() {
    new LgtmScore().calculate(USES_LGTM_CHECKS.unknown());
  }
}