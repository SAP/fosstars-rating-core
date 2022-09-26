package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.Subject.cast;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_ARTIFACT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.delete;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.NoValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.data.owasp.model.Dependency;
import com.sap.oss.phosphor.fosstars.data.owasp.model.OwaspDependencyCheckEntry;
import com.sap.oss.phosphor.fosstars.data.owasp.model.OwaspDependencyCheckReference;
import com.sap.oss.phosphor.fosstars.data.owasp.model.OwaspDependencyCheckVuln;
import com.sap.oss.phosphor.fosstars.data.owasp.model.Software;
import com.sap.oss.phosphor.fosstars.data.owasp.model.VulnerableSoftware;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.value.CVSS;
import com.sap.oss.phosphor.fosstars.model.value.CVSS.V2;
import com.sap.oss.phosphor.fosstars.model.value.CVSS.V3;
import com.sap.oss.phosphor.fosstars.model.value.Reference;
import com.sap.oss.phosphor.fosstars.model.value.VersionRange;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Builder;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.owasp.dependencycheck.Engine;
import org.owasp.dependencycheck.exception.ExceptionCollection;
import org.owasp.dependencycheck.exception.ReportException;
import org.owasp.dependencycheck.utils.Settings;

/**
 * This data provider tries to fill out the
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#VULNERABILITIES_IN_ARTIFACT}.
 * It gathers vulnerabilities in {@link MavenArtifact} using OWASP Dependency-Check.
 * 
 * @see <a href="https://owasp.org/www-project-dependency-check/">OWASP Dependency-Check</a>
 */
public class VulnerabilitiesFromOwaspDependencyCheck implements DataProvider {

  /**
   * The base directory to contain the NVD data source and OWASP Dependency-Check generated DB by
   * parsing through NVD data source.
   */
  private static final String DEFAULT_DOWNLOAD_DIRECTORY = ".fosstars";

  /**
   * The directory to download {@link MavenArtifact} jars.
   */
  private static final String JAR_DIR = String.format("%s/jars", DEFAULT_DOWNLOAD_DIRECTORY);

  /**
   * The directory to save OWASP Dependency-Check reports.
   */
  private static final String REPORT_DIR = String.format("%s/reports", DEFAULT_DOWNLOAD_DIRECTORY);

  /**
   * The directory to save OWASP Dependency-Check temporary files.
   */
  private static final String TEMP_DIR = String.format("%s/tmp", DEFAULT_DOWNLOAD_DIRECTORY);

  /**
   * The directory to save OWASP Dependency-Check DB file.
   */
  private static final String DB_DIR = String.format("%s/db", DEFAULT_DOWNLOAD_DIRECTORY);

  /**
   * The Dependency-Check report file type.
   */
  private static final String REPORT_OUTPUT_FORMAT = "JSON";

  /**
   * Maven repo URL template.
   */
  private static final String MAVEN_REPO_URL_TEMPLATE =
      "https://repo1.maven.org/maven2/%s/%s/%s/%s";

  /**
   * OWASP Dependency-Check settings.
   */
  private final Settings settings;

  /**
   * Initializes a data provider.
   */
  public VulnerabilitiesFromOwaspDependencyCheck() {
    settings = new Settings();
    settings.setString(Settings.KEYS.DATA_DIRECTORY, DEFAULT_DOWNLOAD_DIRECTORY);
    settings.setString(Settings.KEYS.TEMP_DIRECTORY, TEMP_DIR);
    settings.setString(Settings.KEYS.H2_DATA_DIRECTORY, DB_DIR);
  }

  /**
   * The method always returns false, so that all child classes can't be interactive.
   */
  @Override
  public final boolean interactive() {
    return false;
  }

  /**
   * This is a dummy cache which stores nothing.
   */
  @Override
  public ValueCache<Subject> cache() {
    return NoValueCache.create();
  }

  /**
   * There is not call back required for this data provider.
   */
  @Override
  public VulnerabilitiesFromOwaspDependencyCheck set(UserCallback callback) {
    return this;
  }

  /**
   * No cache value is needed that is used by the data provider.
   */
  @Override
  public VulnerabilitiesFromOwaspDependencyCheck set(ValueCache<Subject> cache) {
    return this;
  }

  /**
   * No configuration is required for this data provider.
   */
  @Override
  public VulnerabilitiesFromOwaspDependencyCheck configure(Path config) throws IOException {
    return this;
  }

