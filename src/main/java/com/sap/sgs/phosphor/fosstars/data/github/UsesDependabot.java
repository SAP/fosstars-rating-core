package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_DEPENDABOT;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

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
public class UsesDependabot extends AbstractGitHubDataProvider {

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
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   */
  public UsesDependabot(String where, String name, GitHub github) {
    super(where, name, github);
  }

  @Override
  public UsesDependabot update(ValueSet values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    logger.info("Checking if the project uses Dependabot ...");
    values.update(usesDependabot());
    return this;
  }

  /**
   * Checks if a project has commits from Dependabot in its commit history.
   *
   * @return True if a commit from Dependabot was found, false otherwise.
   */
  private boolean hasDependabotCommits() {
    try {
      GHRepository repository = github.getRepository(path);

      Date date = Date.from(Instant.now().minus(LATEST_MONTHS));
      for (GHCommit commit : repository.queryCommits().since(date).list()) {
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
   * Checks if a project has a configuration file for Dependabot.
   *
   * @return True if a config was found, false otherwise.
   */
  private boolean hasDependabotConfig() {
    try {
      GHRepository repository = github.getRepository(path);
      GHContent content = repository.getFileContent(DEPENDABOT_CONFIG);

      if (content == null) {
        return false;
      }

      if (!content.isFile()) {
        return false;
      }

      return content.getSize() >= ACCEPTABLE_CONFIG_SIZE;
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
  private Value<Boolean> usesDependabot() {
    Optional<Value> something = cache().get(url, USES_DEPENDABOT);
    if (something.isPresent()) {
      something.get();
    }

    Value<Boolean> value = USES_DEPENDABOT.value(
        hasDependabotConfig() || hasDependabotCommits());
    cache().put(url, value);

    return value;
  }

  private static boolean isDependabot(GHCommit commit) throws IOException {
    GHCommit.ShortInfo info = commit.getCommitShortInfo();
    return commit.getAuthor().getLogin().toLowerCase().contains(DEPENDABOT_PATTERN)
        || commit.getCommitter().getLogin().toLowerCase().contains(DEPENDABOT_PATTERN)
        || info.getAuthor().getName().toLowerCase().contains(DEPENDABOT_PATTERN)
        || info.getCommitter().getName().toLowerCase().contains(DEPENDABOT_PATTERN);
  }
}
