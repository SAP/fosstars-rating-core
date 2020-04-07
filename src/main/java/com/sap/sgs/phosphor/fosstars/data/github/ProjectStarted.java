package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.DateValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Date;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider estimates a date when a project was created by the date when the repository
 * was created.
 */
public class ProjectStarted extends FirstCommit {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public ProjectStarted(GitHub github) {
    super(github);
  }

  @Override
  public ProjectStarted doUpdate(GitHubProject project, ValueSet values) throws IOException {
    logger.info("Figuring out when the project started ...");

    Value<Date> firstCommitDate = firstCommitDate(project);

    GHRepository repository = github.getRepository(project.path());
    Date repositoryCreated = repository.getCreatedAt();

    Value<Date> projectStarted = PROJECT_START_DATE.unknown();
    if (!firstCommitDate.isUnknown() && firstCommitDate.get().before(repositoryCreated)) {
      projectStarted = new DateValue(PROJECT_START_DATE, firstCommitDate.get());
    } else if (repositoryCreated != null) {
      projectStarted = new DateValue(PROJECT_START_DATE, repositoryCreated);
    }

    values.update(projectStarted);

    return this;
  }
}
