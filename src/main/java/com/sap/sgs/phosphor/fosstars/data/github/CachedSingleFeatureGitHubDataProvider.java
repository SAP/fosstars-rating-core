package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import org.kohsuke.github.GitHub;

/**
 * This is a base class for data providers that fill out a single feature for a project on GitHub.
 */
public abstract class CachedSingleFeatureGitHubDataProvider extends GitHubCachingDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public CachedSingleFeatureGitHubDataProvider(GitHub github) {
    super(github);
  }

  @Override
  protected final Set<Value> fetchValuesFor(GitHubProject project) throws IOException {
    return Collections.singleton(fetchValueFor(project));
  }

  @Override
  protected final Set<Feature> supportedFeatures() {
    return Collections.singleton(supportedFeature());
  }

  /**
   * Returns a feature that the provider can fill out.
   */
  protected abstract Feature supportedFeature();

  /**
   * Fetch a feature value for a project.
   *
   * @param project The project.
   * @return The value.
   */
  protected abstract Value fetchValueFor(GitHubProject project) throws IOException;
}
