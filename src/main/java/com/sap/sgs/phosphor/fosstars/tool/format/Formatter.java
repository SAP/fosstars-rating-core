package com.sap.sgs.phosphor.fosstars.tool.format;

import com.sap.sgs.phosphor.fosstars.model.Subject;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;

/**
 * The interface of a formatter which knows how to print rating values.
 */
public interface Formatter {

  /**
   * Print out a formatted rating value.
   *
   * @param ratingValue The rating value to be printed.
   * @return A formatted rating value.
   */
  String print(RatingValue ratingValue);

  /**
   * Print out a formatted subject.
   *
   * @param subject The subject.
   * @return A formatted subject.
   */
  String print(Subject subject);
}
