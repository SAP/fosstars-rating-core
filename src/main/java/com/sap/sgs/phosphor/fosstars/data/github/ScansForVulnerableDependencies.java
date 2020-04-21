package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.kohsuke.github.GitHub;

/**
 * This data provider tries to fill out the
 * {@link
 * com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#SCANS_FOR_VULNERABLE_DEPENDENCIES}.
 * feature.
 * It is based on a number of data providers:
 * <ul>
 *   <li>{@link UsesOwaspDependencyCheck}</li>
 *   <li>{@link UsesSnykDependencyCheck}</li>
 * </ul>
 */
public class ScansForVulnerableDependencies extends CachedSingleFeatureGitHubDataProvider {

  private final List<CachedSingleFeatureGitHubDataProvider> providers;

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public ScansForVulnerableDependencies(GitHub github) {
    super(github);
    providers = Arrays.asList(
        new UsesOwaspDependencyCheck(github),
        new UsesSnykDependencyCheck(github)
    );
    // TODO: include UsesDependabot provider
  }

  @Override
  protected Feature supportedFeature() {
    return SCANS_FOR_VULNERABLE_DEPENDENCIES;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    for (CachedSingleFeatureGitHubDataProvider provider : providers) {
      Value value = provider.fetchValueFor(project);
      if (!value.isUnknown() && Boolean.TRUE.equals(value.get())) {
        return SCANS_FOR_VULNERABLE_DEPENDENCIES.value(true);
      }
    }

    return SCANS_FOR_VULNERABLE_DEPENDENCIES.unknown();
  }
}
