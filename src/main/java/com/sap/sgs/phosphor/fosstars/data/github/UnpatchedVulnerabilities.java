package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.data.json.UnpatchedVulnerabilitiesStorage;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.VulnerabilitiesInProject;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import org.kohsuke.github.GitHub;

/**
 * This data provider tries to figure out if an open-source project has unpatched vulnerabilities.
 */
public class UnpatchedVulnerabilities extends CachedSingleFeatureGitHubDataProvider {

  /**
   * A feature that hold info about unpatched vulnerabilities.
   */
  public static final Feature<Vulnerabilities> UNPATCHED_VULNERABILITIES
      = new VulnerabilitiesInProject();

  /**
   * A storage of unpatched vulnerabilities.
   */
  private final UnpatchedVulnerabilitiesStorage unpatchedVulnerabilities;

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   * @throws IOException If the info about unpatched vulnerabilities can't be loaded.
   */
  public UnpatchedVulnerabilities(GitHub github) throws IOException {
    super(github);
    unpatchedVulnerabilities = UnpatchedVulnerabilitiesStorage.load();
  }

  @Override
  protected Feature supportedFeature() {
    return UNPATCHED_VULNERABILITIES;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) {
    logger.info("Figuring out if the project has any unpatched vulnerability ...");
    return UNPATCHED_VULNERABILITIES.value(unpatchedVulnerabilities.getFor(project.url()));
  }
}
