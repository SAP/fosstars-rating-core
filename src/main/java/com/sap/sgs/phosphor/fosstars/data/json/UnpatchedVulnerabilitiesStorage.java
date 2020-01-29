package com.sap.sgs.phosphor.fosstars.data.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * The class maintains a list of unpatched vulnerabilities for open-source projects. An open-source
 * project is represented by the URL it its source code repository.
 */
public class UnpatchedVulnerabilitiesStorage {

  /**
   * Path to a resource which contains the information about unpatched vulnerabilities.
   */
  private static final String RESOURCE_PATH =
      "com/sap/sgs/phosphor/fosstars/data/UnpatchedVulnerabilities.json";

  /**
   * An ObjectMapper for serialization and deserialization.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * This mapping allows retrieving vulnerabilities for an open-source project by providing an URL
   * to the source code repository.
   */
  private final Map<URL, Vulnerabilities> projectVulnerabilities;

  /**
   * @param projectVulnerabilities A mapping which maps an URL to source code repository to a list
   * of unpatched vulnerabilities.
   */
  public UnpatchedVulnerabilitiesStorage(
      @JsonProperty("projectVulnerabilities") Map<URL, Vulnerabilities> projectVulnerabilities) {
    Objects.requireNonNull(projectVulnerabilities, "Hey! Looks like you gave me a null!");
    this.projectVulnerabilities = projectVulnerabilities;
  }

  /*
   * This getter is here to make Jackson happy.
   */
  @JsonGetter("projectVulnerabilities")
  private Map<URL, Vulnerabilities> projectVulnerabilities() {
    return Collections.unmodifiableMap(projectVulnerabilities);
  }

  /**
   * Returns a list of unpatched vulnerabilities for a project.
   *
   * @param url A URL to source code repository for the project.
   * @return An instance of {@link Vulnerabilities} with all known unpatched vulnerabilities for the
   * project.
   * @throws MalformedURLException If the URL is not valid.
   */
  public Vulnerabilities get(String url) throws MalformedURLException {
    Vulnerabilities vulnerabilities = projectVulnerabilities.get(new URL(url));
    if (vulnerabilities == null) {
      return new Vulnerabilities();
    }
    return vulnerabilities;
  }

  /**
   * Returns a list of unpatched vulnerabilities for a project.
   *
   * @param url A URL to source code repository for the project.
   * @return An instance of {@link Vulnerabilities} with all known unpatched vulnerabilities for the
   * project.
   */
  public Vulnerabilities get(URL url) {
    Vulnerabilities vulnerabilities = projectVulnerabilities.get(url);
    if (vulnerabilities == null) {
      return new Vulnerabilities();
    }
    return vulnerabilities;
  }

  /**
   * Adds a vulnerability for a project.
   *
   * @param url A URL to the source code repository.
   * @param vulnerability The vulnerability to be added.
   * @throws MalformedURLException If the URL is not valid.
   */
  public void add(String url, Vulnerability vulnerability) throws MalformedURLException {
    add(new URL(url), vulnerability);
  }

  /**
   * Adds a vulnerability for a project.
   *
   * @param url A URL to the source code repository.
   * @param vulnerability The vulnerability to be added.
   */
  public void add(URL url, Vulnerability vulnerability) {
    Vulnerabilities vulnerabilities = projectVulnerabilities.get(url);
    if (vulnerabilities == null) {
      projectVulnerabilities.put(url, new Vulnerabilities(vulnerability));
    } else {
      vulnerabilities.add(vulnerability);
    }
  }

  /**
   * Loads the default list of unpatched vulnerabilities.
   *
   * @return An instance of {@link UnpatchedVulnerabilitiesStorage}.
   * @throws IOException If something went wrong.
   */
  public static UnpatchedVulnerabilitiesStorage load() throws IOException {
    return load(RESOURCE_PATH);
  }

  /**
   * Loads a custom list of unpatched vulnerabilities form a JSON file.
   *
   * @param path A path to the file.
   * @return An instance of {@link UnpatchedVulnerabilitiesStorage}.
   * @throws IOException If something went wrong.
   */
  public static UnpatchedVulnerabilitiesStorage load(String path) throws IOException {
    File file = new File(path);
    UnpatchedVulnerabilitiesStorage storage;
    if (file.exists()) {
      storage = MAPPER.readValue(file, UnpatchedVulnerabilitiesStorage.class);
    } else {
      storage = loadFromResource(path);
    }
    if (storage == null) {
      throw new IOException(String.format(
          "Could not load unpatched vulnerabilities from %s", path));
    }
    return check(storage);
  }

  /**
   * Stores the current list of unpatched vulnerabilities to a JSON file.
   *
   * @param path A path to the file.
   * @throws IOException If something went wrong.
   */
  public void store(String path) throws IOException {
    Files.write(Paths.get(path), MAPPER.writerWithDefaultPrettyPrinter().writeValueAsBytes(this));
  }

  /**
   * Tries to load {@link UnpatchedVulnerabilitiesStorage} from a resource.
   *
   * @param path A path to the resource.
   * @return An instance of {@link UnpatchedVulnerabilitiesStorage}.
   * @throws IOException If something went wrong.
   */
  private static UnpatchedVulnerabilitiesStorage loadFromResource(String path) throws IOException {
    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    if (is != null) {
      try {
        return MAPPER.readValue(is, UnpatchedVulnerabilitiesStorage.class);
      } finally {
        is.close();
      }
    }
    throw new IOException(String.format("Resource '%s' not found!", path));
  }

  /**
   * Check if an instance of {@link UnpatchedVulnerabilitiesStorage} is valid.
   *
   * @param storage The storage to be checked;
   * @return The same storage if it's valid.
   * @throws IllegalArgumentException If the storage is invalid.
   */
  private static UnpatchedVulnerabilitiesStorage check(UnpatchedVulnerabilitiesStorage storage) {
    for (Map.Entry<URL, Vulnerabilities> entry : storage.projectVulnerabilities.entrySet()) {
      for (Vulnerability vulnerability : entry.getValue().entries()) {
        if (vulnerability.resolution() != Resolution.UNPATCHED) {
          throw new IllegalArgumentException(String.format(
              "Hey! The list of unpatched vulnerabilities is supposed to contain "
                  + "only unpatched vulnerabilities (check out '%s')", vulnerability.id()));
        }
      }
    }
    return storage;
  }

}
