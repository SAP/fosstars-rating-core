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
   * The flag shows if the provider should look for unpatched vulnerabilities in NVD.
   * This is an unstable experimental feature.
   */
  private static boolean ANALYZE_NVD = Boolean.getBoolean("lookForUnpatchedIssuesInNVD");

  /**
   * A feature that hold info about unpatched vulnerabilities.
   */
  private static final Feature<Vulnerabilities> UNPATCHED_VULNERABILITIES
      = new VulnerabilitiesInProject(
          "Info about unpatched vulnerabilities in an open-source project");

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

    if (ANALYZE_NVD) {
      vulnerabilities.add(vulnerabilitiesFromNvdFor(project));
    }

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
    for (NvdEntry entry : nvd.search(NvdEntryMatcher.entriesFor(project))) {
      if (isUnpatched(entry, project)) {
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
   * Checks if an entry from NVD looks like an unpatched vulnerability in a project.
   *
   * @param entry The entry from NVD.
   * @param project The project.
   * @return True if the entry looks like an unpatched vulnerability, false otherwise.
   */
  private static boolean isUnpatched(NvdEntry entry, GitHubProject project) {
    /*
     * This method is currently unreliable. It produces a lot of false-positives.
     *
     * Here are some ideas to make it better.
     * 1. Ignore entries that don't contains configurations and CPE matches.
     * 2. Take into account versions in a CPE match string.
     * 3. Take into account start versions in a CPE match string.
     * 4. Take into account the operator field in configurations.
     *
     * In general, the data in NVD may not be always correct. The NVD team may need to be contacted
     * to address issues in NVD entries.
     */
    if (entry.getConfigurations() == null || entry.getConfigurations().getNodes() == null) {
      return false;
    }

    for (Node node : entry.getConfigurations().getNodes()) {
      if (node == null || node.getCpeMatches() == null) {
        continue;
      }

      for (CpeMatch cpeMatch : node.getCpeMatches()) {
        if (cpeMatch == null || !cpeMatch.getVulnerable() || !match(cpeMatch, project)) {
          continue;
        }

        if (hasEndVersion(cpeMatch)) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Checks if a {@link CpeMatch} matches to a project.
   *
   * @param cpeMatch The {@link CpeMatch} to be checked.
   * @param project The project.
   * @return True if one of the CPE URIs in a CpeMatch element matches a project, false otherwise.
   */
  private static boolean match(CpeMatch cpeMatch, GitHubProject project) {
    return match(cpeMatch.getCpe22Uri(), project) || match(cpeMatch.getCpe23Uri(), project);
  }

  /**
   * Checks if a string matcher to a project.
   *
   * @param string The string.
   * @param project The project.
   * @return True if the string matches to the project, false otherwise.
   */
  private static boolean match(String string, GitHubProject project) {
    return string != null && string.toLowerCase().contains(project.name());
  }

  /**
   * Checks if a CPE match has either "versionEndIncluding" or "versionEndExcluding" fields.
   *
   * @param cpeMatch The CPE match to be checked.
   * @return True if the CPE match doesn't have the fields, false otherwise.
   */
  private static boolean hasEndVersion(CpeMatch cpeMatch) {
    return !StringUtils.isEmpty(cpeMatch.getVersionEndExcluding())
        || !StringUtils.isEmpty(cpeMatch.getVersionEndIncluding());
  }
}
