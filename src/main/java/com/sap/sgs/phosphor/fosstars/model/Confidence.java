package com.sap.sgs.phosphor.fosstars.model;

import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
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
   * Calculates a confidence level based on a number of unknown feature values.
   *
   * @param values The features values.
   * @return The confidence level.
   */
  static double make(Value... values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    if (values.length == 0) {
      throw new IllegalArgumentException("Hey! Values can't be empty!");
    }
    int unknown = 0;
    for (Value value : values) {
      if (value.isUnknown()) {
        unknown++;
      }
    }

    if (unknown == 0) {
      return MAX;
    }

    return MAX * (values.length - unknown) / values.length;
  }

}
