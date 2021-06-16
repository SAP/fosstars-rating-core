package com.sap.oss.phosphor.fosstars.tool;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * The class holds a configuration for {@link Application}.
 */
public class Config {

  /**
   * Where a cache file is located.
   */
  final String cacheFilename;

  /**
   * A config for reporting.
   */
  final List<ReportConfig> reportConfigs;

  /**
   * A config for {@link GitHubProjectFinder}.
   */
  final GitHubProjectFinder.Config finderConfig;

  /**
   * Creates a new config.
   *
   * @param cacheFilename Where a cache file is located.
   * @param reportConfigs A config for reporting.
   * @param finderConfig A configuration from {@link GitHubProjectFinder}.
   */
  Config(
      @JsonProperty("cache") String cacheFilename,
      @JsonProperty("reports") List<ReportConfig> reportConfigs,
      @JsonProperty("finder") GitHubProjectFinder.Config finderConfig) {

    this.cacheFilename = cacheFilename;
    this.reportConfigs = reportConfigs;
    this.finderConfig = finderConfig;
  }

  /**
   * Checks the config has a filename of a cache.
   *
   * @return True if the config has a filename of a cache, false otherwise.
   */
  boolean hasCacheFile() {
    return cacheFilename != null && !cacheFilename.trim().isEmpty();
  }

  /**
   * Loads a config from a file.
   *
   * @param filename A path to the config.
   * @return A loaded config.
   * @throws IOException If something went wrong.
   */
  static Config from(String filename) throws IOException {
    try (InputStream is = Files.newInputStream(Paths.get(filename))) {
      return from(is);
    }
  }

  /**
   * Loads a config from an input stream.
   *
   * @param is The input stream.
   * @return A loaded config.
   * @throws IOException If something went wrong.
   */
  static Config from(InputStream is) throws IOException {
    ObjectMapper mapper = Yaml.mapper();
    mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    return mapper.readValue(is, Config.class);
  }

}
