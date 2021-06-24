package com.sap.oss.phosphor.fosstars.tool.report;

import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.OpenSourceProject;
import java.io.IOException;
import java.util.List;

/**
 * A reporter create a report for a number of open-source projects.
 *
 * @param <T> A type of projects.
 */
public interface Reporter<T extends Subject> {

  /**
   * Runs the reporter for a list of projects.
   *
   * @param projects The projects.
   * @throws IOException If something went wrong.
   */
  void runFor(List<T> projects) throws IOException;

  /**
   * Returns a reporter that does nothing.
   *
   * @param <T> A type of projects.
   * @return A reporter that does nothing.
   */
  static <T extends OpenSourceProject> Reporter<T> dummy() {
    return projects -> {};
  }
}
