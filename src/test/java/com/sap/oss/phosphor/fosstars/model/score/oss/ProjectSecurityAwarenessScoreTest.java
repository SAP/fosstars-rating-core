package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Set;
import org.junit.Test;

public class ProjectSecurityAwarenessScoreTest {

  private static final ProjectSecurityAwarenessScore SCORE = new ProjectSecurityAwarenessScore();

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutEnoughInfo() {
    Set<Value<?>> values = Utils.allUnknown(SCORE.allFeatures());
    values.remove(HAS_SECURITY_TEAM.unknown());
    SCORE.calculate(values);
  }

  @Test
  public void testDescription() {
    assertNotNull(SCORE.description());
    assertFalse(SCORE.description().isEmpty());
  }

  @Test
  public void testWithAllUnknown() {
    assertTrue(SCORE.calculate(Utils.allUnknown(SCORE.allFeatures())).isUnknown());
  }

  @Test
  public void testCalculate() {
    Set<Value<?>> values = OssSecurityScoreTest.defaultValues();
    ScoreValue scoreValue = SCORE.calculate(values);
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
    scoreValue.usedValues().forEach(usedValue -> assertTrue(values.contains(usedValue)));
  }

}