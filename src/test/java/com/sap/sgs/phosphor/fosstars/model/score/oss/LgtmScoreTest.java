package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORSE_LGTM_GRADE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import org.junit.Test;

public class LgtmScoreTest {

  @Test
  public void smokeTest() {
    LgtmScore score = new LgtmScore();
    assertFalse(score.name().isEmpty());
    assertEquals(2, score.features().size());
    assertTrue(score.features().contains(USES_LGTM));
    assertTrue(score.features().contains(WORSE_LGTM_GRADE));
    assertTrue(score.subScores().isEmpty());
  }

  @Test
  public void testCalculate() {
    LgtmScore score = new LgtmScore();
    ScoreValue value = score.calculate(USES_LGTM.unknown(), WORSE_LGTM_GRADE.unknown());
    assertNotNull(value);
    assertTrue(Score.INTERVAL.contains(value.get()));

    value = score.calculate(USES_LGTM.value(true), WORSE_LGTM_GRADE.value(LgtmGrade.A_PLUS));
    assertNotNull(value);
    assertTrue(Score.INTERVAL.contains(value.get()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutUsesLgtmValue() {
    new LgtmScore().calculate(WORSE_LGTM_GRADE.unknown());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithoutUsesWorseLgtmGradeValue() {
    new LgtmScore().calculate(USES_LGTM.unknown());
  }
}