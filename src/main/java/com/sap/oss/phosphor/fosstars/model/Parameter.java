package com.sap.oss.phosphor.fosstars.model;

/**
 * The interface represents a tunable parameter of a score or rating.
 */
public interface Parameter {

  /**
   * Get boundaries of the parameter.
   *
   * @return An interval of valid values of the parameter.
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
   * Get the value of the parameter.
   *
   * @return The value of the parameter.
   */
  Double value();

  /**
   * Checks if the entity is immutable.
   *
   * @return True if the entity is immutable, false otherwise.
   */
  boolean isImmutable();
}
