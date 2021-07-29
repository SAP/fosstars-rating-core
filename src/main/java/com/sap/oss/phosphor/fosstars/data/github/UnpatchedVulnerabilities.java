package com.sap.oss.phosphor.fosstars.data.github;

import com.sap.oss.phosphor.fosstars.data.json.UnpatchedVulnerabilitiesStorage;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.VulnerabilitiesFeature;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import java.io.IOException;

/**
 * This data provider tries to figure out if an open-source project has unpatched vulnerabilities.
 */
public class UnpatchedVulnerabilities
    extends CachedSingleFeatureGitHubDataProvider<Vulnerabilities> {

  /**
   * A feature that hold info about unpatched vulnerabilities.
   */
  private static final Feature<Vulnerabilities> UNPATCHED_VULNERABILITIES
      = new VulnerabilitiesFeature(
          "Info about unpatched vulnerabilities in an open-source project");

  /**
   * A storage of unpatched vulnerabilities.
   */
  private final UnpatchedVulnerabilitiesStorage knownUnpatchedVulnerabilities;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If the info about unpatched vulnerabilities can't be loaded.
   */
  public UnpatchedVulnerabilities(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
    knownUnpatchedVulnerabilities = UnpatchedVulnerabilitiesStorage.load();
  }

  @Override
  protected Feature<Vulnerabilities> supportedFeature() {
    return UNPATCHED_VULNERABILITIES;
  }

  @Override
  protected Value<Vulnerabilities> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project has any unpatched vulnerability ...");

    Vulnerabilities vulnerabilities = new Vulnerabilities();
    vulnerabilities.add(knownUnpatchedVulnerabilities.getFor(project.scm()));

    return UNPATCHED_VULNERABILITIES.value(vulnerabilities);
  }
}
