package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHRepository;

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
   * A list of checks to figure out if a project uses GitHub for development.
   */
  private static final List<Predicate<GHRepository>> CHECKS = Arrays.asList(

      // check if the repository doesn't have an explicit link to the original repository
      repository -> StringUtils.isEmpty(repository.getMirrorUrl()),

      // check if the description mentions "mirror"
      repository -> !repository.getDescription().toLowerCase().contains("mirror"),

      // if GitHub issues are enabled, then it's likely that the project uses GitHub
      GHRepository::hasIssues,

      // if GitHub pages are enabled, then it's likely that the project uses GitHub
      GHRepository::hasPages,

      // if GitHub Wiki is enabled, then it's likely that the project uses GitHub
      GHRepository::hasWiki,

      // if one of the three options for pull requests is enabled, then it looks like
      // that the project uses pull requests
      repository ->  repository.isAllowMergeCommit()
          || repository.isAllowRebaseMerge() || repository.isAllowSquashMerge(),

      // check if repository is not archived
      repository ->  !repository.isArchived(),

      // check if the project doesn't have an explicit link to an SVN repository
      repository ->  StringUtils.isEmpty(repository.getSvnUrl())
  );
  
  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesGithubForDevelopment(GitHubDataFetcher fetcher) {
    super(fetcher);
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
   * @return A value of {@link OssFeatures#USES_GITHUB_FOR_DEVELOPMENT}.
   */
  private Value<Boolean> usesGithubForDevelopment(GitHubProject project) {
    try {
      GHRepository repository = fetcher.repositoryFor(project);

      return USES_GITHUB_FOR_DEVELOPMENT.value(
          usesGitHubForDevelopment(repository, CONFIDENCE_THRESHOLD));
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
   * @return True if it looks like that the project uses GitHub, false otherwise.
   *
   */
  static boolean usesGitHubForDevelopment(GHRepository repository, double threshold) {
    int points = CHECKS.stream()
        .map(check -> check.test(repository) ? 1 : 0)
        .reduce(0, Integer::sum);

    return points / CHECKS.size() >= threshold;
  }
}
