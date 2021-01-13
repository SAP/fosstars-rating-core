package com.sap.oss.phosphor.fosstars.tool.github;

import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.List;

/**
 * An interface of a rating calculator for projects on GitHub.
 */
public interface RatingCalculator {

  /**
   * Set a token for accessing the GitHub APIs.
   *
   * @param token The token.
   * @return The same calculator.
   */
  RatingCalculator token(String token);

  /**
   * Sets a {@link UserCallback} to talk to a user.
   *
   * @param callback The callback.
   * @return The same calculator.
   */
  RatingCalculator set(UserCallback callback);

  /**
   * Sets a cache for feature values.
   *
   * @param cache The cache.
   * @return The same calculator.
   */
  RatingCalculator set(ValueCache<GitHubProject> cache);

  /**
   * Calculates a rating for a single project.
   *
   * @param project The project.
   * @return The same calculator.
   * @throws IOException If something went wrong.
   */
  RatingCalculator calculateFor(GitHubProject project) throws IOException;

  /**
   * Calculates ratings for multiple projects.
   *
   * @param projects The projects.
   * @return The same calculator.
   * @throws IOException If something went wrong.
   */
  RatingCalculator calculateFor(List<GitHubProject> projects) throws IOException;
}
