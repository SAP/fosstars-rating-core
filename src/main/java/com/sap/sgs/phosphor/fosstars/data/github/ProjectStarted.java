package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GitHub;

/**
 * This data provider estimates a date when a project was created by the date when the repository
 * was created.
 */
// TODO: don't extend FirstCommit
public class ProjectStarted extends CachedSingleFeatureGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public ProjectStarted(GitHub github) {
    super(github);
  }

  @Override
  protected Feature supportedFeature() {
    return PROJECT_START_DATE;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out when the project started ...");

    Optional<GHCommit> firstCommit = gitHubDataFetcher().firstCommitFor(project, github);
    Date firstCommitDate = firstCommit.isPresent() ? firstCommit.get().getCommitDate() : null;
    Date repositoryCreated = gitHubDataFetcher().repositoryFor(project, github).getCreatedAt();

    if (firstCommitDate != null && repositoryCreated != null
        && firstCommitDate.before(repositoryCreated)) {
      return PROJECT_START_DATE.value(firstCommitDate);
    } else if (repositoryCreated != null) {
      return PROJECT_START_DATE.value(repositoryCreated);
    }

    return PROJECT_START_DATE.unknown();
  }
}
