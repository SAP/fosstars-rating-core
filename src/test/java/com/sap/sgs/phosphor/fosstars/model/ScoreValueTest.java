package com.sap.sgs.phosphor.fosstars.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ScoreValueTest {

  private static final double ACCURACY = 0.01;

  @Test
  public void increase() {
    ScoreValue value = new ScoreValue();
    assertEquals(Score.MIN, value.score(), ACCURACY);
    value.increase(2.1);
    assertEquals(2.1, value.score(), ACCURACY);
    value.increase(4.9);
    assertEquals(7.0, value.score(), ACCURACY);
    value.increase(20.0);
    assertEquals(Score.MAX, value.score(), ACCURACY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void increaseNegative() {
    new ScoreValue().increase(-1.0);
  }

  @Test
  public void decrease() {
    ScoreValue value = new ScoreValue(5.0, 10.0);
    assertEquals(5.0, value.score(), ACCURACY);
    value.decrease(2.3);
    assertEquals(2.7, value.score(), ACCURACY);
    value.decrease(1.1);
    assertEquals(1.6, value.score(), ACCURACY);
    value.decrease(5.0);
    assertEquals(Score.MIN, value.score(), ACCURACY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void decreaseNegative() {
    new ScoreValue().decrease(-1.0);
  }

  @Test
  public void confidence() {
    ScoreValue value = new ScoreValue(5.0, 10.0);
    assertEquals(10.0, value.confidence(), ACCURACY);
    value.confidence(5.1);
    assertEquals(5.1, value.confidence(), ACCURACY);
  }

  @Test
  public void equalsAndHashCode() {
    ScoreValue one = new ScoreValue(5.0, 10.0);
    ScoreValue two = new ScoreValue(5.0, 10.0);
    ScoreValue three = new ScoreValue(5.1, 10.0);
    ScoreValue four = new ScoreValue(5.0, 9.0);

    assertEquals(one, two);
    assertNotEquals(one, three);
    assertNotEquals(one, four);
    assertNotEquals(three, four);

    assertEquals(one.hashCode(), two.hashCode());
    assertNotEquals(one.hashCode(), three.hashCode());
    assertNotEquals(one.hashCode(), four.hashCode());
    assertNotEquals(three.hashCode(), four.hashCode());
  }

}