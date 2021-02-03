package com.sap.oss.phosphor.fosstars.model.score;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.SECURITY_SCORE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.SECURITY_TESTING_SCORE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Parameter;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.Weight;
import com.sap.oss.phosphor.fosstars.model.feature.DoubleFeature;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.weight.MutableWeight;
import com.sap.oss.phosphor.fosstars.model.weight.ScoreWeights;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

public class WeightedCompositeScoreTest {

  private static final double PRECISION = 0.001;

  @Test
  public void testBasics() {
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
  public void testCalculateWithValidValues() {
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
    for (Value<?> value : scoreValue.usedValues()) {
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
  public void testEqualsAndHashCode() {
    Score one = new WeightedScoreImpl();
    Score two = new WeightedScoreImpl();
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
    assertNotEquals(null, one);
    assertEquals(one, one);
  }

  @Test
  public void testWithValidValue() {
    Score score = new WeightedScoreImpl();
    Value<Double> value = score.value(7.54);
    assertNotNull(value);
    assertEquals(7.54, value.get(), PRECISION);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNegativeValue() {
    new WeightedScoreImpl().value(-3.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithTooBigValue() {
    new WeightedScoreImpl().value(42.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithZeroWeights() {
    ScoreWeights weights = ScoreWeights.createFor(
        PROJECT_ACTIVITY_SCORE_EXAMPLE,
        SECURITY_TESTING_SCORE_EXAMPLE);
    weights.set(PROJECT_ACTIVITY_SCORE_EXAMPLE, new MutableWeight(0));
    weights.set(SECURITY_SCORE_EXAMPLE, new MutableWeight(0));

    WeightedCompositeScore score = new WeightedCompositeScore(
        "test",
        setOf(PROJECT_ACTIVITY_SCORE_EXAMPLE, SECURITY_SCORE_EXAMPLE),
        weights);
    score.calculate();
  }

  @Test(expected = NullPointerException.class)
  public void testWithNullName() {
    new WeightedCompositeScore(null, new FirstScore());
  }

  @Test(expected = NullPointerException.class)
  public void testWithNullScoreList() {
    new WeightedCompositeScore("test", (Score[]) null);
  }

  @Test(expected = NullPointerException.class)
  public void testWithNullScoreSet() {
    new WeightedCompositeScore("test", null, ScoreWeights.createFor());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithEmptyScoreList() {
    new WeightedCompositeScore("test");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithEmptyScoreSet() {
    new WeightedCompositeScore(
        "test",
        new HashSet<>(),
        ScoreWeights.createFor(SECURITY_TESTING_SCORE_EXAMPLE));
  }

  @Test
  public void testWithPreCalculatedSubScores() {
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
  public void testWithOnePreCalculatedSubScore() {
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
  public void testParameters() {
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
  public void testThatNotImmutableByDefault() {
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
  public void testMakeImmutable() {
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
  public void testDescription() {
    assertNotNull(new WeightedScoreImpl().description());
    assertTrue(new WeightedScoreImpl().description().isEmpty());
  }

  @Test
  public void testWithOneNotApplicableSubScore() {
    WeightedScoreImpl score = new WeightedScoreImpl(
        setOf(new FirstScore().returnsNotApplicable(), new SecondScore()),
        WeightedScoreImpl.initWeights()
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
    ScoreValue subScoreValue = (ScoreValue) usedValues.get(0);
    assertTrue(subScoreValue.isNotApplicable());
    assertEquals(WeightedScoreImpl.FIRST_WEIGHT, subScoreValue.weight(), PRECISION);

    assertTrue(usedValues.get(1) instanceof ScoreValue);
    subScoreValue = (ScoreValue) usedValues.get(1);
    assertFalse(subScoreValue.isNotApplicable());
    assertEquals(SecondScore.VALUE, subScoreValue.get(), PRECISION);
    assertEquals(Confidence.MAX, subScoreValue.confidence(), PRECISION);
    assertEquals(WeightedScoreImpl.SECOND_WEIGHT, subScoreValue.weight(), PRECISION);
  }

  @Test
  public void testWithAllNotApplicableSubScores() {
    WeightedScoreImpl score = new WeightedScoreImpl(
        setOf(new FirstScore().returnsNotApplicable(), new SecondScore().returnsNotApplicable()),
        WeightedScoreImpl.initWeights()
    );

    ScoreValue scoreValue = score.calculate(
        FirstScore.FEATURE.value(5.0),
        SecondScore.FEATURE.value(8.0));

    assertFalse(scoreValue.isUnknown());
    assertTrue(scoreValue.isNotApplicable());
    assertEquals(Confidence.MAX, scoreValue.confidence(), PRECISION);
  }

  @Test
  public void testWithAllUnknownValues() {
    WeightedScoreImpl score = new WeightedScoreImpl(
        setOf(new FirstScore().returnUnknown(), new SecondScore().returnUnknown()),
        WeightedScoreImpl.initWeights()
    );

    ScoreValue scoreValue = score.calculate(
        FirstScore.FEATURE.unknown(), SecondScore.FEATURE.unknown());

    assertTrue(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(Confidence.MIN, scoreValue.confidence(), PRECISION);
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    WeightedScoreImpl score = new WeightedScoreImpl(
        setOf(SECURITY_TESTING_SCORE_EXAMPLE, PROJECT_ACTIVITY_SCORE_EXAMPLE),
        ScoreWeights.createFor(SECURITY_TESTING_SCORE_EXAMPLE, PROJECT_ACTIVITY_SCORE_EXAMPLE));
    score.weights().set(SECURITY_TESTING_SCORE_EXAMPLE, new MutableWeight(0.1));
    score.weights().set(PROJECT_ACTIVITY_SCORE_EXAMPLE, new MutableWeight(0.7));
    WeightedScoreImpl clone = Json.read(Json.toBytes(score), WeightedScoreImpl.class
    );
    assertEquals(score, clone);
  }

  private abstract static class AbstractTestScore extends FeatureBasedScore {

    private boolean returnsNotApplicable = false;
    private boolean returnUnknown = false;

    AbstractTestScore(String name, Feature<?>... features) {
      super(name, features);
    }

    AbstractTestScore returnsNotApplicable() {
      returnsNotApplicable = true;
      return this;
    }

    AbstractScore returnUnknown() {
      returnUnknown = true;
      return this;
    }

    @Override
    public ScoreValue calculate(Value<?>... values) {
      if (returnsNotApplicable) {
        return scoreValue(MIN, values)
            .confidence(Confidence.make(values))
            .makeNotApplicable();
      }
      if (returnUnknown) {
        return scoreValue(MIN, values)
            .confidence(Confidence.make(values))
            .makeUnknown();
      }
      return calculateImpl(values);
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

  private static class WeightedScoreImpl extends WeightedCompositeScore {

    private static final String NAME = "Test score";

    private static final double FIRST_WEIGHT = 0.8;
    private static final double SECOND_WEIGHT = 0.3;

    private static final FirstScore FIRST_SCORE = new FirstScore();
    private static final SecondScore SECOND_SCORE = new SecondScore();

    WeightedScoreImpl(Set<Score> scores, ScoreWeights weights) {
      super(NAME, scores, weights);
    }

    WeightedScoreImpl() {
      super(NAME, setOf(FIRST_SCORE, SECOND_SCORE), initWeights());
    }

    static ScoreWeights initWeights() {
      ScoreWeights weights = ScoreWeights.createFor(FIRST_SCORE, SECOND_SCORE);
      weights.set(FIRST_SCORE, new MutableWeight(FIRST_WEIGHT));
      weights.set(SECOND_SCORE, new MutableWeight(SECOND_WEIGHT));
      return weights;
    }

  }

}