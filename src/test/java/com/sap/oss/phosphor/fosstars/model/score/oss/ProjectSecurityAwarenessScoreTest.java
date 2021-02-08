package com.sap.oss.phosphor.fosstars.model.score.oss;

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

  private static final ProjectSecurityAwarenessScore PROJECT_SECURITY_AWARENESS_SCORE
      = new ProjectSecurityAwarenessScore();

  private static final double ACCURACY = 0.001;

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutEnoughInfo() {
    Set<Value<?>> values = Utils.allUnknown(PROJECT_SECURITY_AWARENESS_SCORE.allFeatures());
    values.remove(HAS_SECURITY_TEAM.unknown());
    PROJECT_SECURITY_AWARENESS_SCORE.calculate(values);
  }

  @Test
  public void testDescription() {
    assertNotNull(PROJECT_SECURITY_AWARENESS_SCORE.description());
    assertFalse(PROJECT_SECURITY_AWARENESS_SCORE.description().isEmpty());
  }

  @Test
  public void testCalculate() {
    Set<Value<?>> values = Utils.allUnknown(PROJECT_SECURITY_AWARENESS_SCORE.allFeatures());
    ScoreValue scoreValue = PROJECT_SECURITY_AWARENESS_SCORE.calculate(values);
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(Confidence.MIN, scoreValue.confidence(), ACCURACY);
    assertEquals(values.size(), scoreValue.usedValues().size());
    scoreValue.usedValues().forEach(usedValue -> assertTrue(values.contains(usedValue)));
  }

}