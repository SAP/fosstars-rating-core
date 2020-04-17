package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FIRST_COMMIT_DATE;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GitHub;

/**
 * This data provider returns a date of the first commit.
 */
public class FirstCommit extends AbstractGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public FirstCommit(GitHub github) {
    super(github);
  }

  @Override
  protected FirstCommit doUpdate(GitHubProject project, ValueSet values) throws IOException {
    logger.info("Figuring out when the first commit was done ...");

    Value<Date> firstCommit = firstCommitDate(project);
    values.update(firstCommit);

    return this;
  }

  /**
   * Looks for the first commit and returns a value with its date.
   *
   * @return An instance of {@link Value} which contains a date of the first commit.
   * @throws IOException If something went wrong.
   */
  Value<Date> firstCommitDate(GitHubProject project) throws IOException {
    Optional<Value> something = cache.get(project, FIRST_COMMIT_DATE);
    if (something.isPresent()) {
      return something.get();
    }

    Value<Date> value = FIRST_COMMIT_DATE.unknown();
    Optional<GHCommit> firstCommit = gitHubDataFetcher().firstCommitFor(project, github);
    if (firstCommit.isPresent()) {
      value = FIRST_COMMIT_DATE.value(firstCommit.get().getCommitDate());
    }
    cache.put(project, value, tomorrow());
    return value;
  }
}
