package com.sap.oss.phosphor.fosstars.data.github;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHUser;

/**
 * This is a base class for dependency checker data providers such as Dependabot and Snyk.
 */
public abstract class AbstractDependencyScanDataProvider extends GitHubCachingDataProvider {

  /**
   * Period of time to be checked.
   */
  private static final Duration ONE_YEAR = Duration.ofDays(365);

  /**
   * A minimal number of characters in a config for dependency checker.
   */
  private static final int ACCEPTABLE_CONFIG_SIZE = 10;

  protected abstract String getDependencyCheckerPattern();

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public AbstractDependencyScanDataProvider(
      GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  /**
   * Checks if a repository contains commits from dependency checker in the commit history.
   *
   * @param repository The repository.
   * @return True if at least one commit from dependency checker was found, false otherwise.
   */
  public boolean hasDependencyCheckerCommits(LocalRepository repository) {
    Date date = Date.from(Instant.now().minus(ONE_YEAR));

    try {
      for (Commit commit : repository.commitsAfter(date)) {
        if (isDependencyChecker(commit)) {
          return true;
        }
      }
    } catch (IOException e) {
      logger.warn("Something went wrong!", e);
    }

    return false;
  }

  /**
   * Checks if a repository has a configuration file for dependency checker.
   *
   * @param repository The repository
   * @param configs The config files path as String array
   * @return True if a config was found, false otherwise.
   * @throws IOException If something went wrong.
   */
  public boolean hasDependencyCheckerConfig(LocalRepository repository, String[] configs)
      throws IOException {
    for (String config : configs) {
      Optional<String> content = repository.file(config);
      if (content.isPresent() && content.get().length() >= ACCEPTABLE_CONFIG_SIZE) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks whether a project has open pull requests from dependency checker.
   *
   * @param project The project.
   * @return True if the project has open pull requests form dependency checker.
   * @throws IOException If something went wrong.
   */
  public boolean hasOpenPullRequestFromDependencyChecker(GitHubProject project) throws IOException {
    return fetcher.repositoryFor(project).getPullRequests(GHIssueState.OPEN).stream()
        .anyMatch(this::createdByDependencyChecker);
  }

  /**
   * Checks if a pull request was created by dependency checker.
   *
   * @param pullRequest The pull request.
   * @return True if the user looks like dependency checker, false otherwise.
   */
  private boolean createdByDependencyChecker(GHPullRequest pullRequest) {
    try {
      GHUser user = pullRequest.getUser();
      return isDependencyChecker(user.getName()) || isDependencyChecker(user.getLogin());
    } catch (IOException e) {
      logger.warn("Oops! Could not fetch name or login!", e);
      return false;
    }
  }

  /**
   * Checks if a commit was done by dependency checker.
   *
   * @param commit The commit to be checked.
   * @return True if the commit was done by dependency checker, false otherwise.
   */
  private boolean isDependencyChecker(Commit commit) {
    if (isDependencyChecker(commit.authorName()) || isDependencyChecker(commit.committerName())) {
      return true;
    }

    for (String line : commit.message()) {
      if ((line.startsWith("Signed-off-by:") || line.startsWith("Co-authored-by:"))
          && line.contains(getDependencyCheckerPattern())) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks whether a name looks like dependency checker.
   *
   * @param name The name.
   * @return True if the name looks like dependency checker, false otherwise.
   */
  private boolean isDependencyChecker(String name) {
    return name != null && name.toLowerCase().contains(getDependencyCheckerPattern());
  }
}
