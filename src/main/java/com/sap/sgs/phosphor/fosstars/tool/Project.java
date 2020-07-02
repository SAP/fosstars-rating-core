package com.sap.sgs.phosphor.fosstars.tool;

import java.net.URL;

/**
 * An interface for an open-source project.
 */
public interface Project {

  /**
   * Returns a URL of the projects' SCM.
   *
   * @return A URL of the project's SCM.
   */
  URL url();
}
