package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * This data provider returns a number of commits last 3 months.
 */
public class NumberOfCommits extends CachedSingleFeatureGitHubDataProvider {

  /**
   * ~3 months.
   */
  private static final Duration THREE_MONTHS = Duration.ofDays(90);

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public NumberOfCommits(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature supportedFeature() {
    return NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Counting how many commits have been done in the last three months ...");

    Date date = Date.from(Instant.now().minus(THREE_MONTHS));
    return NUMBER_OF_COMMITS_LAST_THREE_MONTHS.value(
        fetcher.localRepositoryFor(project).commitsAfter(date).size());
  }
}
