package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static java.lang.Boolean.FALSE;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * This data provider tries to fill out the {@link OssFeatures#SCANS_FOR_VULNERABLE_DEPENDENCIES}.
 * feature. It is based on the following data providers:
 * <ul>
 *   <li>{@link UsesOwaspDependencyCheck}</li>
 * </ul>
 */
public class ScansForVulnerableDependencies extends CachedSingleFeatureGitHubDataProvider {

  /**
   * A list of underlying data providers.
   */
  private final List<CachedSingleFeatureGitHubDataProvider> providers;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public ScansForVulnerableDependencies(GitHubDataFetcher fetcher) {
    super(fetcher);
    providers = Arrays.asList(
        new UsesOwaspDependencyCheck(fetcher)
    );
  }

  @Override
  protected Feature supportedFeature() {
    return SCANS_FOR_VULNERABLE_DEPENDENCIES;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    for (CachedSingleFeatureGitHubDataProvider provider : providers) {
      Value<Boolean> value = provider.fetchValueFor(project);
      if (value.orElse(FALSE)) {
        return SCANS_FOR_VULNERABLE_DEPENDENCIES.value(true);
      }
    }

    return SCANS_FOR_VULNERABLE_DEPENDENCIES.value(false);
  }
}
