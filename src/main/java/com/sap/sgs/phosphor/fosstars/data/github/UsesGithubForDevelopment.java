package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_GITHUB_FOR_DEVELOPMENT;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;

/**
 * This data provider tries to figure out if a project uses GitHub for development.
 */
public class UsesGithubForDevelopment extends CachedSingleFeatureGitHubDataProvider {

  /**
   * This threshold shows how many checks have to be passed to say that a project uses GitHub
   * for development.
   */
  private static final double CONFIDENCE_THRESHOLD = 0.6;

  /**
   * A list of checks to figure out if a project uses GitHub for development.
   */
  private static final List<Predicate<GHRepository>> CHECKS = Arrays.asList(

      // check if the description mentions "mirror"
      repository -> {
        String description = repository.getDescription();
        return StringUtils.isEmpty(description) || !description.toLowerCase().contains("mirror");
      },

      // if GitHub issues are enabled, then it's likely that the project uses GitHub
      GHRepository::hasIssues,

      // if GitHub Wiki or Pages are enabled, then it's likely that the project uses GitHub
      repository -> repository.hasWiki() || repository.hasPages(),

      // if one of the three options for pull requests is enabled, then it looks like
      // that the project uses pull requests
      repository ->  repository.isAllowMergeCommit()
          || repository.isAllowRebaseMerge() || repository.isAllowSquashMerge(),

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
  protected Feature supportedFeature() {
    return USES_GITHUB_FOR_DEVELOPMENT;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) {
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

    // check if the repository doesn't have an explicit link to the original repository
    // or an explicit link to an SVN repository and the link does not contain github.com
    if (notGitHubUrl(repository.getMirrorUrl()) || notGitHubUrl(repository.getSvnUrl())) {
      return false;
    }

    // The .github directory contains various settings for the features provider by GitHub
    // if a project's repository has this directory, that means that the project takes advantage
    // of some GitHub features
    // therefore, it's likely that the project uses GitHub for development
    if (hasGitHubDirectory(repository)) {
      return true;
    }

    int points = CHECKS.stream()
        .map(check -> check.test(repository) ? 1 : 0)
        .reduce(0, Integer::sum);

    return (double) points / CHECKS.size() >= threshold;
  }

  /**
   * Check if a string is not a URL to GitHub.
   * 
   * @param input The string to be checked.
   * @return True if the string is not a URL to GitHub, false otherwise.
   * @throws IOException If something goes wrong.
   */
  private static boolean notGitHubUrl(String input) throws IOException {
    try {
      return !StringUtils.isEmpty(input) 
          && !new URI(input).getHost().equalsIgnoreCase("github.com");
    } catch (URISyntaxException e) {
      throw new IOException(String.format("Error while parsing url %s", input), e);
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