package com.sap.oss.phosphor.fosstars.tool.finder;

import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.tool.GitHubProjectFinder;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Abstract class for the subject to scan and gather subjects.
 */
public abstract class AbstractEntityFinder<T extends Subject> implements Finder<T> {

  /**
   * A configuration.
   */
  protected FinderConfig config = new FinderConfig();

  /**
   * A parser of configurations for {@link GitHubProjectFinder} which are stored in YAML.
   */
  public static class ConfigParser {

    /**
     * Parse a configuration stored in a file.
     *
     * @param filename The file name.
     * @return A loaded configuration.
     * @throws IOException If something went wrong.
     */
    public FinderConfig parse(String filename) throws IOException {
      try (InputStream is = Files.newInputStream(Paths.get(filename))) {
        return parse(is);
      }
    }

    /**
     * Parse a configuration from an input stream.
     *
     * @param is The input stream.
     * @return A loaded configuration.
     * @throws IOException If something went wrong.
     */
    public FinderConfig parse(InputStream is) throws IOException {
      Objects.requireNonNull(is, "Input stream can't be null!");
      return Yaml.read(is, FinderConfig.class);
    }
  }
}
