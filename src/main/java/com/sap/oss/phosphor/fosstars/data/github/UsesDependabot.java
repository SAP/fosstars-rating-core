package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

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
  protected Feature<Boolean> supportedFeature() {
    return USES_DEPENDABOT;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
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
    for (String config : DEPENDABOT_CONFIGS) {
      Optional<String> content = repository.file(config);
      if (content.isPresent() && content.get().length() >= ACCEPTABLE_CONFIG_SIZE) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if a project uses Dependabot.
   *
   * @return A value for the {@link OssFeatures#USES_DEPENDABOT} feature.
   */
  private Value<Boolean> usesDependabot(GitHubProject project) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
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