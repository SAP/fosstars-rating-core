package com.sap.sgs.phosphor.fosstars.tool.github;

import java.io.IOException;
import java.util.List;
import org.kohsuke.github.GitHub;

/**
 * The class calculates security ratings for multiple open-source projects.
 */
class MultipleSecurityRatingsCalculator extends AbstractRatingCalculator {

  /**
   * Initializes a new calculator.
   *
   * @param github An interface to GitHub.
   */
  MultipleSecurityRatingsCalculator(GitHub github) {
    super(github);
  }

  @Override
  MultipleSecurityRatingsCalculator calculateFor(GitHubProject project) throws IOException {
    singleSecurityRatingCalculator().calculateFor(project);
    return this;
  }

  @Override
  MultipleSecurityRatingsCalculator calculateFor(List<GitHubProject> projects) throws IOException {
    for (GitHubProject project : projects) {
      calculateFor(project);
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
