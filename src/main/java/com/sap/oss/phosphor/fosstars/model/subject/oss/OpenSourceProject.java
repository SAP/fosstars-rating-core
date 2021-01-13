package com.sap.oss.phosphor.fosstars.model.subject.oss;

import com.sap.oss.phosphor.fosstars.model.Subject;
import java.net.URL;

/**
 * An interface of an open-source project.
 */
public interface OpenSourceProject extends Subject {

  /**
   * Returns a URL of the projects' SCM.
   *
   * @return A URL of the projects' SCM.
   */
  URL scm();
}
