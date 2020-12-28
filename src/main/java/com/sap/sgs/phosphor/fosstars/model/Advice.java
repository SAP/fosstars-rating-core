package com.sap.sgs.phosphor.fosstars.model;

import com.sap.sgs.phosphor.fosstars.advice.AdviceContent;

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
   * @return The value.
   */
  Value<?> value();

  /**
   * Get a content of the advice.
   *
   * @return The content.
   */
  AdviceContent content();
}
