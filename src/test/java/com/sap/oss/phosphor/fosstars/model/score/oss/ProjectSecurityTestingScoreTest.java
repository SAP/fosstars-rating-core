package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class ProjectSecurityTestingScoreTest {

  private static final ProjectSecurityTestingScore SCORE = new ProjectSecurityTestingScore();

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithNothing() {
    SCORE.calculate();
  }

  @Test
  public void testCalculateWithAllUnknown() {
    ScoreValue scoreValue = SCORE.calculate(Utils.allUnknown(SCORE.allFeatures()));
    assertTrue(scoreValue.isUnknown());
    assertEquals(Confidence.MIN, scoreValue.confidence(), DELTA);
  }

  @Test
  public void testCalculate() {
    assertScore(
        Score.INTERVAL,
        SCORE,
        setOf(
            SCORE.score(StaticAnalysisScore.class).value(Score.MIN),
            SCORE.score(DependencyScanScore.class).value(Score.MIN),
            SCORE.score(NoHttpToolScore.class).value(Score.MIN),
            SCORE.score(MemorySafetyTestingScore.class).value(Score.MIN),
            SCORE.score(FuzzingScore.class).value(Score.MIN)));

    assertScore(
        Score.INTERVAL,
        SCORE,
        setOf(
            SCORE.score(StaticAnalysisScore.class).value(Score.MAX),
            SCORE.score(DependencyScanScore.class).value(Score.MAX),
            SCORE.score(NoHttpToolScore.class).value(Score.MAX),
            SCORE.score(MemorySafetyTestingScore.class).value(Score.MAX),
            SCORE.score(FuzzingScore.class).value(Score.MAX)));
  }

  @Test
  public void testExplanation() {
    ScoreValue value = SCORE.calculate(
        SCORE.score(StaticAnalysisScore.class).value(Score.MAX / 3),
        SCORE.score(DependencyScanScore.class).value(Score.MAX / 5),
        SCORE.score(NoHttpToolScore.class).value(Score.MAX / 2),
        SCORE.score(MemorySafetyTestingScore.class).value(Score.MAX / 4),
        SCORE.score(FuzzingScore.class).value(Score.MIN / 2));

    assertTrue(value.score().description().isEmpty());
    assertTrue(value.explanation().isEmpty());
  }

  @Test
  public void testEqualsAndHashCode() {
    ProjectSecurityTestingScore one = new ProjectSecurityTestingScore();
    ProjectSecurityTestingScore two = new ProjectSecurityTestingScore();
    assertEquals(one, one);
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());
  }

}