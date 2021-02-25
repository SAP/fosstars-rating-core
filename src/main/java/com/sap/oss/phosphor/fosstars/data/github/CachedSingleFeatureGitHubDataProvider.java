package com.sap.oss.phosphor.fosstars.data.github;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * This is a base class for data providers that fill out a single feature for a project on GitHub.
 *
 * @param <F> Type of data held by the feature.
 */
public abstract class CachedSingleFeatureGitHubDataProvider<F> extends GitHubCachingDataProvider {

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
  public final Set<Feature<?>> supportedFeatures() {
    return Collections.singleton(supportedFeature());
  }

  /**
   * Get a feature that the provider can fill out.
   *
   * @return A feature that the provider can fill out.
   */
  protected abstract Feature<F> supportedFeature();

  /**
   * Fetch a feature value for a project.
   *
   * @param project The project.
   * @return The value.
   * @throws IOException If something went wrong.
   */
  protected abstract Value<F> fetchValueFor(GitHubProject project) throws IOException;
}
