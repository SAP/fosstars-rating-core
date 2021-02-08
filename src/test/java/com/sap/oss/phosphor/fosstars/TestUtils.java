package com.sap.oss.phosphor.fosstars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Set;

public class TestUtils {

  private static final double DELTA = 0.01;

  /**
   * The method checks if a score calculates an expected score value for a specified set of values.
   * First, the method asks the score to calculate a score value for the passed values.
   * Next, it checks if the score value equals to the expected one.
   * Then, it checks if the score value contains all the passed values.
   *
   * @param expectedScoreValue The expected score value.
   * @param score The score.
   * @param values The values.
   */
  public static void assertScore(double expectedScoreValue, Score score, Set<Value<?>> values) {
    ScoreValue scoreValue = score.calculate(values);
    assertEquals(expectedScoreValue, scoreValue.get(), DELTA);
    assertEquals(values.size(), scoreValue.usedValues().size());
    assertTrue(values.containsAll(scoreValue.usedValues()));
  }

  /**
   * The method checks if a score calculates an expected score value for a specified set of values.
   * First, the method asks the score to calculate a score value for the passed values.
   * Next, it checks if the score value belong to the expected range.
   * Then, it checks if the score value contains all the passed values.
   *
   * @param expectedInterval The expected range for the score value.
   * @param score The score.
   * @param values The values.
   */
  public static void assertScore(Interval expectedInterval, Score score, Set<Value<?>> values) {
    ScoreValue scoreValue = score.calculate(values);
    assertTrue(expectedInterval.contains(scoreValue.get()));
    assertTrue(Confidence.INTERVAL.contains(scoreValue.confidence()));
    assertEquals(values.size(), scoreValue.usedValues().size());
    assertTrue(values.containsAll(scoreValue.usedValues()));
  }
}
