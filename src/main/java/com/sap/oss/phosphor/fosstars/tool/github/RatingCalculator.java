package com.sap.oss.phosphor.fosstars.tool.github;

import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;

/**
 * A calculator that calculates a rating for a project.
 */
public interface RatingCalculator {

  /**
   * Sets an interface for interacting with a user.
   *
   * @param callback The interface for interacting with a user.
   * @return The same calculator.
   */
  RatingCalculator set(UserCallback callback);

  /**
   * Set a cache for the calculator.
   *
   * @param cache The cache.
   * @return The same calculator.
   */
  RatingCalculator set(ValueCache<GitHubProject> cache);

  /**
   * Calculate a rating and assign it to a project.
   *
   * @param project The project.
   * @return The same calculator.
   * @throws IOException If something went wrong.
   */
  default RatingCalculator calculateFor(GitHubProject project) throws IOException {
    return calculateFor(project, ValueHashSet.empty());
  }

  /**
   * Calculate a rating and assign it to a project.
   *
   * @param project The project.
   * @param knownValues values which are known and should not be collected by a data provider
   * @return The same calculator.
   * @throws IOException If something went wrong.
   */
  RatingCalculator calculateFor(GitHubProject project, ValueSet knownValues) throws IOException;
}
