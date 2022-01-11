package com.sap.oss.phosphor.fosstars.tool.finder;

import java.io.IOException;
import java.util.List;

/**
 * Interface for the subjects to find the related projects or artifacts.
 */
public interface Finder<T> {

  /**
   * Gather subjects with the possible released versions.
   *
   * @return A list of subjects.
   * @throws IOException If something went wrong.
   */
  List<T> run() throws IOException;

  /**
   * Sets a config.
   *
   * @param config The config to be used.
   * @return The same {@link Finder}.
   */
  Finder set(FinderConfig config);
}
