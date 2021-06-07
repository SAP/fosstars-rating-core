package com.sap.oss.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;

/**
 * An interface for a weight.
 * All implementations have to support serialization to JSON with Jackson.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface Weight extends Parameter {

  /**
   * Min weight.
   */
  double MIN = 0.0;

  /**
   * Max weight.
   */
  double MAX = 1.0;

  /**
   * An valid interval for a weight.
   */
  Interval INTERVAL = DoubleInterval.init().from(MIN).to(MAX).openLeft().closedRight().make();

  /**
   * Get the weight's value.
   *
   * @return The weight's value.
   */
  Double value();

  /**
   * Accept a visitor.
   *
   * @param visitor The visitor.
   */
  void accept(Visitor visitor);

  /**
   * Checks if a weight belongs to the correct interval.
   *
   * @param value The weight.
   * @return The weight if it's correct.
   * @throws IllegalArgumentException if the weight is not correct.
   */
  static double check(double value) {
    if (!INTERVAL.contains(value)) {
      throw new IllegalArgumentException(
          String.format("Weight %s doesn't below to the interval %s", value, INTERVAL));
    }
    return value;
  }
}
