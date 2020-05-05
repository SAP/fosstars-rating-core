package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.VulnerabilitiesInProject;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import com.sap.sgs.phosphor.fosstars.nvd.NVD;
import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Objects;

/**
 * This data provider looks for vulnerabilities in NVD.
 */
public class VulnerabilitiesFromNvd extends CachedSingleFeatureGitHubDataProvider {

  /**
   * A feature that hold info about vulnerabilities in the NVD.
   */
  private static final Feature<Vulnerabilities> VULNERABILITIES_IN_NVD
      = new VulnerabilitiesInProject();

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
  protected Feature supportedFeature() {
    return VULNERABILITIES_IN_NVD;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Looking for vulnerabilities in NVD ...");

    Vulnerabilities vulnerabilities = new Vulnerabilities();
    for (NvdEntry entry : nvd.search(VendorDataMatcher.with(project))) {
      vulnerabilities.add(Vulnerability.Builder.from(entry).make());
    }

    return VULNERABILITIES_IN_NVD.value(vulnerabilities);
  }
}
