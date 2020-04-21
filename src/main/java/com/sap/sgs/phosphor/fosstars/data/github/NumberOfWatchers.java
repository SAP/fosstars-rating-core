package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import org.kohsuke.github.GitHub;

/**
 * This data provider returns a number of watchers for a project.
 */
public class NumberOfWatchers extends CachedSingleFeatureGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public NumberOfWatchers(GitHub github) {
    super(github);
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
        gitHubDataFetcher().repositoryFor(project, github).getSubscribersCount());
  }
}
