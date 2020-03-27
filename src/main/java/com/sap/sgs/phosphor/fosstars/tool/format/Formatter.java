package com.sap.sgs.phosphor.fosstars.tool.format;

import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;

/**
 * The interface of a formatter which knows how to convert
 * a rating value to a string.
 */
public interface Formatter {

  /**
   * Print out a formatted rating value.
   *
   * @param ratingValue The rating value to be printed.
   * @return A formatted rating value.
   */
  String print(RatingValue ratingValue);
}
