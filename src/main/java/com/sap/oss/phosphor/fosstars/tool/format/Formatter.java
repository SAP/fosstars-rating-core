package com.sap.oss.phosphor.fosstars.tool.format;

import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;

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
  
  /**
   * Print out the title of a single value
   *
   * @param value The value.
   * @return A formatted title for the value.
   */
  String printTitle(Value<?> value);
  
  /**
   * Print out the body of a single value
   *
   * @param value The value.
   * @return A formatted body for the value.
   */
  String printBody(Value<?> value);
  
}
