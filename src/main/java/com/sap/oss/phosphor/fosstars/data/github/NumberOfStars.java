package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;

/**
 * This data provider returns a number of stars for a project.
 */
public class NumberOfStars extends CachedSingleFeatureGitHubDataProvider<Integer> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public NumberOfStars(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Integer> supportedFeature() {
    return NUMBER_OF_GITHUB_STARS;
  }

  /**
   * Looks for a number of stars for a project on GitHub.
   *
   * @param project The project.
   * @return The number of stars.
   * @throws IOException If something went wrong.
   */
  @Override
  protected Value<Integer> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Counting how many stars the project has ...");
    return NUMBER_OF_GITHUB_STARS.value(
        fetcher.repositoryFor(project).getStargazersCount());
  }

}
