package com.sap.oss.phosphor.fosstars.tool.format;

import com.sap.oss.phosphor.fosstars.model.Subject;

/**
 * The interface of a formatter which knows how to print rating values.
 */
public interface Formatter {

  /**
   * Print out a formatted subject.
   *
   * @param subject The subject.
   * @return A formatted subject.
   */
  String print(Subject subject);
  
}
