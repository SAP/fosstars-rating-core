package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FIRST_COMMIT_DATE;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterator;

/**
 * This data provider returns a date of the first commit.
 */
public class FirstCommit extends AbstractGitHubDataProvider {

  /**
   * A size of a page for GitHub API.
   */
  private static final int PAGE_SIZE = 10000;

  /**
   * 7 days in millis.
   */
  private static final long DELTA = 7 * 24 * 60 * 60 * 1000L;

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

    GHRepository repository = github.getRepository(project.path());
    long millis = repository.getCreatedAt().getTime() + DELTA;

    PagedIterator<GHCommit> iterator = repository.queryCommits()
        .until(millis)
        .list()
        .withPageSize(PAGE_SIZE)
        .iterator();

    List<GHCommit> lastPage = null;
    while (iterator.hasNext()) {
      lastPage = iterator.nextPage();
    }

    if (lastPage == null) {
      return UnknownValue.of(FIRST_COMMIT_DATE);
    }

    Value<Date> value = FIRST_COMMIT_DATE.value(lastPage.get(lastPage.size() - 1).getCommitDate());
    cache.put(project, value);

    return value;
  }
}
