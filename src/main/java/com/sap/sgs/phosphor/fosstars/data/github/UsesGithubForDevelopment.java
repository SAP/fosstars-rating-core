package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider tries to figure out if a project uses GitHub for development.
 */
public class UsesGithubForDevelopment extends CachedSingleFeatureGitHubDataProvider {

  /**
   * This threshold shows how many checks have to be passed to say that a project uses GitHub
   * for development.
   */
  static final double CONFIDENCE_THRESHOLD = 0.7;

  /**
   * This threshold shows how many commits have to point to existing GitHub user names
   * to say that commits are mostly done by users on GitHub.
   */
  private static final double RESOLVED_USERS_THRESHOLD = 0.9;

  private static final List<Check> CHECKS = Arrays.asList(

      // check if the repository doesn't have an explicit link to the original repository
      (repository, commits) -> StringUtils.isEmpty(repository.getMirrorUrl()),

      // check if the description mentions "mirror"
      (repository, commits) -> !repository.getDescription().toLowerCase().contains("mirror"),

      // if GitHub issues are enabled, then it's likely that the project uses GitHub
      (repository, commits) -> repository.hasIssues(),

      // if GitHub pages are enabled, then it's likely that the project uses GitHub
      (repository, commits) -> repository.hasPages(),

      // if GitHub Wiki is enabled, then it's likely that the project uses GitHub
      (repository, commits) -> repository.hasWiki(),

      // if one of the three options for pull requests is enabled, then it looks like
      // that the project uses pull requests
      (repository, commits) ->  repository.isAllowMergeCommit()
          || repository.isAllowRebaseMerge() || repository.isAllowSquashMerge(),

      // check if repository is not archived
      (repository, commits) ->  !repository.isArchived(),

      // check if the project doesn't have an explicit link to an SVN repository
      (repository, commits) ->  StringUtils.isEmpty(repository.getSvnUrl()),

      // check if most of user names in the latest commits are valid GitHub users
      (repository, commits) ->  {
        if (commits.isEmpty()) {
          return false;
        }
        int resolved = commits.stream()
            .map(commit -> canResolveUsersFor(commit) ? 1 : 0)
            .reduce(0, Integer::sum);
        return resolved / commits.size() >= RESOLVED_USERS_THRESHOLD;
      }
  );
  
  /**
   * Initializes a data providerepository.
   *
   * @param github An interface to the GitHub API.
   */
  public UsesGithubForDevelopment(GitHub github) {
    super(github);
  }

  @Override
  protected Feature supportedFeature() {
    return USES_GITHUB_FOR_DEVELOPMENT;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses GitHub as the main development platform ...");
    return usesGithubForDevelopment(project);
  }

  /**
   * Checks if a project uses GitHub as a main development platform.
   *
   * @param project The project to be checked.
   * @return {@link BooleanValue} of the feature.
   */
  private Value<Boolean> usesGithubForDevelopment(GitHubProject project) {
    try {
      GHRepository repository = gitHubDataFetcher().repositoryFor(project, github);

      Date threeMonthsAgo = Date.from(Instant.now().minus(3, ChronoUnit.MONTHS));
      List<GHCommit> commits = gitHubDataFetcher().commitsAfter(threeMonthsAgo, project, github);

      return USES_GITHUB_FOR_DEVELOPMENT.value(
          usesGitHubForDevelopment(repository, commits, CONFIDENCE_THRESHOLD));
    } catch (IOException e) {
      logger.warn("Couldn't fetch data, something went wrong!", e);
      return USES_GITHUB_FOR_DEVELOPMENT.unknown();
    }
  }

  /**
   * The method checks if it looks like that a project uses GitHub as a main development platform.
   * The method runs a number of checks for the project. If most of the checks pass,
   * then the method concludes that the projects uses GitHub for development.
   * 
   * @param repository The project's repository.
   * @param commits The project's commit history.
   * @return True if it looks like that the project uses GitHub, false otherwise.
   *
   */
  static boolean usesGitHubForDevelopment(
      GHRepository repository, List<GHCommit> commits, double threshold) {

    int points = CHECKS.stream()
        .map(check -> check.run(repository, commits) ? 1 : 0)
        .reduce(0, Integer::sum);

    return points / CHECKS.size() >= threshold;
  }

  /**
   * Checks if user names from a commit point to existing GitHub users.
   *
   * @param commit The commit to be checked.
   * @return True if user names from the commit can be resolved to existing GitHub users,
   *         false otherwise.
   */
  private static boolean canResolveUsersFor(GHCommit commit) {
    if (commit == null) {
      return false;
    }

    try {
      commit.getCommitter();
      commit.getAuthor();
    } catch (IOException e) {
      return false;
    }

    return true;
  }

  /**
   * An interface for a check for a project.
   */
  interface Check {

    /**
     * Runs the check for a repository and commit history.
     * 
     * @param repository The repository,
     * @param commits The commit history.
     * @return Returns true if the check is passed, false otherwise.
     */
    boolean run(GHRepository repository, List<GHCommit> commits);
  }
}
