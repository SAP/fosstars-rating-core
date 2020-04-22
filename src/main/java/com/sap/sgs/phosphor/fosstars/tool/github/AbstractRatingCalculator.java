package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.NoValueCache;
import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.data.ValueCache;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GitHub;

/**
 * A base class for rating calculators.
 */
abstract class AbstractRatingCalculator implements RatingCalculator {

  /**
   * A logger.
   */
  protected final Logger logger = LogManager.getLogger(getClass());

  /**
   * An interface to GitHub.
   */
  final GitHub github;

  /**
   * A cache of feature values for GitHub projects.
   */
  ValueCache<GitHubProject> cache = NoValueCache.create();

  /**
   * A token for accessing the GitHub APIs.
   */
  String token;

  /**
   * An interface for interacting with a user.
   */
  UserCallback callback = NoUserCallback.INSTANCE;

  /**
   * Initializes a new calculator.
   *
   * @param github An interface to GitHub.
   */
  AbstractRatingCalculator(GitHub github) {
    this.github = Objects.requireNonNull(github, "Oh no! An interface to GitHub can't be null!");
  }

  /**
   * Sets a token for accessing the GitHub APIs.
   *
   * @param token The token.
   * @return The same calculator.
   */
  @Override
  public RatingCalculator token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Sets an interface for interacting with a user.
   *
   * @param callback The interface for interacting with a user.
   * @return The same calculator.
   */
  @Override
  public RatingCalculator set(UserCallback callback) {
    this.callback = callback;
    return this;
  }

  /**
   * Set a cache for the calculator.
   *
   * @param cache The cache.
   * @return The same calculator.
   */
  @Override
  public RatingCalculator set(ValueCache<GitHubProject> cache) {
    this.cache = Objects.requireNonNull(cache, "Oh no! Cache can't be null!");
    return this;
  }

}
