package com.sap.oss.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * An interface which represents an interval.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface Interval {

  /**
   * Checks if a double belongs to the interval.
   *
   * @param n A number to be checked.
   * @return True if the number belongs to the interval, false otherwise.
   */
  boolean contains(double n);

  /**
   * Checks if an integer belongs to the interval.
   *
   * @param n A number to be checked.
   * @return True if the number belongs to the interval, false otherwise.
   */
  boolean contains(int n);

  /**
   * Calculates a mean of the interval.
   *
   * @return A mean of the interval.
   */
  double mean();
}
