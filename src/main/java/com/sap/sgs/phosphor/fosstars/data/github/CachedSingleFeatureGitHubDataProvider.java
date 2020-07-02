package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * This is a base class for data providers that fill out a single feature for a project on GitHub.
 */
public abstract class CachedSingleFeatureGitHubDataProvider extends GitHubCachingDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public CachedSingleFeatureGitHubDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected final ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    return ValueHashSet.from(fetchValueFor(project));
  }

  @Override
  protected final Set<Feature> supportedFeatures() {
    return Collections.singleton(supportedFeature());
  }

  /**
   * Get a feature that the provider can fill out.
   *
   * @return A feature that the provider can fill out.
   */
  protected abstract Feature supportedFeature();

  /**
   * Fetch a feature value for a project.
   *
   * @param project The project.
   * @return The value.
   * @throws IOException If something went wrong.
   */
  protected abstract Value fetchValueFor(GitHubProject project) throws IOException;
}