  @Override
  public VulnerabilitiesFromOwaspDependencyCheck update(Subject subject, ValueSet values)
      throws IOException {

    Objects.requireNonNull(values, "On no! Values cannot be null");
    MavenArtifact artifact = cast(subject, MavenArtifact.class);

    if (!artifact.version().isPresent()) {
      throw new IOException("Oh no! The version is not available.");
    }

    Optional<OwaspDependencyCheckEntry> owaspDependencyCheckEntry = scan(artifact);
    if (!owaspDependencyCheckEntry.isPresent()
        || owaspDependencyCheckEntry.get().getDependencies() == null) {
      values.update(VULNERABILITIES_IN_ARTIFACT.unknown());
      return this;
    }

    Vulnerabilities vulnerabilities = new Vulnerabilities();
    for (Dependency dependency : owaspDependencyCheckEntry.get().getDependencies()) {
      if (dependency.getVulnerabilities() == null) {
        continue;
      }

      for (OwaspDependencyCheckVuln owaspDependencyCheckVuln : dependency.getVulnerabilities()) {
        vulnerabilities.add(from(owaspDependencyCheckVuln));
      }
    }

    values.update(VULNERABILITIES_IN_ARTIFACT.value(vulnerabilities));
    return this;
  }

  /**
   * Returns the supported feature loaded by this data provider.
   */
  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(VULNERABILITIES_IN_ARTIFACT);
  }

  @Override
  public boolean supports(Subject subject) {
    return subject instanceof MavenArtifact;
  }

  /**
   * Scan the input jar file and analyze the extracted {@link Dependency}.
   * 
   * @param engine OWASP Dependency-Check core {@link Engine}.
   * @param file The jar.
   * @param exceptionCollection A collection of exceptions that occurred during the analysis.
   */
  private static void analyze(Engine engine, File file, ExceptionCollection exceptionCollection) {
    try {
      engine.scan(file);
      engine.analyzeDependencies();
    } catch (ExceptionCollection e) {
      exceptionCollection.addException(e);
    }
  }

  /**
   * Process the report generated from analysis of the jar file.
   * 
   * @param engine OWASP Dependency-Check core {@link Engine}.
   * @param fileName The name of report.
   * @param exceptionCollection A collection of exceptions that occurred during processing.
   * @return An optional of {@link OwaspDependencyCheckEntry}.
   * @throws IOException If something went wrong.
   */
  private static Optional<OwaspDependencyCheckEntry> process(Engine engine, String fileName,
      ExceptionCollection exceptionCollection) throws IOException {
    Optional<Path> reportPath = createDirectory(REPORT_DIR);

    if (reportPath.isPresent()) {
      File file = reportPath.get().resolve(String.format("%s.json", fileName)).toFile();

      try {
        engine.writeReports(fileName, file, REPORT_OUTPUT_FORMAT, exceptionCollection);
        return Optional.ofNullable(Json.mapper().readValue(file, OwaspDependencyCheckEntry.class));
      } catch (ReportException e) {
        throw new IOException("Oh no! The report writing failed.", e);
      }
    }
    return Optional.empty();
  }

  /**
   * Scan the {@link MavenArtifact}.
   * 
   * @param artifact The {@link MavenArtifact}.
   * @return An optional of {@link OwaspDependencyCheckEntry}.
   * @throws IOException If something went wrong.
   */
  Optional<OwaspDependencyCheckEntry> scan(MavenArtifact artifact) throws IOException {
    Optional<Path> filePath = fetch(artifact);

    if (filePath.isPresent()) {
      final ExceptionCollection exceptionCollection = new ExceptionCollection();
      try (Engine engine = new Engine(settings)) {
        analyze(engine, filePath.get().toFile(), exceptionCollection);
        return process(engine, filePath.get().toFile().getName(), exceptionCollection);
      } finally {
        delete(TEMP_DIR, JAR_DIR, REPORT_DIR);
      }
    }
    return Optional.empty();
  }

  /**
   * Converts an {@link OwaspDependencyCheckVuln} to a {@link Vulnerability}.
   *
   * @param owaspDependencyCheckVuln The {@link OwaspDependencyCheckVuln} to be converted.
   * @return An instance of {@link Vulnerability}.
   * @throws IOException If something went wrong.
   */
  private static Vulnerability from(OwaspDependencyCheckVuln owaspDependencyCheckVuln)
      throws IOException {
    Objects.requireNonNull(owaspDependencyCheckVuln,
        "Oh no! Vulnerability from OWASP Dependency Check entry is null!");

    return Builder.newVulnerability(owaspDependencyCheckVuln.getName())
        .description(owaspDependencyCheckVuln.getDescription())
        .set(cvssFrom(owaspDependencyCheckVuln))
        .references(referencesFrom(owaspDependencyCheckVuln))
        // OWASP Dependency Check reports unpatched vulnerabilities for an artifact, so we can just
        // set resolution to unpatched.
        .set(Resolution.UNPATCHED)
        .versionRanges(extractVersions(owaspDependencyCheckVuln))
        .make();
  }

  /**
   * Extracts a CVSS score from an {@link OwaspDependencyCheckVuln}.
   *
   * @param owaspDependencyCheckVuln The {@link OwaspDependencyCheckVuln}.
   * @return The CVSS score.
   */
  private static CVSS cvssFrom(OwaspDependencyCheckVuln owaspDependencyCheckVuln) {
    if (owaspDependencyCheckVuln.getCvssv3() != null) {
      return new CVSS.V3(owaspDependencyCheckVuln.getCvssv3().getBaseScore(),
          V3.UNKNOWN_IMPACT, V3.UNKNOWN_IMPACT, V3.UNKNOWN_IMPACT);
    }

    if (owaspDependencyCheckVuln.getCvssv2() != null) {
      return new CVSS.V2(owaspDependencyCheckVuln.getCvssv2().getScore(),
          V2.UNKNOWN_IMPACT, V2.UNKNOWN_IMPACT, V2.UNKNOWN_IMPACT);
    }

    return null;
  }

  /**
   * Extracts references from an {@link OwaspDependencyCheckVuln}.
   *
   * @param owaspDependencyCheckVuln The {@link OwaspDependencyCheckVuln}.
   * @return The list of {@link OwaspDependencyCheckReference}.
   * @throws IOException If something went wrong.
   */
  private static List<Reference> referencesFrom(OwaspDependencyCheckVuln owaspDependencyCheckVuln)
      throws IOException {
    List<Reference> references = new ArrayList<>();
    for (OwaspDependencyCheckReference r : owaspDependencyCheckVuln.getReferences()) {
      try {
        references.add(new Reference(r.getName(), new URL(r.getUrl())));
      } catch (MalformedURLException e) {
        throw new IOException("Could not parse a URL from a reference in NVD", e);
      }
    }
    return references;
  }

  /**
   * Extracts version ranges from an {@link OwaspDependencyCheckVuln}.
   *
   * @param owaspVulnerability The {@link OwaspDependencyCheckVuln}.
   * @return The list of {@link VersionRange}.
   */
  private static List<VersionRange> extractVersions(OwaspDependencyCheckVuln owaspVulnerability) {
    if (owaspVulnerability.getVulnerableSoftware() == null) {
      return Collections.emptyList();
    }

    return owaspVulnerability.getVulnerableSoftware().stream()
        .map(VulnerabilitiesFromOwaspDependencyCheck::createVersionRange)
        .collect(Collectors.toList());
  }

  /**
   * Extracts version range from an {@link VulnerableSoftware}.
   *
   * @param vulnerableSoftware The {@link VulnerableSoftware}.
   * @return The {@link VersionRange}.
   */
  private static VersionRange createVersionRange(VulnerableSoftware vulnerableSoftware) {
    Software software = vulnerableSoftware.getSoftware();
    return new VersionRange(software.getVersionStartIncluding(), software.getVersionEndIncluding());
  }

  /**
   * Creates an HTTP client.
   *
   * @return An HTTP client.
   */
  private static CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

  /**
   * Fetches file from the URL.
   *
   * @param artifact The {@link MavenArtifact} to be fetched.
   * @return Optional path for the locally downloaded file.
   * @throws IOException If something went wrong.
   */
  private static Optional<Path> fetch(MavenArtifact artifact) throws IOException {
    String fileName = String.format("%s-%s.jar", artifact.artifact(), artifact.version().get());
    String url = String.format(MAVEN_REPO_URL_TEMPLATE, artifact.group().replace(".", "/"),
        artifact.artifact(), artifact.version().get(), fileName);

    try (CloseableHttpClient client = httpClient()) {
      HttpGet httpGetRequest = new HttpGet(url);
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest)) {
        HttpEntity entity = httpResponse.getEntity();
        if (entity == null) {
          return Optional.empty();
        }
        return writeFile(entity, JAR_DIR, fileName);
      }
    }
  }

  /**
   * Write the response entity to a file in the directory.
   * 
   * @param entity Entity from Http response.
   * @param directory The directory to write the file.
   * @param fileName The name of the file to write into.
   * @return Optional path of the file written in directory.
   * @throws IOException If something went wrong.
   */
  private static Optional<Path> writeFile(HttpEntity entity, String directory, String fileName)
      throws IOException {
    Optional<Path> directoryPath = createDirectory(directory);
    if (!directoryPath.isPresent()) {
      return Optional.empty();
    }

    try (FileOutputStream outstream =
        new FileOutputStream(String.format("%s/%s", directory, fileName))) {
      entity.writeTo(outstream);
      return Optional.ofNullable(directoryPath.get().resolve(fileName));
    }
  }

  /**
   * Creates the path directory and returns the created directory path.
   * 
   * @param directory The directory path.
   * @return Optional path of the directory.
   * @throws IOException If something went wrong.
   */
  private static Optional<Path> createDirectory(String directory) throws IOException {
    return Optional.ofNullable(Files.createDirectories(Paths.get(directory)));
  }
}
