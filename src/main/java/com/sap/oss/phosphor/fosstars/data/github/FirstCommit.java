package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FIRST_COMMIT_DATE;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * This data provider returns a date of the first commit.
 */
public class FirstCommit extends CachedSingleFeatureGitHubDataProvider<Date> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public FirstCommit(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Date> supportedFeature() {
    return FIRST_COMMIT_DATE;
  }

  @Override
  protected Value<Date> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out when the first commit was done ...");
    return firstCommitDate(project);
  }

  /**
   * Looks for the first commit and returns a value with its date.
   *
   * @return An instance of {@link Value} which contains a date of the first commit.
   * @throws IOException If something went wrong.
   */
  private Value<Date> firstCommitDate(GitHubProject project) throws IOException {
    Optional<Commit> firstCommit = GitHubDataFetcher.localRepositoryFor(project).firstCommit();
    if (firstCommit.isPresent()) {
      return FIRST_COMMIT_DATE.value(firstCommit.get().date());
    }

    return FIRST_COMMIT_DATE.unknown();
  }
}
