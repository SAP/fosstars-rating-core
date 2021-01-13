package com.sap.oss.phosphor.fosstars.advice;

import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;

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
