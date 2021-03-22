package com.sap.oss.phosphor.fosstars.advice;

import com.sap.oss.phosphor.fosstars.model.Subject;
import java.util.Collections;
import java.util.List;

/**
 * An advisor that can give advice about a rating and scores of a subject.
 */
public interface Advisor {

  /**
   * An advisor that gives no advice.
   */
  Advisor DUMMY = subject -> Collections.emptyList();

  /**
   * Get a list of advice for a subject.
   *
   * @param subject The subject.
   * @return A list of advice for the subject.
   */
  List<Advice> adviceFor(Subject subject);
}
