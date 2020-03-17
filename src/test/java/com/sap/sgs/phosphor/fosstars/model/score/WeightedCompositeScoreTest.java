package com.sap.sgs.phosphor.fosstars.model.score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Parameter;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import com.sap.sgs.phosphor.fosstars.model.feature.DoubleFeature;
import com.sap.sgs.phosphor.fosstars.model.score.WeightedCompositeScore.WeightedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.weight.MutableWeight;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class WeightedCompositeScoreTest {

  private static final double PRECISION = 0.001;

  @Test
  public void smokeTest() {
    WeightedScoreImpl score = new WeightedScoreImpl();
    assertEquals(WeightedScoreImpl.NAME, score.name());
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
  public void calculate() {
    Score score = new WeightedScoreImpl();

    Value<Double> firstValue = FirstScore.FEATURE.value(FirstScore.VALUE);
    Value<Double> secondValue = SecondScore.FEATURE.value(SecondScore.VALUE);

    double firstScoreValue = new FirstScore().calculate(firstValue).get();
    double secondScoreValue = new SecondScore().calculate(secondValue).get();
    double weightedFirstScore = 0.8 * firstScoreValue;
    double weightedSecondScore = 0.3 * secondScoreValue;
    double weightSum = 0.8 + 0.3;
    double expectedScore = (weightedFirstScore + weightedSecondScore) / weightSum;

    ScoreValue scoreValue = score.calculate(firstValue, secondValue);
    assertEquals(expectedScore, scoreValue.get(), PRECISION);
    assertEquals(Confidence.MAX, scoreValue.confidence(), PRECISION);
    assertEquals(2, scoreValue.usedValues().size());
    for (Value value : scoreValue.usedValues()) {
      if (value.feature() instanceof FirstScore) {
        assertEquals(firstScoreValue, value.get());
        continue;
      }
      if (value.feature() instanceof SecondScore) {
        assertEquals(secondScoreValue, value.get());
        continue;
      }
      fail("Unexpected score: " + value.feature().name());
    }
  }

  @Test
  public void equalsAndHashCode() {
    Score one = new WeightedScoreImpl();
    Score two = new WeightedScoreImpl();
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
    assertNotEquals(null, one);
    assertEquals(one, one);
  }

  @Test
  public void equalWeightedScores() {
    MutableWeight mutableWeightOne = new MutableWeight(0.7);
    MutableWeight mutableWeightTwo = new MutableWeight(0.7);
    assertEquals(mutableWeightOne, mutableWeightTwo);

    WeightedCompositeScore.WeightedScore one = new WeightedCompositeScore.WeightedScore(
        new FirstScore(), mutableWeightOne);
    WeightedCompositeScore.WeightedScore two = new WeightedCompositeScore.WeightedScore(
        new FirstScore(), mutableWeightTwo);
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());

    // check if WeightedScore considers only scores in equals() and hashCode()
    mutableWeightOne.value(0.5);
    assertNotEquals(mutableWeightOne, mutableWeightTwo);
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void validValue() {
    Score score = new WeightedScoreImpl();
    Value<Double> value = score.value(7.54);
    assertNotNull(value);
    assertEquals(7.54, value.get(), PRECISION);
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeValue() {
    new WeightedScoreImpl().value(-3.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void tooBigValue() {
    new WeightedScoreImpl().value(42.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void zeroWeights() {
    MutableWeight mutableWeightOne = new MutableWeight(0);
    MutableWeight mutableWeightTwo = new MutableWeight(0);

    WeightedCompositeScore.WeightedScore one = new WeightedCompositeScore.WeightedScore(
        new FirstScore(), mutableWeightOne);
    WeightedCompositeScore.WeightedScore two = new WeightedCompositeScore.WeightedScore(
        new SecondScore(), mutableWeightTwo);

    Set<WeightedScore> weightedScores = new HashSet<>();
    weightedScores.add(one);
    weightedScores.add(two);

    WeightedCompositeScore score = new WeightedCompositeScore("test", weightedScores);
    score.calculate();
  }

  @Test(expected = NullPointerException.class)
  public void nullName() {
    new WeightedCompositeScore(null, new FirstScore());
  }

  @Test(expected = NullPointerException.class)
  public void nullScoreList() {
    new WeightedCompositeScore("test", (Score[]) null);
  }

  @Test(expected = NullPointerException.class)
  public void nullScoreSet() {
    new WeightedCompositeScore("test", (Set<WeightedScore>) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyScoreList() {
    new WeightedCompositeScore("test", new Score[0]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void emptyScoreSet() {
    new WeightedCompositeScore("test", new HashSet<>());
  }

  @Test
  public void allScoresPreCalculated() {
    Score score = new WeightedScoreImpl();

    double firstValue = 3.0;
    double secondValue = 2.0;

    final double firstConfidence = 7.3;
    final double secondConfidence = 6.1;

    ScoreValue firstPreCalculatedScoreValue = new ScoreValue(
        new FirstScore(),
        firstValue,
        WeightedScoreImpl.FIRST_WEIGHT,
        firstConfidence,
        Collections.emptyList());
    ScoreValue secondPreCalculatedScoreValue = new ScoreValue(
        new SecondScore(),
        secondValue,
        WeightedScoreImpl.SECOND_WEIGHT,
        secondConfidence,
        Collections.emptyList());
    ScoreValue scoreValue = score.calculate(
        firstPreCalculatedScoreValue, secondPreCalculatedScoreValue);
    assertNotNull(scoreValue);

    final double weightSum = WeightedScoreImpl.FIRST_WEIGHT + WeightedScoreImpl.SECOND_WEIGHT;

    double weightedValueSum = firstValue * WeightedScoreImpl.FIRST_WEIGHT
        + secondValue * WeightedScoreImpl.SECOND_WEIGHT;
    double expectedScore = weightedValueSum / weightSum;
    assertEquals(expectedScore, scoreValue.get(), PRECISION);

    double weightedConfidenceSum = firstConfidence * WeightedScoreImpl.FIRST_WEIGHT
        + secondConfidence * WeightedScoreImpl.SECOND_WEIGHT;
    double expectedConfidence = weightedConfidenceSum / weightSum;
    assertEquals(expectedConfidence, scoreValue.confidence(), PRECISION);
  }

  @Test
  public void oneScorePreCalculated() {
    Score score = new WeightedScoreImpl();

    final double firstValue = 3.0;
    final double firstConfidence = 7.2;

    ScoreValue preCalculatedScoreValue = new ScoreValue(
        new FirstScore(),
        firstValue,
        WeightedScoreImpl.FIRST_WEIGHT,
        firstConfidence,
        Collections.emptyList());
    ScoreValue scoreValue = score.calculate(preCalculatedScoreValue);
    assertNotNull(scoreValue);

    final double weightSum = WeightedScoreImpl.FIRST_WEIGHT + WeightedScoreImpl.SECOND_WEIGHT;

    double weightedValueSum = firstValue * WeightedScoreImpl.FIRST_WEIGHT
        + SecondScore.VALUE * WeightedScoreImpl.SECOND_WEIGHT;
    double expectedScore = weightedValueSum / weightSum;
    assertEquals(expectedScore, scoreValue.get(), PRECISION);

    double weightedConfidenceSum = firstConfidence * WeightedScoreImpl.FIRST_WEIGHT
        + Confidence.MAX * WeightedScoreImpl.SECOND_WEIGHT;
    double expectedConfidence = weightedConfidenceSum / weightSum;
    assertEquals(expectedConfidence, scoreValue.confidence(), PRECISION);
  }

  @Test
  public void parameters() {
    WeightedScoreImpl score = new WeightedScoreImpl();
    Set<Score> subScores = score.subScores();
    assertNotNull(subScores);
    assertEquals(2, subScores.size());

    List<Weight> weights = score.parameters();
    assertNotNull(weights);
    assertEquals(2, weights.size());

    double weightSum = 0.0;
    for (Weight weight : weights) {
      weightSum += weight.value();
    }
    assertEquals(
        WeightedScoreImpl.FIRST_WEIGHT + WeightedScoreImpl.SECOND_WEIGHT,
        weightSum,
        PRECISION);
  }

  @Test
  public void notImmutableByDefault() {
    WeightedScoreImpl score = new WeightedScoreImpl();
    assertFalse(score.isImmutable());
    for (Parameter parameter : score.parameters()) {
      final double w = 1.0;
      assertNotEquals(w, parameter.value());
      parameter.value(w);
      assertEquals(w, parameter.value(), PRECISION);
    }
  }

  @Test
  public void makeImmutable() {
    WeightedScoreImpl score = new WeightedScoreImpl();
    score.makeImmutable();
    assertTrue(score.isImmutable());
    for (Parameter parameter : score.parameters()) {
      try {
        parameter.value(0.1);
        fail("Oh no! The parameter should not be modifiable!");
      } catch (UnsupportedOperationException e) {
        // expected
      }
    }
  }

  @Test
  public void description() {
    assertNotNull(new WeightedScoreImpl().description());
    assertTrue(new WeightedScoreImpl().description().isEmpty());
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

  private static class WeightedScoreImpl extends WeightedCompositeScore {

    private static final String NAME = "Test score";

    private static final double FIRST_WEIGHT = 0.8;
    private static final double SECOND_WEIGHT = 0.3;

    WeightedScoreImpl() {
      super(NAME, init());
    }

    private static Set<WeightedScore> init() {
      Set<WeightedScore> scores = new HashSet<>();
      scores.add(new WeightedScore(new FirstScore(), new MutableWeight(FIRST_WEIGHT)));
      scores.add(new WeightedScore(new SecondScore(), new MutableWeight(SECOND_WEIGHT)));
      return scores;
    }
  }

}