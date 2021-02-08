package com.sap.oss.phosphor.fosstars.model.score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.DoubleFeature;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Comparator;
import java.util.List;
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

  @Test
  public void testWithOneNotApplicable() {
    TestAverageCompositeScore score = new TestAverageCompositeScore(
        new FirstScore().returnsNotApplicable(),
        new SecondScore()
    );

    ScoreValue scoreValue = score.calculate(
        FirstScore.FEATURE.value(5.0),
        SecondScore.FEATURE.value(8.0));

    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(Confidence.MAX, scoreValue.confidence(), PRECISION);

    List<Value<?>> usedValues = scoreValue.usedValues();
    usedValues.sort(Comparator.comparing(a -> a.feature().name()));

    assertEquals(2, usedValues.size());

    assertTrue(usedValues.get(0) instanceof ScoreValue);
    assertTrue(usedValues.get(0).isNotApplicable());

    assertTrue(usedValues.get(1) instanceof ScoreValue);
    ScoreValue subScoreValue = (ScoreValue) usedValues.get(1);
    assertEquals(SecondScore.VALUE, subScoreValue.get(), PRECISION);
    assertEquals(Confidence.MAX, subScoreValue.confidence(), PRECISION);
  }

  @Test
  public void testWithAllNotApplicable() {
    TestAverageCompositeScore score = new TestAverageCompositeScore(
        new FirstScore().returnsNotApplicable(),
        new SecondScore().returnsNotApplicable()
    );

    ScoreValue scoreValue = score.calculate(
        FirstScore.FEATURE.value(5.0),
        SecondScore.FEATURE.value(8.0));

    assertFalse(scoreValue.isUnknown());
    assertTrue(scoreValue.isNotApplicable());
  }

  private abstract static class AbstractTestScore extends FeatureBasedScore {

    private boolean returnsNotApplicable = false;

    AbstractTestScore(String name, Feature<?>... features) {
      super(name, features);
    }

    AbstractTestScore returnsNotApplicable() {
      returnsNotApplicable = true;
      return this;
    }

    @Override
    public ScoreValue calculate(Value<?>... values) {
      ScoreValue value = calculateImpl(values);
      if (returnsNotApplicable) {
        return value.makeNotApplicable();
      }
      return value;
    }

    abstract ScoreValue calculateImpl(Value<?>... values);

    @Override
    public int hashCode() {
      return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return obj.getClass() == getClass();
    }
  }

  private static class FirstScore extends AbstractTestScore {

    private static final Feature<Double> FEATURE = new DoubleFeature("first feature");

    private static final double VALUE = 0.2;

    FirstScore() {
      super("First score", FEATURE);
    }

    @Override
    public ScoreValue calculateImpl(Value<?>... values) {
      return scoreValue(VALUE, values).confidence(Confidence.MAX);
    }
  }

  private static class SecondScore extends AbstractTestScore {

    private static final Feature<Double> FEATURE = new DoubleFeature("second feature");

    private static final double VALUE = 0.5;

    SecondScore() {
      super("Second score", FEATURE);
    }

    @Override
    public ScoreValue calculateImpl(Value<?>... values) {
      return scoreValue(VALUE, values).confidence(Confidence.MAX);
    }
  }

  private static class TestAverageCompositeScore extends AverageCompositeScore {

    private static final String NAME = "test";

    TestAverageCompositeScore(Score... scores) {
      super(NAME, scores);
    }

    TestAverageCompositeScore() {
      super(NAME, new FirstScore(), new SecondScore());
    }
  }

}