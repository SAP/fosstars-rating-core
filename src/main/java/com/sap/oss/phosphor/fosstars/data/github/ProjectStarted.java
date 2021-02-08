package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

/**
 * This data provider estimates a date when a project was created by the date when the repository
 * was created.
 */
public class ProjectStarted extends CachedSingleFeatureGitHubDataProvider<Date> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public ProjectStarted(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Date> supportedFeature() {
    return PROJECT_START_DATE;
  }

  @Override
  protected Value<Date> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out when the project started ...");

    Optional<Commit> firstCommit = GitHubDataFetcher.localRepositoryFor(project).firstCommit();
    Date firstCommitDate = firstCommit.map(Commit::date).orElse(null);
    Date repositoryCreated = fetcher.repositoryFor(project).getCreatedAt();

    if (firstCommitDate != null && repositoryCreated != null
        && firstCommitDate.before(repositoryCreated)) {
      return PROJECT_START_DATE.value(firstCommitDate);
    } else if (repositoryCreated != null) {
      return PROJECT_START_DATE.value(repositoryCreated);
    }

    return PROJECT_START_DATE.unknown();
  }
}
