package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.data.NoValueCache;
import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.data.ValueCache;
import com.sap.sgs.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.sgs.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.sgs.phosphor.fosstars.nvd.NVD;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  final GitHubDataFetcher fetcher;

  /**
   * An interface to NVD.
   */
  final NVD nvd;

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
   * @param fetcher An interface to GitHub.
   * @param nvd An interface to NVD.
   */
  AbstractRatingCalculator(GitHubDataFetcher fetcher, NVD nvd) {
    Objects.requireNonNull(fetcher, "Oh no! An interface to GitHub can't be null!");
    Objects.requireNonNull(nvd, "Oh no! An interface to NVD can't be null!");
    this.fetcher = fetcher;
    this.nvd = nvd;
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
