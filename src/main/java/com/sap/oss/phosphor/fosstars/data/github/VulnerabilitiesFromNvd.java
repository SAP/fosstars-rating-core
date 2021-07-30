package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.NvdEntryMatcher.entriesFor;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.VulnerabilitiesFeature;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import com.sap.oss.phosphor.fosstars.nvd.data.NvdEntry;
import java.io.IOException;
import java.util.Objects;

/**
 * This data provider looks for vulnerabilities in NVD.
 */
public class VulnerabilitiesFromNvd extends CachedSingleFeatureGitHubDataProvider<Vulnerabilities> {

  /**
   * A feature that hold info about vulnerabilities in the NVD.
   */
  private static final Feature<Vulnerabilities> VULNERABILITIES_IN_NVD
      = new VulnerabilitiesFeature(
      "Info about vulnerabilities in an open-source project from NVD");

  /**
   * An interface to NVD.
   */
  private final NVD nvd;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @param nvd An interface to NVD.
   */
  public VulnerabilitiesFromNvd(GitHubDataFetcher fetcher, NVD nvd) {
    super(fetcher);
    this.nvd = Objects.requireNonNull(nvd, "NVD can't be null!");
  }

  @Override
  protected Feature<Vulnerabilities> supportedFeature() {
    return VULNERABILITIES_IN_NVD;
  }

  @Override
  protected Value<Vulnerabilities> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Looking for vulnerabilities in NVD ...");

    Vulnerabilities vulnerabilities = new Vulnerabilities();
    for (NvdEntry entry : nvd.search(entriesFor(project))) {
      vulnerabilities.add(Vulnerability.Builder.from(entry).make());
    }

    logger.info("Found {} {}", vulnerabilities.size(),
        vulnerabilities.size() == 1 ? "vulnerability" : "vulnerabilities");
    return VULNERABILITIES_IN_NVD.value(vulnerabilities);
  }
}
