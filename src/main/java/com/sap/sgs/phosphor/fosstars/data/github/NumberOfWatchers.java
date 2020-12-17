package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;

/**
 * This data provider returns a number of watchers for a project.
 */
public class NumberOfWatchers extends CachedSingleFeatureGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public NumberOfWatchers(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature supportedFeature() {
    return NUMBER_OF_WATCHERS_ON_GITHUB;
  }

  /**
   * Looks for a number of watchers for a project on GitHub.
   *
   * @param project The project.
   * @return The number of watchers.
   * @throws IOException If something went wrong.
   */
  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Counting how many watchers the project has ...");
    return NUMBER_OF_WATCHERS_ON_GITHUB.value(
        fetcher.repositoryFor(project).getSubscribersCount());
  }
}
