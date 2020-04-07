package com.sap.sgs.phosphor.fosstars.data.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class maintains information about open-source projects which are supported by companies.
 */
public class CompanySupportStorage extends AbstractJsonStorage {

  /**
   * Path to a resource which contains the information company support.
   */
  private static final String RESOURCE_PATH =
      "com/sap/sgs/phosphor/fosstars/data/CompanySupport.json";

  /**
   * Maps a project's code repository URL to a list of companies which support the project.
   */
  private final Map<String, List<String>> projects;

  /**
   * Initializes a storage.
   *
   * @param projects Information about projects which are supported by companies.
   */
  public CompanySupportStorage(@JsonProperty("projects") Map<String, List<String>> projects) {
    Objects.requireNonNull(projects, "Projects can't be null");
    this.projects = projects;
  }

  /**
   * Checks if a project is supported by at least one company.
   *
   * @param url The project's code repository URL.
   * @return True if the project is supported at least by one company, false otherwise.
   */
  public boolean supports(String url) {
    return !companies(url).isEmpty();
  }

  /**
   * Checks if a project is supported by at least one company.
   *
   * @param url The project's code repository URL.
   * @return True if the project is supported at least by one company, false otherwise.
   */
  public boolean supports(URL url) {
    return supports(url.toString());
  }

  /**
   * Looks for companies which support a project.
   *
   * @param url The project's code repository URL.
   * @return A list of companies which support the project.
   */
  public List<String> companies(String url) {
    Objects.requireNonNull(url, "URL can't be null!");
    checkHttps(url);

    for (Map.Entry<String, List<String>> entry : projects.entrySet()) {
      if (url.startsWith(entry.getKey())) {
        return entry.getValue();
      }
    }

    return Collections.emptyList();
  }

  /*
   * This getter is here to make Jackson happy.
   */
  @JsonGetter("projects")
  private Map<String, List<String>> projects() {
    return Collections.unmodifiableMap(projects);
  }

  /**
   * Loads info about company support from the default location.
   *
   * @return An instance of {@link CompanySupportStorage}.
   * @throws IOException If something went wrong.
   */
  public static CompanySupportStorage load() throws IOException {
    return load(RESOURCE_PATH);
  }

  /**
   * Loads info about company support from a specified file.
   *
   * @return An instance of {@link CompanySupportStorage}.
   * @throws IOException If something went wrong.
   */
  public static CompanySupportStorage load(String path) throws IOException {
    File file = new File(path);
    CompanySupportStorage storage;
    if (file.exists()) {
      storage = MAPPER.readValue(file, CompanySupportStorage.class);
    } else {
      storage = loadFromResource(path);
    }
    if (storage == null) {
      throw new IOException(String.format(
          "Could not load info about companies from %s", path));
    }
    return storage;
  }

  /**
   * Tries to load info about company support from a specified file.
   *
   * @param path A path to the resource.
   * @return An instance of {@link CompanySupportStorage}.
   * @throws IOException If something went wrong.
   */
  private static CompanySupportStorage loadFromResource(String path) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (is != null) {
      try {
        return MAPPER.readValue(is, CompanySupportStorage.class);
      } finally {
        is.close();
      }
    }
    throw new IOException(String.format("Resource '%s' not found!", path));
  }
}
