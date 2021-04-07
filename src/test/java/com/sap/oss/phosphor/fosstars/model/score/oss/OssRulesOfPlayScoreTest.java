package com.sap.oss.phosphor.fosstars.model.score.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.UnknownValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class OssRulesOfPlayScoreTest {

  private static final OssRulesOfPlayScore SCORE = new OssRulesOfPlayScore();

  private static final double DELTA = 0.01;

  @Test
  public void testFeatures() {
    assertFalse(SCORE.features().isEmpty());
    for (Feature<?> feature : SCORE.features()) {
      assertTrue(feature instanceof BooleanFeature);
    }
  }

  @Test
  public void testSubScores() {
    assertTrue(SCORE.subScores().isEmpty());
  }

  @Test
  public void testEqualsAndHashCode() {
    OssRulesOfPlayScore score = new OssRulesOfPlayScore();
    assertEquals(score, score);
    assertNotEquals(score, null);
    OssRulesOfPlayScore anotherScore = new OssRulesOfPlayScore();
    assertTrue(score.equals(anotherScore) && anotherScore.equals(score));
    assertEquals(score.hashCode(), anotherScore.hashCode());
  }

  @Test
  public void testSerialization() throws IOException {
    assertEquals(SCORE, Json.read(Json.toBytes(SCORE), OssRulesOfPlayScore.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithNoValues() {
    SCORE.calculate();
  }

  // the test cases below implement verification procedure for the score
  // if necessary, they may be re-written using test vectors

  @Test
  public void testCalculateWithUnknownValues() {
    Set<Value<?>> values = Utils.allUnknown(SCORE.allFeatures());
    ScoreValue scoreValue = SCORE.calculate(values);
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(Score.MAX, scoreValue.get(), DELTA);
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
    assertEquals(values.size(), scoreValue.usedValues().size());
    for (Value<?> usedValue : scoreValue.usedValues()) {
      assertTrue(values.contains(usedValue));
    }
  }

  @Test
  public void testCalculateWithAllPassedRules() {
    ScoreValue scoreValue = SCORE.calculate(allRulesPassed());
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(Score.MAX, scoreValue.get(), DELTA);
    assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
  }

  @Test
  public void testCalculateWithOneFailedRule() {
    for (Feature<?> feature : SCORE.features()) {
      assertTrue(feature instanceof BooleanFeature);
      ValueSet values = allRulesPassed();
      double expectedScore = Score.MIN;
      if (OssRulesOfPlayScore.EXPECTED_FALSE.contains(feature)) {
        values.update(new BooleanValue((BooleanFeature) feature, true));
      } else if (OssRulesOfPlayScore.EXPECTED_TRUE.contains(feature)) {
        values.update(new BooleanValue((BooleanFeature) feature, false));
      } else if (OssRulesOfPlayScore.RECOMMENDED_FALSE.contains(feature)) {
        values.update(new BooleanValue((BooleanFeature) feature, true));
        expectedScore = OssRulesOfPlayScore.SCORE_WITH_WARNING;
      } else if (OssRulesOfPlayScore.RECOMMENDED_TRUE.contains(feature)) {
        values.update(new BooleanValue((BooleanFeature) feature, false));
        expectedScore = OssRulesOfPlayScore.SCORE_WITH_WARNING;
      } else {
        fail("Unexpected feature: " + feature);
      }
      ScoreValue scoreValue = SCORE.calculate(values);
      assertFalse(scoreValue.isUnknown());
      assertFalse(scoreValue.isNotApplicable());
      assertEquals(expectedScore, scoreValue.get(), DELTA);
      assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
      assertEquals(values.size(), scoreValue.usedValues().size());
    }
  }

  @Test
  public void testCalculateWithOneUnknownValue() {
    for (Feature<?> feature : SCORE.features()) {
      assertTrue(feature instanceof BooleanFeature);
      ValueSet values = allRulesPassed().update(UnknownValue.of(feature));
      ScoreValue scoreValue = SCORE.calculate(values);
      assertFalse(scoreValue.isUnknown());
      assertFalse(scoreValue.isNotApplicable());
      assertEquals(Score.MAX, scoreValue.get(), DELTA);
      assertTrue(scoreValue.confidence() > Confidence.MIN);
      assertTrue(scoreValue.confidence() < Confidence.MAX);
      assertEquals(values.size(), scoreValue.usedValues().size());
    }
  }

  public static ValueSet allRulesPassed() {
    ValueSet values = new ValueHashSet();
    OssRulesOfPlayScore.EXPECTED_TRUE.forEach(feature -> values.update(feature.value(true)));
    OssRulesOfPlayScore.EXPECTED_FALSE.forEach(feature -> values.update(feature.value(false)));
    OssRulesOfPlayScore.RECOMMENDED_TRUE.forEach(feature -> values.update(feature.value(true)));
    OssRulesOfPlayScore.RECOMMENDED_FALSE.forEach(feature -> values.update(feature.value(false)));
    return values;
  }

}