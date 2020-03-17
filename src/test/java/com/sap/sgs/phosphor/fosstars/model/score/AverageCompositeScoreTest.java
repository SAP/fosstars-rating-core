package com.sap.sgs.phosphor.fosstars.model.score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.DoubleFeature;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class AverageCompositeScoreTest {

  private static final double PRECISION = 0.001;

  @Test
  public void smokeTest() {
    TestAverageCompositeScore score = new TestAverageCompositeScore();
    assertEquals(TestAverageCompositeScore.NAME, score.name());
    assertEquals(2, score.allFeatures().size());
    assertTrue(score.allFeatures().contains(FirstScore.FEATURE));
    assertTrue(score.allFeatures().contains(SecondScore.FEATURE));
    assertTrue(score.features().isEmpty());
    assertNotNull(score.description());
    assertTrue(score.description().isEmpty());
    assertEquals(2, score.subScores().size());
    assertTrue(score.subScores().contains(new FirstScore()));
    assertTrue(score.subScores().contains(new SecondScore()));
  }

  @Test
  public void equalsAndHashCode() {
    Score one = new TestAverageCompositeScore();
    Score two = new TestAverageCompositeScore();
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
    assertNotEquals(null, one);
    assertEquals(one, one);
  }

  @Test
  public void validValue() {
    Score score = new TestAverageCompositeScore();
    Value<Double> value = score.value(7.54);
    assertNotNull(value);
    assertEquals(7.54, value.get(), PRECISION);
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeValue() {
    new TestAverageCompositeScore().value(-3.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void tooBigValue() {
    new TestAverageCompositeScore().value(42.0);
  }

  @Test
  public void allScoresPreCalculated() {
    final Score score = new TestAverageCompositeScore();

    final double firstValue = 3.0;
    final double secondValue = 2.0;

    final double firstConfidence = 7.3;
    final double secondConfidence = 6.1;

    ScoreValue firstPreCalculatedScoreValue = new ScoreValue(new FirstScore());
    firstPreCalculatedScoreValue.set(firstValue);
    firstPreCalculatedScoreValue.confidence(firstConfidence);

    ScoreValue secondPreCalculatedScoreValue = new ScoreValue(new SecondScore());
    secondPreCalculatedScoreValue.set(secondValue);
    secondPreCalculatedScoreValue.confidence(secondConfidence);

    ScoreValue scoreValue = score.calculate(
        firstPreCalculatedScoreValue,
        secondPreCalculatedScoreValue);

    assertNotNull(scoreValue);

    double expectedScore = (firstValue + secondValue) / 2;
    double expectedConfidence = (firstConfidence + secondConfidence) / 2;

    assertEquals(expectedScore, scoreValue.get(), PRECISION);
    assertEquals(expectedConfidence, scoreValue.confidence(), PRECISION);
  }

  @Test
  public void oneScorePreCalculated() {
    Score score = new TestAverageCompositeScore();

    final double firstValue = 3.0;
    final double firstConfidence = 7.2;

    ScoreValue preCalculatedScoreValue = new ScoreValue(new FirstScore());
    preCalculatedScoreValue.set(firstValue);
    preCalculatedScoreValue.confidence(firstConfidence);

    ScoreValue scoreValue = score.calculate(preCalculatedScoreValue);
    assertNotNull(scoreValue);

    double expectedScore = (firstValue + SecondScore.VALUE) / 2;
    assertEquals(expectedScore, scoreValue.get(), PRECISION);

    double expectedConfidence = (firstConfidence + Confidence.MAX) / 2;
    assertEquals(expectedConfidence, scoreValue.confidence(), PRECISION);
  }

  private static class FirstScore extends FeatureBasedScore {

    private static final Feature<Double> FEATURE = new DoubleFeature("first feature");

    private static final double VALUE = 0.2;

    FirstScore() {
      super("First score", FEATURE);
    }

    @Override
    public ScoreValue calculate(Value... values) {
      return scoreValue(VALUE, values);
    }

    @Override
    public int hashCode() {
      return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return obj.getClass() == getClass();
    }
  }

  private static class SecondScore extends FeatureBasedScore {

    private static final Feature<Double> FEATURE = new DoubleFeature("second feature");

    private static final double VALUE = 0.5;

    SecondScore() {
      super("Second score", FEATURE);
    }

    @Override
    public ScoreValue calculate(Value... values) {
      return scoreValue(VALUE, values);
    }

    @Override
    public int hashCode() {
      return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return obj.getClass() == getClass();
    }
  }

  private static class TestAverageCompositeScore extends AverageCompositeScore {

    private static final String NAME = "test";

    TestAverageCompositeScore() {
      super(NAME, new FirstScore(), new SecondScore());
    }
  }

}