package com.sap.sgs.phosphor.fosstars.tool;

import com.sap.sgs.phosphor.fosstars.model.subject.oss.OpenSourceProject;
import java.io.IOException;
import java.util.List;

/**
 * A reporter create a report for a number of open-source projects.
 *
 * @param <T> A type of projects.
 */
public interface Reporter<T extends OpenSourceProject> {

  /**
   * Runs the reporter for a list of projects.
   *
   * @param projects The projects.
   * @throws IOException If something went wrong.
   */
  void runFor(List<T> projects) throws IOException;

  /**
   * This is a reporter which does nothing.
   */
  Reporter DUMMY = projects -> {};
}
