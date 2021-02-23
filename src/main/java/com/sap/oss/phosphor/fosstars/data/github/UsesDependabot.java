package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHUser;

/**
 * <p>This data provider checks if an open-source project on GitHub
 * uses Dependabot, and fills out the {@link OssFeatures#USES_DEPENDABOT} feature.</p>
 *
 * <p>First, the provider checks if a repository contains a configuration file for Dependabot.
 * If the config exists, then the provider reports that the projects uses Dependabot.
 * Next, the provider searches for commits from Dependabot in the commit history.
 * If the commits are found, then the provider also reports that the project uses Dependabot.</p>
 */
public class UsesDependabot extends GitHubCachingDataProvider {

  /**
   * A list of locations of a Dependabot configuration file in a repository.
   *
   * @see <a href="https://dependabot.com/docs/config-file/">Dependabot config files</a>
   * @see <a href="https://docs.github.com/en/free-pro-team@latest/github/managing-security-vulnerabilities/managing-vulnerabilities-in-your-projects-dependencies">Managing vulnerabilities in your project's dependencies</a>
   */
  private static final String[] DEPENDABOT_CONFIGS = {
      ".dependabot/config.yml",
      ".github/dependabot.yml"
  };

  /**
   * A minimal number of characters in a config for Dependabot.
   */
  private static final int ACCEPTABLE_CONFIG_SIZE = 10;

  /**
   * A pattern to detect commits by Dependabot.
   */
  private static final String DEPENDABOT_PATTERN = "dependabot";

  /**
   * Period of time to be checked.
   */
  private static final Duration ONE_YEAR = Duration.ofDays(365);

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesDependabot(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Set<Feature<?>> supportedFeatures() {
    return setOf(USES_DEPENDABOT, HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Checking how the project uses Dependabot ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    return ValueHashSet.from(
        USES_DEPENDABOT.value(hasDependabotConfig(repository) || hasDependabotCommits(repository)),
        HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT.value(hasOpenPullRequestFromDependabot(project)));
  }

  /**
   * Checks if a repository contains commits from Dependabot in the commit history.
   *
   * @param repository The repository.
   * @return True if at least one commit from Dependabot was found, false otherwise.
   */
  private boolean hasDependabotCommits(LocalRepository repository) {
    Date date = Date.from(Instant.now().minus(ONE_YEAR));

    try {
      for (Commit commit : repository.commitsAfter(date)) {
        if (isDependabot(commit)) {
          return true;
        }
      }
    } catch (IOException e) {
      logger.warn("Something went wrong!", e);
    }

    return false;
  }

  /**
   * Checks if a repository has a configuration file for Dependabot.
   *
   * @param repository The repository
   * @return True if a config was found, false otherwise.
   */
  private boolean hasDependabotConfig(LocalRepository repository) throws IOException {
    for (String config : DEPENDABOT_CONFIGS) {
      Optional<String> content = repository.file(config);
      if (content.isPresent() && content.get().length() >= ACCEPTABLE_CONFIG_SIZE) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks whether a project has open pull requests from Dependabot.
   *
   * @param project The project.
   * @return True if the project has open pull requests form Dependabot.
   * @throws IOException If something went wrong.
   */
  private boolean hasOpenPullRequestFromDependabot(GitHubProject project) throws IOException {
    return fetcher.repositoryFor(project).getPullRequests(GHIssueState.OPEN).stream()
        .anyMatch(this::createdByDependabot);
  }

  /**
   * Checks if a pull request was created by Dependabot.
   *
   * @param pullRequest The pull request.
   * @return True if the user looks like Dependabot, false otherwise.
   */
  private boolean createdByDependabot(GHPullRequest pullRequest) {
    try {
      GHUser user = pullRequest.getUser();
      return user.getName().toLowerCase().contains(DEPENDABOT_PATTERN)
          || user.getLogin().toLowerCase().contains(DEPENDABOT_PATTERN);
    } catch (IOException e) {
      logger.warn("Oops! Could not fetch name or login!", e);
      return false;
    }
  }

  /**
   * Checks if a commit was done by Dependabot.
   *
   * @param commit The commit to be checked.
   * @return True if the commit was done by Dependabot, false otherwise.
   */
  private static boolean isDependabot(Commit commit) {
    if (commit.authorName().toLowerCase().contains(DEPENDABOT_PATTERN)
        || commit.committerName().toLowerCase().contains(DEPENDABOT_PATTERN)) {
      return true;
    }

    for (String line : commit.message()) {
      if ((line.startsWith("Signed-off-by:") || line.startsWith("Co-authored-by:"))
          && line.contains(DEPENDABOT_PATTERN)) {
        return true;
      }
    }

    return false;
  }
}