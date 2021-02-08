package com.sap.oss.phosphor.fosstars.model;

import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The interface shows that a class can provide a level of confidence for the result
 * which it holds or provides.
 */
public interface Confidence {

  /**
   * Minimal confidence.
   */
  double MIN = 0.0;

  /**
   * Maximum confidence.
   */
  double MAX = 10.0;

  /**
   * A valid interval for a score value.
   */
  Interval INTERVAL = DoubleInterval.init().from(MIN).to(MAX).closed().make();

  /**
   * Get a level of confidence.
   *
   * @return A level of confidence.
   */
  double confidence();

  /**
   * Checks if a specified confidence level is correct.
   *
   * @param confidence The confidence level to be checked.
   * @return The same confidence level if it's correct.
   * @throws IllegalArgumentException If the confidence level is not correct.
   */
  static double check(double confidence) {
    if (!INTERVAL.contains(confidence)) {
      throw new IllegalArgumentException(String.format(
          "Hey! Confidence must be in interval [%.2f, %.2f] but you gave me %.02f",
          MIN, MAX, confidence));
    }
    return confidence;
  }

  /**
   * Checks if a confidence is in the valid range, and returns an adjusted value if necessary.
   *
   * @param value A confidence to be checked.
   * @return {@link #MIN} if the confidence is less than {@link Confidence#MIN},
   *         {@link #MAX} if the confidence is greater than {@link Confidence#MAX},
   *         or the original confidence otherwise.
   */
  static double adjust(double value) {
    if (value < Confidence.MIN) {
      return Confidence.MIN;
    }
    if (value > Confidence.MAX) {
      return Confidence.MAX;
    }
    return value;
  }

  /**
   * Calculates a confidence level based on a number of values.
   * The methods takes into account the following:
   * <ul>
   *   <li>number of unknown values</li>
   *   <li>score values with their confidences and weights</li>
   *   <li>values with confidences</li>
   * </ul>
   *
   * @param values The features values.
   * @return The confidence level.
   */
  static double make(List<Value<?>> values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");

    if (values.size() == 0) {
      throw new IllegalArgumentException("Hey! Values can't be empty!");
    }

    double weightSum = 0.0;
    double weightedConfidenceSum = 0.0;

    for (Value<?> value : values) {
      if (value.isUnknown()) {
        // if value is unknown, then confidence is min and weight is 1.0
        weightSum += 1.0;
      } else if (value instanceof ScoreValue) {
        ScoreValue scoreValue = (ScoreValue) value;
        weightSum += scoreValue.weight();
        weightedConfidenceSum += scoreValue.weight() * scoreValue.confidence();
      } else if (value instanceof Confidence) {
        weightedConfidenceSum += ((Confidence) value).confidence();
        weightSum += 1.0;
      } else {
        // if it's a regular value, then confidence is max and weight is 1.0 as well
        weightedConfidenceSum += MAX;
        weightSum += 1.0;
      }
    }

    return adjust(weightedConfidenceSum / weightSum);
  }

  /**
   * Calculates a confidence level based on a number of values.
   *
   * @param values The values.
   * @return The confidence level
   * @see #make(List)
   */
  static double make(Value<?>... values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    return make(Arrays.asList(values));
  }

}
