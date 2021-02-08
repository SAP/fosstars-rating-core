package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;

/**
 * This data provider tries to figure out if a project uses GitHub for development.
 */
public class UsesGithubForDevelopment extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * This threshold shows how many checks have to be passed to say that a project uses GitHub
   * for development.
   */
  private static final double CONFIDENCE_THRESHOLD = 0.5;

  /**
   * A list of checks to figure out if a project uses GitHub for development.
   */
  private static final List<Predicate<GHRepository>> CHECKS = Arrays.asList(

      // check if the description mentions "mirror"
      repository -> {
        String description = repository.getDescription();
        return StringUtils.isEmpty(description) || !description.toLowerCase().contains("mirror");
      },

      // check if the repository doesn't have a link to a non-GitHub repository
      repository -> Stream.of(repository.getMirrorUrl(), repository.getSvnUrl())
          .filter(StringUtils::isNotEmpty)
          .noneMatch(UsesGithubForDevelopment::notGitHubUrl),

      // if GitHub issues are enabled, then it's likely that the project uses GitHub
      GHRepository::hasIssues,

      // if GitHub Wiki or Pages are enabled, then it's likely that the project uses GitHub
      repository -> repository.hasWiki() || repository.hasPages(),

      // check if repository is not archived
      repository ->  !repository.isArchived()
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
  protected Feature<Boolean> supportedFeature() {
    return USES_GITHUB_FOR_DEVELOPMENT;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) {
    logger.info("Figuring out if the project uses GitHub for development ...");
    return usesGithubForDevelopment(project);
  }

  /**
   * Checks if a project uses GitHub for development.
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
   * The method checks if it looks like that a project uses GitHub for development.
   * The method runs a number of checks for the project. If most of the checks pass,
   * then the method concludes that the projects uses GitHub for development.
   * 
   * @param repository The project's repository.
   * @param threshold value to be checked against.
   * @return True if it looks like that the project uses GitHub, false otherwise.
   * @throws IOException If something goes wrong.
   */
  static boolean usesGitHubForDevelopment(GHRepository repository, double threshold) 
      throws IOException {

    // The .github directory contains various settings for the features provider by GitHub
    // if a project's repository has this directory, that means that the project takes advantage
    // of some GitHub features
    // therefore, it's likely that the project uses GitHub for development
    if (hasGitHubDirectory(repository)) {
      return true;
    }

    // if pull requests are enabled for the repository, then it looks like GitHub is used for dev
    if (enabledPullRequests(repository)) {
      return true;
    }

    int points = CHECKS.stream()
        .map(check -> check.test(repository) ? 1 : 0)
        .reduce(0, Integer::sum);

    return Double.compare((double) points / CHECKS.size(), threshold) >= 0;
  }

  /**
   * Check if pull requests are enabled in a repository.
   *
   * @param repository The repository to be checked.
   * @return True if pull requests are enabled, false otherwise.
   */
  private static boolean enabledPullRequests(GHRepository repository) {
    // if one of the three options for pull requests is enabled, then it looks like
    // that the project uses pull requests
    return repository.isAllowMergeCommit()
        || repository.isAllowRebaseMerge()
        || repository.isAllowSquashMerge();
  }

  /**
   * Check if a string is not a URL to GitHub.
   * 
   * @param input The string to be checked.
   * @return True if the string is not a URL to GitHub, false otherwise.
   */
  static boolean notGitHubUrl(String input) {
    try {
      return !"github.com".equalsIgnoreCase(new URI(input).getHost());
    } catch (URISyntaxException e) {
      return true;
    }
  }

  /**
   * Check if a repository has a not-empty .github directory.
   *
   * @param repository The repository to be checked.
   * @return True if the repository has the directory, false otherwise.
   */
  private static boolean hasGitHubDirectory(GHRepository repository) {
    try {
      List<GHContent> contents = repository.getDirectoryContent(".github");
      return contents != null && !contents.isEmpty();
    } catch (IOException e) {
      return false;
    }
  }
}