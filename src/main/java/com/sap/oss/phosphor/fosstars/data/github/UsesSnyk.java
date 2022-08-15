package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_SNYK;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SNYK;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHUser;

/**
 * This data provider checks if an open-source project on GitHub uses Snyk, and fills out the {@link
 * OssFeatures#USES_SNYK} feature.
 *
 * <p>First, the provider checks if a repository contains a policy file for Snyk. If the policy file
 * exists, then the provider reports that the project uses Snyk. Next, the provider searches for
 * commits from Snyk in the commit history. If the commits are found, then the provider also reports
 * that the project uses Snyk.
 */
public class UsesSnyk extends GitHubCachingDataProvider {

  /**
   * A file name containing Snyk policies in a repository.
   *
   * @see <a href="https://docs.snyk.io/snyk-cli/test-for-vulnerabilities/the-.snyk-file/">The .snyk
   *     file</a>
   */
  private static String SNYK_POLICY_FILE_NAME = ".snyk";

  /**
   * A minimal number of characters in a config for Snyk.
   */
  private static final int ACCEPTABLE_CONFIG_SIZE = 10;

  /**
   * A location of a Snyk configuration file in a repository.
   *
   * @see <a href="https://github.com/snyk/actions/tree/master/setup">To setup Snyk actions</a>
   *
   */
  private static final String [] SNYK_CONFIGS = {
      ".github/workflows/snyk.yaml",
      ".github/workflows/snyk.yml"
  };

  /** 
    * Predicate to confirm if there is a file in open-source project with the .snyk extension.
    */
  private static final Predicate<Path> SNYK_FILE_PREDICATE =
      path -> path.getFileName().toString().endsWith(SNYK_POLICY_FILE_NAME);

  /**
   * A pattern to detect commits by Snyk.
   *
   * @see <a
   *     href="https://docs.snyk.io/integrations/git-repository-scm-integrations/github-integration#commit-signing/">Snyk
   *     commit signing</a>
   */
  private static final String SNYK_PATTERN = "snyk";

  /** Period of time to be checked. */
  private static final Duration ONE_YEAR = Duration.ofDays(365);

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesSnyk(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(USES_SNYK, HAS_OPEN_PULL_REQUEST_FROM_SNYK);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Checking how the project uses Snyk ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    return ValueHashSet.from(
        USES_SNYK.value(
            hasSnykPolicy(repository)
            || hasSnykConfig(repository)
            || hasSnykCommits(repository)),
        HAS_OPEN_PULL_REQUEST_FROM_SNYK.value(hasOpenPullRequestFromSnyk(project)));
  }

  /**
   * Checks if a repository has a configuration file for Snyk.
   *
   * @param repository The repository
   * @return True if a config was found, false otherwise.
   */
  private boolean hasSnykConfig(LocalRepository repository) throws IOException {
    for (String config : SNYK_CONFIGS) {
      Optional<String> content = repository.file(config);
      if (content.isPresent() && content.get().length() >= ACCEPTABLE_CONFIG_SIZE) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks if a repository has a policy file for Snyk.
   *
   * @param repository The repository
   * @return True if a policy file was found, false otherwise.
   */
  private boolean hasSnykPolicy(LocalRepository repository) throws IOException {
    List<Path> snykPolicyFilePaths = repository.files(SNYK_FILE_PREDICATE);
    return !snykPolicyFilePaths.isEmpty();
  }

  /**
   * Checks whether a project has open pull requests from Snyk.
   *
   * @param project The project.
   * @return True if the project has open pull requests form Snyk.
   * @throws IOException If something went wrong.
   */
  private boolean hasOpenPullRequestFromSnyk(GitHubProject project) throws IOException {
    return fetcher.repositoryFor(project).getPullRequests(GHIssueState.OPEN).stream()
        .anyMatch(this::createdBySnyk);
  }

  /**
   * Checks if a pull request was created by Snyk.
   *
   * @param pullRequest The pull request.
   * @return True if the user looks like Snyk, false otherwise.
   */
  private boolean createdBySnyk(GHPullRequest pullRequest) {
    try {
      GHUser user = pullRequest.getUser();
      return isSnyk(user.getName()) || isSnyk(user.getLogin());
    } catch (IOException e) {
      logger.warn("Oops! Could not fetch name or login!", e);
      return false;
    }
  }

  /**
   * Checks if a repository contains commits from Snyk in the commit history.
   *
   * @param repository The repository.
   * @return True if at least one commit from Snyk was found, false otherwise.
   */
  private boolean hasSnykCommits(LocalRepository repository) {
    Date date = Date.from(Instant.now().minus(ONE_YEAR));

    try {
      for (Commit commit : repository.commitsAfter(date)) {
        if (isSnyk(commit)) {
          return true;
        }
      }
    } catch (IOException e) {
      logger.warn("Something went wrong!", e);
    }

    return false;
  }

  /**
   * Checks if a commit was done by Snyk.
   *
   * @param commit The commit to be checked.
   * @return True if the commit was done by Snyk, false otherwise.
   */
  private static boolean isSnyk(Commit commit) {
    if (isSnyk(commit.authorName()) || isSnyk(commit.committerName())) {
      return true;
    }

    for (String line : commit.message()) {
      if ((line.startsWith("Signed-off-by:") || line.startsWith("Co-authored-by:"))
          && line.contains(SNYK_PATTERN)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Checks whether a name looks like Snyk.
   *
   * @param name The name.
   * @return True if the name looks like Snyk, false otherwise.
   */
  private static boolean isSnyk(String name) {
    return name != null && name.toLowerCase().contains(SNYK_PATTERN);
  }
}
