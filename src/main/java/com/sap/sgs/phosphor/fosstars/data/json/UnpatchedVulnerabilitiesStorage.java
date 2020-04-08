package com.sap.sgs.phosphor.fosstars.data.json;

import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.UNKNOWN_FIXED_DATE;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.UNKNOWN_INTRODUCED_DATE;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.Reference;
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
public class UnpatchedVulnerabilitiesStorage extends AbstractJsonStorage {

  /**
   * Path to a resource which contains the information about unpatched vulnerabilities.
   */
  private static final String RESOURCE_PATH =
      "com/sap/sgs/phosphor/fosstars/data/UnpatchedVulnerabilities.json";

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
  public Vulnerabilities get(String url) throws MalformedURLException {
    return get(new URL(url));
  }

  /**
   * Returns a list of unpatched vulnerabilities for a project.
   *
   * @param url A URL to source code repository for the project.
   * @return An instance of {@link Vulnerabilities} with all known unpatched vulnerabilities for the
   *         project.
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

  /**
   * The main method is here for demo purposes.
   * It can be also used to add unpatched vulnerabilities to the storage.
   */
  public static void main(String... args) throws IOException {
    UnpatchedVulnerabilitiesStorage storage = UnpatchedVulnerabilitiesStorage.load();
    storage.add(
        "https://github.com/odata4j/odata4j",
        new Vulnerability(
            "https://nvd.nist.gov/vuln/detail/CVE-2014-0171",
            "XML external entity (XXE) vulnerability",
            CVSS.v2(5.0),
            Collections.emptyList(),
            Resolution.UNPATCHED,
            UNKNOWN_INTRODUCED_DATE,
            UNKNOWN_FIXED_DATE));
    storage.add(
        "https://github.com/odata4j/odata4j",
        new Vulnerability(
            "https://nvd.nist.gov/vuln/detail/CVE-2016-11023",
            "SQL injection (ExecuteCountQueryCommand)",
            CVSS.v3(9.8),
            Collections.singletonList(
                new Reference(
                    "Public disclosure",
                    new URL("https://groups.google.com/d/msg/odata4j-discuss/_lBwwXP30g0/Av6zkZMdBwAJ"))),
            Resolution.UNPATCHED,
            UNKNOWN_INTRODUCED_DATE,
            UNKNOWN_FIXED_DATE));
    storage.add(
        "https://github.com/odata4j/odata4j",
        new Vulnerability(
            "https://nvd.nist.gov/vuln/detail/CVE-2016-11024",
            "SQL injection (ExecuteJPQLQueryCommand)",
            CVSS.v3(9.8),
            Collections.singletonList(
                new Reference(
                    "Public disclosure",
                    new URL("https://groups.google.com/d/msg/odata4j-discuss/_lBwwXP30g0/Av6zkZMdBwAJ"))),
            Resolution.UNPATCHED,
            UNKNOWN_INTRODUCED_DATE,
            UNKNOWN_FIXED_DATE));
    storage.store("src/main/resources/" + RESOURCE_PATH);
  }

}
