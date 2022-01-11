package com.sap.oss.phosphor.fosstars.tool.report;

import com.sap.oss.phosphor.fosstars.model.Subject;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * This is the abstract class for a reporter that takes a number of projects and merge them in to a
 * JSON file.
 */
abstract class AbstractJsonReporter<T extends Subject> extends AbstractReporter<T> {

  /**
   * A path to an output file.
   */
  protected final Path filename;

  /**
   * Initializes a new reporter.
   *
   * @param filename A path to an output file.
   */
  public AbstractJsonReporter(String filename) {
    Objects.requireNonNull(filename, "Oh no! Output filename is null!");
    if (filename.trim().isEmpty()) {
      throw new IllegalArgumentException("Oh no! Output filename is empty!");
    }
    this.filename = Paths.get(filename);
  }
}
