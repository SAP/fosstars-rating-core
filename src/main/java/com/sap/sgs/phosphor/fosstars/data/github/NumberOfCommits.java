package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Date;
import org.kohsuke.github.GitHub;

/**
 * This data provider returns a number of commits last 3 months.
 */
public class NumberOfCommits extends CachedSingleFeatureGitHubDataProvider {

  /**
   * 3 months in millis.
   */
  private static final long DELTA = 90 * 24 * 60 * 60 * 1000L;

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public NumberOfCommits(GitHub github) {
    super(github);
  }

  @Override
  protected Feature supportedFeature() {
    return NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Counting how many commits have been done in the last three months ...");

    // TODO: define a date in a modern way
    Date date = new Date(System.currentTimeMillis() - DELTA);
    return NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(
        gitHubDataFetcher().commitsAfter(date, project, github).size());
  }
}
