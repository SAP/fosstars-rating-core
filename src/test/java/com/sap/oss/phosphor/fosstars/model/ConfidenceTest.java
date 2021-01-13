package com.sap.oss.phosphor.fosstars.model;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.SECURITY_TESTING_SCORE_EXAMPLE;
import static org.junit.Assert.assertEquals;

import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class ConfidenceTest {

  private static final double DELTA = 0.01;

  @Test
  public void testValid() {
    Confidence.check(5.0);
    Confidence.check(0.0);
    Confidence.check(10.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegative() {
    Confidence.check(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToBig() {
    Confidence.check(11);
  }

  @Test
  public void testAdjust() {
    assertEquals(Confidence.MIN, Confidence.adjust(-1), DELTA);
    assertEquals(Confidence.MIN, Confidence.adjust(0), DELTA);
    assertEquals(Confidence.MAX, Confidence.adjust(11), DELTA);
    assertEquals(5.0, Confidence.adjust(5.0), DELTA);
  }

  @Test
  public void testMakeWithUnknown() {
    assertEquals(Confidence.MIN,
        Confidence.make(
            NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.unknown(),
            NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.unknown()),
        DELTA);
    assertEquals((Confidence.MAX - Confidence.MIN) / 2,
        Confidence.make(
            NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(10),
            NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.unknown()),
        DELTA);
    assertEquals(Confidence.MAX,
        Confidence.make(
            NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(10),
            NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(3)),
        DELTA);
  }

  @Test
  public void testMakeWithScoreValues() {
    assertEquals(6.18,
        Confidence.make(
            new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).confidence(3.0).weight(0.4),
            new ScoreValue(SECURITY_TESTING_SCORE_EXAMPLE).confidence(8.0).weight(0.7)),
        DELTA);
  }

  @Test
  public void testMakeWithVariousValues() {
    assertEquals(5.41,
        Confidence.make(
            new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE).confidence(3.0).weight(0.4),
            new ScoreValue(SECURITY_TESTING_SCORE_EXAMPLE).confidence(8.0).weight(0.7),
            NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(10),
            NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.unknown()),
        DELTA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMakeWithNoValues() {
    Confidence.make();
  }
}