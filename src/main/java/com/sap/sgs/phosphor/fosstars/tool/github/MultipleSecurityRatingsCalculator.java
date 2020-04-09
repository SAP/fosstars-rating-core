package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.kohsuke.github.GitHub;

/**
 * The class calculates security ratings for multiple open-source projects.
 */
class MultipleSecurityRatingsCalculator extends AbstractRatingCalculator {

  /**
   * A cache of processed projects.
   */
  private GitHubProjectCache projectCache = GitHubProjectCache.empty();

  /**
   * A list of projects for which a rating couldn't be calculated.
   */
  private final List<GitHubProject> failedProjects = new ArrayList<>();

  /**
   * Initializes a new calculator.
   *
   * @param github An interface to GitHub.
   */
  MultipleSecurityRatingsCalculator(GitHub github) {
    super(github);
  }

  /**
   * Set a cache of processed projects.
   *
   * @param projectCache The cache.
   * @return The same {@link MultipleSecurityRatingsCalculator}.
   */
  MultipleSecurityRatingsCalculator set(GitHubProjectCache projectCache) {
    this.projectCache = Objects.requireNonNull(projectCache, "Oh no! Project cache can't be null!");
    return this;
  }

  @Override
  public MultipleSecurityRatingsCalculator calculateFor(GitHubProject project) throws IOException {
    Optional<RatingValue> cachedRatingValue = projectCache.cachedRatingValueFor(project);
    if (cachedRatingValue.isPresent()) {
      project.set(cachedRatingValue.get());
      logger.info("Found a cached rating for {}", project);
      return this;
    }

    singleSecurityRatingCalculator().calculateFor(project);
    projectCache.add(project);

    return this;
  }

  /**
   * Calculates ratings for multiple projects.
   * First, the method checks if a rating value for a project is already available in cache.
   *
   * @param projects The projects.
   * @return The same calculator.
   */
  @Override
  public MultipleSecurityRatingsCalculator calculateFor(List<GitHubProject> projects) {
    failedProjects.clear();
    for (GitHubProject project : projects) {
      try {
        calculateFor(project);
      } catch (Exception e) {
        logger.warn("Oh no! Could not calculate a rating for {}", project.url());
        logger.warn(e);
        failedProjects.add(project);
      }
    }
    return this;
  }

  /**
   * Returns a list of projects for which ratings couldn't be calculated.
   */
  List<GitHubProject> failedProjects() {
    return new ArrayList<>(failedProjects);
  }

  /**
   * Creates a {@link SingleSecurityRatingCalculator} for calculating a rating for a single project.
   *
   * @return An instance of {@link SingleSecurityRatingCalculator}.
   */
  RatingCalculator singleSecurityRatingCalculator() {
    return new SingleSecurityRatingCalculator(github)
        .token(token)
        .set(callback)
        .set(cache);
  }

}
