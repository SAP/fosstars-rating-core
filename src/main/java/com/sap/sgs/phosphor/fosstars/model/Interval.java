package com.sap.sgs.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;

/**
 * An interface which represents an interval.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = DoubleInterval.class)
})
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
