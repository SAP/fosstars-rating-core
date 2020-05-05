package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution.UNPATCHED;

import com.sap.sgs.phosphor.fosstars.data.json.UnpatchedVulnerabilitiesStorage;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.VulnerabilitiesInProject;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;
import com.sap.sgs.phosphor.fosstars.nvd.NVD;
import com.sap.sgs.phosphor.fosstars.nvd.data.CpeMatch;
import com.sap.sgs.phosphor.fosstars.nvd.data.Node;
import com.sap.sgs.phosphor.fosstars.nvd.data.NvdEntry;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * This data provider tries to figure out if an open-source project has unpatched vulnerabilities.
 */
public class UnpatchedVulnerabilities extends CachedSingleFeatureGitHubDataProvider {

  /**
   * A feature that hold info about unpatched vulnerabilities.
   */
  private static final Feature<Vulnerabilities> UNPATCHED_VULNERABILITIES
      = new VulnerabilitiesInProject();

  /**
   * A storage of unpatched vulnerabilities.
   */
  private final UnpatchedVulnerabilitiesStorage knownUnpatchedVulnerabilities;

  /**
   * An interface to NVD.
   */
  private final NVD nvd;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @param nvd An interface to NVD.
   * @throws IOException If the info about unpatched vulnerabilities can't be loaded.
   */
  public UnpatchedVulnerabilities(GitHubDataFetcher fetcher, NVD nvd) throws IOException {
    super(fetcher);
    this.nvd = Objects.requireNonNull(nvd, "NVD can't be null!");
    knownUnpatchedVulnerabilities = UnpatchedVulnerabilitiesStorage.load();
  }

  @Override
  protected Feature supportedFeature() {
    return UNPATCHED_VULNERABILITIES;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) {
    logger.info("Figuring out if the project has any unpatched vulnerability ...");

    Vulnerabilities vulnerabilities = new Vulnerabilities();
    vulnerabilities.add(knownUnpatchedVulnerabilities.getFor(project.url()));
    vulnerabilities.add(vulnerabilitiesFromNvdFor(project));

    return UNPATCHED_VULNERABILITIES.value(vulnerabilities);
  }

  /**
   * Looks for unpatched vulnerabilities in NVD for a specified project.
   *
   * @param project The project.
   * @return Unpatched vulnerabilities.
   */
  Vulnerabilities vulnerabilitiesFromNvdFor(GitHubProject project) {
    Vulnerabilities vulnerabilities = new Vulnerabilities();
    for (NvdEntry entry : nvd.search(VendorDataMatcher.with(project))) {
      if (isUnpatched(entry)) {
        vulnerabilities.add(
            Vulnerability.Builder.from(entry)
                .set(UNPATCHED)
                .noFixDate()
                .make());
      }
    }
    return vulnerabilities;
  }

  /**
   * Checks if an entry from NVD looks like an unpatched vulnerability
   * by analyzing configurations.
   *
   * @param entry The entry from NVD.
   * @return True if the entry looks like an unpatched vulnerability, false otherwise.
   */
  private static boolean isUnpatched(NvdEntry entry) {
    if (entry.getConfigurations() == null || entry.getConfigurations().getNodes() == null) {
      return false;
    }

    for (Node node : entry.getConfigurations().getNodes()) {
      if (node == null || node.getCpeMatch() == null) {
        continue;
      }

      for (CpeMatch cpeMatch : node.getCpeMatch()) {
        if (cpeMatch.getVulnerable() && noVersionEnd(cpeMatch)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Checks if a CPE match doesn't have
   * either "versionEndIncluding" or "versionEndExcluding" fields.
   *
   * @param cpeMatch The CPE match to be checked.
   * @return True if the CPE match doesn't have the fields, false otherwise.
   */
  private static boolean noVersionEnd(CpeMatch cpeMatch) {
    return StringUtils.isEmpty(cpeMatch.getVersionEndExcluding())
        && StringUtils.isEmpty(cpeMatch.getVersionEndIncluding());
  }

}
