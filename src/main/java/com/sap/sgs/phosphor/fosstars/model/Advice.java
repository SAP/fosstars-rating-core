package com.sap.sgs.phosphor.fosstars.model;

/**
 * An advice about a subject's rating, score or feature value.
 */
public interface Advice {

  /**
   * Get a subject of the advice.
   *
   * @return The subject.
   */
  Subject subject();

  /**
   * Get a value for which the advice was given.
   *
   * @return The value
   */
  Value<?> value();

  /**
   * Get a text of the advice.
   *
   * @return The text.
   */
  String get();
}
