package com.sap.sgs.phosphor.fosstars.model;

import static org.junit.Assert.assertEquals;

import com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures;
import org.junit.Test;

public class ConfidenceTest {

  private static final double DELTA = 0.01;

  @Test
  public void checkValid() {
    Confidence.check(5.0);
    Confidence.check(0.0);
    Confidence.check(10.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkNegative() {
    Confidence.check(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkToBig() {
    Confidence.check(11);
  }

  @Test
  public void adjust() {
    assertEquals(Confidence.MIN, Confidence.adjust(-1), DELTA);
    assertEquals(Confidence.MIN, Confidence.adjust(0), DELTA);
    assertEquals(Confidence.MAX, Confidence.adjust(11), DELTA);
    assertEquals(5.0, Confidence.adjust(5.0), DELTA);
  }

  @Test
  public void make() {
    assertEquals(Confidence.MIN,
        Confidence.make(
            ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.unknown(),
            ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.unknown()),
        DELTA);
    assertEquals((Confidence.MAX - Confidence.MIN) / 2,
        Confidence.make(
            ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(10),
            ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.unknown()),
        DELTA);
    assertEquals(Confidence.MAX,
        Confidence.make(
            ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(10),
            ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(3)),
        DELTA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void makeWithNoValues() {
    Confidence.make();
  }
}