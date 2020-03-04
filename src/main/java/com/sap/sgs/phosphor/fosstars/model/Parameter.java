package com.sap.sgs.phosphor.fosstars.model;

/**
 * The interface represents a tunable parameter of a score or rating.
 */
public interface Parameter {

  /**
   * Returns an interval of valid values of the parameter.
   */
  Interval boundaries();

  /**
   * Updates the parameter.
   *
   * @param v A new value.
   * @return The same parameter.
   * @throws UnsupportedOperationException If the parameter can't be updated.
   */
  Parameter value(double v);

  /**
   * Returns the value of the parameter.
   */
  Double value();
}
