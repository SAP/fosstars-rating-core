package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * <p>This data provider checks if an open-source project on GitHub
 * uses Dependabot, and fills out the {@link OssFeatures#USES_DEPENDABOT} feature.</p>
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
    return repository.file(DEPENDABOT_CONFIG)
        .filter(content -> content.length() >= ACCEPTABLE_CONFIG_SIZE)
        .isPresent();
  }

  /**
   * Checks if a project uses Dependabot.
   *
   * @return A value for the {@link OssFeatures#USES_DEPENDABOT} feature.
   */
  private Value<Boolean> usesDependabot(GitHubProject project) throws IOException {
    LocalRepository repository = fetcher.localRepositoryFor(project);
    return USES_DEPENDABOT.value(
        hasDependabotConfig(repository)
            || hasDependabotCommits(repository));
  }

  /**
   * Checks if a commit was done by Dependabot.
   *
   * @param commit The commit to be checked.
   * @return True if the commit was done by Dependabot, false otherwise.
   */
  private static boolean isDependabot(Commit commit) {
    return commit.authorName().toLowerCase().contains(DEPENDABOT_PATTERN)
        || commit.committerName().toLowerCase().contains(DEPENDABOT_PATTERN);
  }
}