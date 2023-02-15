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
import java.util.Set;

/**
 * <p>This data provider checks if an open-source project on GitHub
 * uses Dependabot, and fills out the {@link OssFeatures#USES_DEPENDABOT} feature.</p>
 *
 * <p>First, the provider checks if a repository contains a configuration file for Dependabot.
 * If the config exists, then the provider reports that the projects uses Dependabot.
 * Next, the provider searches for commits from Dependabot in the commit history.
 * If the commits are found, then the provider also reports that the project uses Dependabot.</p>
 */
public class UsesDependabot extends AbstractDependencyScanDataProvider {

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
   * A pattern to detect commits by Dependabot.
   */
  private static final String DEPENDABOT_PATTERN = "dependabot";

  @Override
  protected String getDependencyCheckerPattern() {
    return DEPENDABOT_PATTERN;
  }

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesDependabot(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(USES_DEPENDABOT, HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Checking how the project uses Dependabot ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    return ValueHashSet.from(
        USES_DEPENDABOT.value(
            hasDependencyCheckerConfig(repository, DEPENDABOT_CONFIGS)
                || hasDependencyCheckerCommits(repository)),
        HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT.value(
            hasOpenPullRequestFromDependencyChecker(project)));
  }
}