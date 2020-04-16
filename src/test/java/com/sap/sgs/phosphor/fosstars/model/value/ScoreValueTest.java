package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class ScoreValueTest {

  private static final double ACCURACY = 0.01;

  @Test
  public void increase() {
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
  public void increaseNegative() {
    new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).increase(-1.0);
  }

  @Test
  public void decrease() {
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
  public void decreaseNegative() {
    new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).decrease(-1.0);
  }

  @Test
  public void confidence() {
    ScoreValue value = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 1.0, 10.0, Collections.emptyList());
    assertEquals(10.0, value.confidence(), ACCURACY);
    value.confidence(5.1);
    assertEquals(5.1, value.confidence(), ACCURACY);
  }

  @Test
  public void usedValues() {
    List<Value> usedValues = Arrays.asList(
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
  public void weight() {
    ScoreValue value = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 0.7, 10.0, Collections.emptyList());
    assertEquals(0.7, value.weight(), 0.01);
    value.weight(0.42);
    assertEquals(0.42, value.weight(), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeWeight() {
    new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 0.7, 10.0, Collections.emptyList())
        .weight(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void zeroWeight() {
    new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 0.7, 10.0, Collections.emptyList())
        .weight(0.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void tooBigWeight() {
    new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 0.7, 10.0, Collections.emptyList())
        .weight(1.1);
  }

  @Test
  public void explanation() {
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
  public void equalsAndHashCode() {
    List<Value> usedValues = Arrays.asList(
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
  public void serializeAndDeserialize() throws IOException {
    List<Value> usedValues = Arrays.asList(
        NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(10),
        NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(3));

    ObjectMapper mapper = new ObjectMapper();
    ScoreValue value = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 1.0, 7.2, usedValues);
    byte[] bytes = mapper.writeValueAsBytes(value);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    ScoreValue clone = mapper.readValue(bytes, ScoreValue.class);
    assertNotNull(clone);
    assertEquals(value, clone);
    assertEquals(value.hashCode(), clone.hashCode());
  }

}