package com.sap.oss.phosphor.fosstars.data.json;

import static com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Builder.newVulnerability;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.value.CVSS;
import com.sap.oss.phosphor.fosstars.model.value.CVSS.V2;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
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
public class UnpatchedVulnerabilitiesStorage extends AbstractJsonStorage {

  /**
   * Path to a resource which contains the information about unpatched vulnerabilities.
   */
  private static final String RESOURCE_PATH =
      "com/sap/oss/phosphor/fosstars/data/UnpatchedVulnerabilities.json";

  /**
   * This mapping allows retrieving vulnerabilities for an open-source project by providing an URL
   * to the source code repository.
   */
  private final Map<URL, Vulnerabilities> projectVulnerabilities;

  /**
   * Initializes a storage.
   *
   * @param projectVulnerabilities A mapping which maps an URL to source code repository to a list
   *                               of unpatched vulnerabilities.
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
   *         project.
   * @throws MalformedURLException If the URL is not valid.
   */
  public Vulnerabilities getFor(String url) throws MalformedURLException {
    return getFor(new URL(url));
  }

  /**
   * Returns a list of unpatched vulnerabilities for a project.
   *
   * @param url A URL to source code repository for the project.
   * @return An instance of {@link Vulnerabilities} with all known unpatched vulnerabilities for the
   *         project.
   */
  public Vulnerabilities getFor(URL url) {
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
   * Loads a list of unpatched vulnerabilities from  a JSON file or resource.
   *
   * @param path A path to the file or resource.
   * @return An instance of {@link UnpatchedVulnerabilitiesStorage}.
   * @throws IOException If something went wrong.
   */
  public static UnpatchedVulnerabilitiesStorage load(String path) throws IOException {
    return load(path, UnpatchedVulnerabilitiesStorage.class);
  }

  /**
   * Stores the current list of unpatched vulnerabilities to a JSON file.
   *
   * @param path A path to the file.
   * @throws IOException If something went wrong.
   */
  public void store(String path) throws IOException {
    Files.write(Paths.get(path), Json.toBytes(this));
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

  /**
   * The main method is here for demo purposes.
   * It can be also used to add unpatched vulnerabilities to the storage.
   *
   * @param args Command line arguments.
   * @throws IOException If something went wrong.
   */
  public static void main(String... args) throws IOException {
    UnpatchedVulnerabilitiesStorage storage = UnpatchedVulnerabilitiesStorage.load();

    storage.add(
        "https://github.com/odata4j/odata4j",
        newVulnerability("https://nvd.nist.gov/vuln/detail/CVE-2014-0171")
            .set(new CVSS.V2(5.0, V2.UNKNOWN_IMPACT, V2.UNKNOWN_IMPACT, V2.UNKNOWN_IMPACT))
            .set(Resolution.UNPATCHED)
            .make());

    storage.store("src/main/resources/" + RESOURCE_PATH);
  }

}
