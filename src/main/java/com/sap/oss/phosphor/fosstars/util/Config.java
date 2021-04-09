package com.sap.oss.phosphor.fosstars.util;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * The class contains helper methods for configuring objects.
 */
public class Config {

  /**
   * Looks for default YAML configuration files for a class.
   * Default config file names are based on the class name. If a canonical class name
   * is "com.sap.CustomClass", then the method will try to load the following configs:
   * <ul>
   *   <li>com.sap.CustomClass.config.yml</li>
   *   <li>com.sap.CustomClass.config.yaml</li>
   *   <li>CustomClass.config.yml</li>
   *   <li>CustomClass.config.yaml</li>
   * </ul>
   *
   * @param clazz The class.
   * @return A path to one of the default configs.
   * @throws IOException If something went wrong.
   */
  public static Optional<Path> loadDefaultYamlConfigIfAvailable(Class<?> clazz) throws IOException {
    for (String name : asList(clazz.getSimpleName(), clazz.getCanonicalName())) {
      for (String suffix : asList("yml", "yaml")) {
        Path path = Paths.get(String.format("%s.config.%s", name, suffix));
        if (Files.isRegularFile(path)) {
          return Optional.of(path);
        }
      }
    }

    return Optional.empty();
  }
}
