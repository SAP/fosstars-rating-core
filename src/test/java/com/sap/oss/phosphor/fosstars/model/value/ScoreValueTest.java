package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.SECURITY_TESTING_SCORE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

public class ScoreValueTest {

  private static final double ACCURACY = 0.01;

  @Test
  public void testIncrease() {
    ScoreValue value = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE);
    Assert.assertEquals(Score.MIN, value.get(), ACCURACY);
    value.increase(2.1);
    assertEquals(2.1, value.get(), ACCURACY);
    value.increase(4.9);
    assertEquals(7.0, value.get(), ACCURACY);
    value.increase(20.0);
    assertEquals(Score.MAX, value.get(), ACCURACY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIncreaseNegative() {
    new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).increase(-1.0);
  }

  @Test
  public void testDecrease() {
    ScoreValue value = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 1.0, 10.0, Collections.emptyList());
    assertEquals(5.0, value.get(), ACCURACY);
    value.decrease(2.3);
    assertEquals(2.7, value.get(), ACCURACY);
    value.decrease(1.1);
    assertEquals(1.6, value.get(), ACCURACY);
    value.decrease(5.0);
    assertEquals(Score.MIN, value.get(), ACCURACY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDecreaseNegative() {
    new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).decrease(-1.0);
  }

  @Test
  public void testConfidence() {
    ScoreValue value = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 1.0, 10.0, Collections.emptyList());
    assertEquals(10.0, value.confidence(), ACCURACY);
    value.confidence(5.1);
    assertEquals(5.1, value.confidence(), ACCURACY);
  }

  @Test
  public void testUsedValues() {
    List<Value<?>> usedValues = Arrays.asList(
        NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(10),
        NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(3));

    ScoreValue scoreValue = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 1.0, 10.0, usedValues);

    assertNotNull(scoreValue.usedValues());
    assertEquals(2, scoreValue.usedValues().size());
    assertEquals(
        scoreValue.usedValues().get(0),
        NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(10));
    assertEquals(
        scoreValue.usedValues().get(1),
        NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(3));
  }

  @Test
  public void testWeight() {
    ScoreValue value = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 0.7, 10.0, Collections.emptyList());
    assertEquals(0.7, value.weight(), 0.01);
    value.weight(0.42);
    assertEquals(0.42, value.weight(), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeWeight() {
    new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 0.7, 10.0, Collections.emptyList())
        .weight(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testZeroWeight() {
    new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 0.7, 10.0, Collections.emptyList())
        .weight(0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTooBigWeight() {
    new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 0.7, 10.0, Collections.emptyList())
        .weight(1.1);
  }

  @Test
  public void testExplanation() {
    List<String> notes = new ArrayList<>();
    notes.add("first note");
    notes.add("second note");

    ScoreValue value = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 1.0, 10.0,
        Collections.emptyList(), notes, false, false);
    assertNotNull(value.explanation());
    assertEquals(2, value.explanation().size());
    assertTrue(value.explanation().containsAll(notes));

    // check if the explanation can't be modified
    notes.add("new note");
    assertEquals(2, value.explanation().size());

    value.explain("third note");
    assertEquals(3, value.explanation().size());
    assertTrue(value.explanation().contains("third note"));
  }

  @Test
  public void testEqualsAndHashCode() {
    List<Value<?>> usedValues = Arrays.asList(
        NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(10),
        NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(3));

    ScoreValue one = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 1.0, 10.0, usedValues);

    ScoreValue two = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 1.0, 10.0, usedValues);
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());

    ScoreValue three = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 1.0, 10.0, usedValues);
    assertNotEquals(one, three);
    assertNotEquals(one.hashCode(), three.hashCode());

    ScoreValue four = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 1.0, 9.0, usedValues);
    assertNotEquals(one, four);
    assertNotEquals(one.hashCode(), four.hashCode());

    ScoreValue five = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 1.0, 10.0, Collections.emptyList());
    assertNotEquals(one, five);
    assertNotEquals(one.hashCode(), five.hashCode());
  }

  @Test
  public void testJsonSerialization() throws IOException {
    List<Value<?>> usedValues = Arrays.asList(
        NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(10),
        NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(3));

    ScoreValue valueWithExplanation = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 1.0, 7.2, usedValues);
    valueWithExplanation.explain("this is an explanation");
    ScoreValue clone = Json.read(Json.toBytes(valueWithExplanation), ScoreValue.class);
    assertEquals(valueWithExplanation, clone);
    assertEquals(valueWithExplanation.hashCode(), clone.hashCode());
    assertEquals(1, clone.explanation().size());
    assertEquals("this is an explanation", clone.explanation().get(0));

    ScoreValue valueWithoutExplanation = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 1.0, 7.2, usedValues);

    assertNotEquals(valueWithExplanation, valueWithoutExplanation);

    clone = Json.read(Json.toBytes(valueWithoutExplanation), ScoreValue.class);
    assertEquals(valueWithoutExplanation, clone);
    assertEquals(valueWithoutExplanation.hashCode(), clone.hashCode());
    assertTrue(valueWithoutExplanation.explanation().isEmpty());
  }

  @Test
  public void testJsonSerializationWithUnknownScoreValue() throws IOException {
    ScoreValue value = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).makeUnknown();

    byte[] bytes = Json.toBytes(value);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    ScoreValue clone = Json.read(bytes, ScoreValue.class);
    assertNotNull(clone);
    assertTrue(clone.isUnknown());
    assertEquals(value, clone);
  }

  @Test
  public void testOrElse() {
    ScoreValue value = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).set(5.1);
    assertEquals(5.1, value.orElse(1.2), ACCURACY);

    Value<Double> unknown = UnknownValue.of(PROJECT_ACTIVITY_SCORE_EXAMPLE);
    assertEquals(3.0, unknown.orElse(3.0), ACCURACY);

    Value<Double> notApplicable = NotApplicableValue.of(PROJECT_ACTIVITY_SCORE_EXAMPLE);
    assertEquals(3.0, notApplicable.orElse(3.0), ACCURACY);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetWithUnknownValue() {
    new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).makeUnknown().get();
  }

  @Test
  public void testUsedFeatureValues() {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 7));
    values.add(new IntegerValue(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 2));
    values.add(new BooleanValue(SECURITY_REVIEW_DONE_EXAMPLE, true));
    values.add(new BooleanValue(STATIC_CODE_ANALYSIS_DONE_EXAMPLE, false));

    Rating rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);
    ScoreValue scoreValue = rating.calculate(values).scoreValue();
    assertEquals(2, scoreValue.usedValues().size());
    assertEquals(4, scoreValue.usedFeatureValues().size());
    assertTrue(scoreValue.usedFeatureValues().containsAll(values));
  }

  @Test
  public void testFindUsedSubScoreValue() {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 7));
    values.add(new IntegerValue(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 2));
    values.add(new BooleanValue(SECURITY_REVIEW_DONE_EXAMPLE, true));
    values.add(new BooleanValue(STATIC_CODE_ANALYSIS_DONE_EXAMPLE, false));

    Rating rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);
    ScoreValue scoreValue = rating.calculate(values).scoreValue();

    assertEquals(2, scoreValue.usedValues().size());
    assertTrue(
        scoreValue.findUsedSubScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE.getClass()).isPresent());
    assertTrue(
        scoreValue.findUsedSubScoreValue(SECURITY_TESTING_SCORE_EXAMPLE.getClass()).isPresent());
  }

}