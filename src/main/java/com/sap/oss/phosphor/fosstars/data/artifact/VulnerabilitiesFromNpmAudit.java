package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.data.artifact.NvdEntryArtifactCveMatcher.nvdEntryFrom;
import static com.sap.oss.phosphor.fosstars.model.Subject.cast;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_ARTIFACT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.databind.JsonNode;
import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.NoValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.data.npmaudit.model.Advisory;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.NpmArtifact;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import com.sap.oss.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This data provider tries to fill out the {@link OssFeatures#VULNERABILITIES_IN_ARTIFACT}. It
 * gathers vulnerabilities from {@link NpmArtifact} using NPM Audit.
 *
 * @see <a href="https://docs.npmjs.com/auditing-package-dependencies-for-security-vulnerabilities">NPM
 * Audit</a>
 */
public class VulnerabilitiesFromNpmAudit implements DataProvider {

  /**
   * A logger.
   */
  private final Logger logger = LogManager.getLogger(getClass());

  /**
   * NPM registry URL to access security audit.
   */
  private static final String NPM_AUDIT_URL =
      "https://registry.npmjs.org/-/npm/v1/security/audits";

  /**
   * NPM Audit API POST request body template.
   */
  private static final String
      NPM_AUDIT_URL_BODY_ENTITY =
      String.join("",
          "{",
          "\"name\": \"npm-vulnerabilities\",",
          "\"version\": \"1.0.0\",",
          "\"requires\" :{\"%s\": \"%s\"},",
          "\"dependencies\": {\"%s\": {\"version\": \"%s\"}}",
          "}");

  /**
   * An interface to NVD.
   */
  private final NVD nvd;

  /**
   * Initializes a data provider.
   *
   * @param nvd An {@link NVD}.
   */
  public VulnerabilitiesFromNpmAudit(NVD nvd) {
    this.nvd = nvd;
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
   * There is no call back required for this data provider.
   */
  @Override
  public VulnerabilitiesFromNpmAudit set(UserCallback callback) {
    return this;
  }

  /**
   * No cache value is needed that is used by the data provider.
   */
  @Override
  public VulnerabilitiesFromNpmAudit set(ValueCache<Subject> cache) {
    return this;
  }

  /**
   * No configuration is required for this data provider.
   */
  @Override
  public VulnerabilitiesFromNpmAudit configure(Path config) {
    return this;
  }

  @Override
  public VulnerabilitiesFromNpmAudit update(Subject subject, ValueSet values)
      throws IOException {
    logger.info("Gathering vulnerabilities from NPM Audit...");
    Objects.requireNonNull(values, "On no! Values cannot be null");
    NpmArtifact artifact = cast(subject, NpmArtifact.class);

    if (!artifact.version().isPresent()) {
      throw new IOException("Oh no! The version is not available.");
    }

    List<CVEMetadata> cves = cvesFrom(artifact);
    Vulnerabilities vulnerabilities = new Vulnerabilities();
    for (CVEMetadata cveMetadata : cves) {
      Optional<NvdEntry> nvdEntry = nvd.searchNvd(nvdEntryFrom(cveMetadata.cveID));
      nvdEntry.ifPresent(entry ->
          vulnerabilities.add(Vulnerability
              .Builder.from(entry).set(cveMetadata.resolution).make()));
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
    return subject instanceof NpmArtifact;
  }

  /**
   * Creates an HTTP client.
   *
   * @return An HTTP client.
   */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
  }

  /**
   * Fetches audit content from the URL.
   *
   * @param artifact The {@link NpmArtifact} to be fetched.
   * @return Optional path for the locally downloaded file.
   * @throws IOException If something went wrong.
   */
  private JsonNode fetch(NpmArtifact artifact) throws IOException {
    try (CloseableHttpClient client = httpClient()) {
      HttpPost httpPostRequest = new HttpPost(NPM_AUDIT_URL);
      httpPostRequest.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
      String body = String.format(NPM_AUDIT_URL_BODY_ENTITY, artifact.identifier(),
          artifact.version().get(),
          artifact.identifier(), artifact.version().get());
      httpPostRequest.setEntity(new StringEntity(body));
      try (CloseableHttpResponse httpResponse = client.execute(httpPostRequest)) {
        return Json.mapper().readTree(httpResponse.getEntity().getContent());
      }
    }
  }

  /**
   * Fetches the npm artifact security audit.
   *
   * @param artifact The {@link NpmArtifact}.
   * @return List of {@link CVEMetadata}s.
   * @throws IOException If something went wrong.
   */
  private List<CVEMetadata> cvesFrom(NpmArtifact artifact) throws IOException {
    JsonNode json = fetch(artifact);
    JsonNode advisories = json.at("/advisories");

    List<CVEMetadata> cvesMetadata = new ArrayList<>();
    for (JsonNode item : advisories) {
      Advisory advisory = Json.mapper().treeToValue(item, Advisory.class);
      cvesMetadata.addAll(advisory.getCves().stream()
          .map(cve -> new CVEMetadata(cve, advisory.getPatchedVersions()))
          .collect(Collectors.toList()));
    }
    return cvesMetadata;
  }


  /**
   * A class to hold the metadata information for a specific CVE identifier.
   */
  private static class CVEMetadata {

    /**
     * The CVE identifier.
     */
    private final String cveID;

    /**
     * The {@link Resolution}.
     */
    private final Resolution resolution;

    public CVEMetadata(String cveId, String resolution) {
      this.cveID = cveId;
      this.resolution
          = StringUtils.isEmpty(resolution) || resolution.equals("<0.0.0")
          ? Resolution.UNPATCHED : Resolution.PATCHED;
    }
  }
}