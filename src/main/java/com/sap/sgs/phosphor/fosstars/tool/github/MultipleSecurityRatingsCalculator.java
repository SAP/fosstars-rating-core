package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.io.IOException;
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
  MultipleSecurityRatingsCalculator calculateFor(GitHubProject project) throws IOException {
    singleSecurityRatingCalculator().calculateFor(project);
    return this;
  }

  /**
   * Calculates ratings for multiple projects.
   * First, the method checks if a rating value for a project is already available in cache.
   *
   * @param projects The projects.
   * @return The same calculator.
   * @throws IOException If something went wrong.
   */
  @Override
  MultipleSecurityRatingsCalculator calculateFor(List<GitHubProject> projects) throws IOException {
    for (GitHubProject project : projects) {
      Optional<RatingValue> cachedRatingValue = projectCache.cachedRatingValueFor(project);
      if (cachedRatingValue.isPresent()) {
        project.set(cachedRatingValue.get());
        System.out.printf("Found a cached rating value!%n");
        continue;
      }

      calculateFor(project);
      projectCache.add(project);
    }
    return this;
  }

  /**
   * Creates a {@link SingleSecurityRatingCalculator} for calculating a rating for a single project.
   *
   * @return An instance of {@link SingleSecurityRatingCalculator}.
   */
  AbstractRatingCalculator singleSecurityRatingCalculator() {
    return new SingleSecurityRatingCalculator(github).token(token).set(callback);
  }

}
