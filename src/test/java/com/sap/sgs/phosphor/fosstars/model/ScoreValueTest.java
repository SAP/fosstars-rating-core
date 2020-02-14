package com.sap.sgs.phosphor.fosstars.model;

import static com.sap.sgs.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Test;

public class ScoreValueTest {

  private static final double ACCURACY = 0.01;

  @Test
  public void increase() {
    ScoreValue value = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE);
    assertEquals(Score.MIN, value.get(), ACCURACY);
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
    ScoreValue value = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 10.0);
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
    ScoreValue value = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 10.0);
    assertEquals(10.0, value.confidence(), ACCURACY);
    value.confidence(5.1);
    assertEquals(5.1, value.confidence(), ACCURACY);
  }

  @Test
  public void equalsAndHashCode() {
    ScoreValue one = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 10.0);
    ScoreValue two = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 10.0);
    ScoreValue three = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 10.0);
    ScoreValue four = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.0, 9.0);

    assertEquals(one, two);
    assertNotEquals(one, three);
    assertNotEquals(one, four);
    assertNotEquals(three, four);

    assertEquals(one.hashCode(), two.hashCode());
    assertNotEquals(one.hashCode(), three.hashCode());
    assertNotEquals(one.hashCode(), four.hashCode());
    assertNotEquals(three.hashCode(), four.hashCode());
  }

  @Test
  public void serializeAndDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    ScoreValue value = new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 7.2);
    byte[] bytes = mapper.writeValueAsBytes(value);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    ScoreValue clone = mapper.readValue(bytes, ScoreValue.class);
    assertNotNull(clone);
    assertEquals(value, clone);
    assertEquals(value.hashCode(), clone.hashCode());
  }

}