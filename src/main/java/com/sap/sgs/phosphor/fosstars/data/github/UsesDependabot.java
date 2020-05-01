package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitUser;

/**
 * <p>This data provider checks if an open-source project on GitHub
 * uses Dependabot, and fills out the
 * {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_DEPENDABOT} feature.</p>
 *
 * <p>First, the provider checks if a repository contains a configuration file for Dependabot.
 * If the config exists, then the provider reports that the projects uses Dependabot.
 * Next, the provider searches for commits from Dependabot in the commit history.
 * If the commits are found, then the provider also reports that the project uses Dependabot.</p>
 */
public class UsesDependabot extends CachedSingleFeatureGitHubDataProvider {

  /**
   * A path to Dependabot configuration file in a repository.
   */
  private static final String DEPENDABOT_CONFIG = ".dependabot/config.yml";

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
  private static final Duration LATEST_MONTHS = Duration.ofDays(365);

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public UsesDependabot(GitHub github) {
    super(github);
  }

  @Override
  protected Feature supportedFeature() {
    return USES_DEPENDABOT;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Checking if the project uses Dependabot ...");
    return usesDependabot(project);
  }

  /**
   * Checks if a repository contains commits from Dependabot in the commit history.
   *
   * @param commits A list of commits after a specific date.
   * @return True if at least one commit from Dependabot was found, false otherwise.
   */
  private boolean hasDependabotCommits(List<GHCommit> commits) {
    try {
      for (GHCommit commit : commits) {
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
  private boolean hasDependabotConfig(GHRepository repository) {
    try {
      GHContent content = repository.getFileContent(DEPENDABOT_CONFIG);

      if (content == null) {
        return false;
      }

      if (!content.isFile()) {
        return false;
      }

      return content.getSize() >= ACCEPTABLE_CONFIG_SIZE;
    } catch (GHFileNotFoundException e) {
      return false; // okay, the config doesn't exist
    } catch (IOException e) {
      logger.warn("Something went wrong!", e);
    }

    return false;
  }

  /**
   * Checks if a project uses Dependabot.
   *
   * @return A value for the
   *         {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_DEPENDABOT}
   *         feature.
   */
  private Value<Boolean> usesDependabot(GitHubProject project) throws IOException {
    Date date = Date.from(Instant.now().minus(LATEST_MONTHS));
    GitHubDataFetcher fetcher = gitHubDataFetcher();

    return USES_DEPENDABOT.value(
        hasDependabotConfig(fetcher.repositoryFor(project, github))
            || hasDependabotCommits(fetcher.commitsAfter(date, project, github)));
  }

  /**
   * Checks if a commit was done by Dependabot.
   *
   * @param commit The commit to be checked.
   * @return True if the commit was done by Dependabot, false otherwise.
   * @throws IOException If something went wrong.
   */
  private static boolean isDependabot(GHCommit commit) throws IOException {
    if (isDependabot(commit.getAuthor()) || isDependabot(commit.getCommitter())) {
      return true;
    }

    GHCommit.ShortInfo info = commit.getCommitShortInfo();
    if (info == null) {
      return false;
    }

    return isDependabot(info.getAuthor()) || isDependabot(info.getCommitter());
  }

  /**
   * Check if a GitHub user looks like Dependabot.
   *
   * @param user The user to be checked.
   * @return True if the user looks like Dependabot, false otherwise.
   */
  private static boolean isDependabot(GHUser user) {
    return user != null
        && user.getLogin() != null
        && user.getLogin().toLowerCase().contains(DEPENDABOT_PATTERN);
  }

  /**
   * Check if a git user looks like Dependabot.
   *
   * @param user The user to be checked.
   * @return True if the user looks like Dependabot, false otherwise.
   */
  private static boolean isDependabot(GitUser user) {
    return user != null
        && user.getName() != null
        && user.getName().toLowerCase().contains(DEPENDABOT_PATTERN);
  }
}