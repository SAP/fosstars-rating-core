package com.sap.oss.phosphor.fosstars.tool.github;

import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.NoValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A base class for rating calculators.
 */
public abstract class AbstractRatingCalculator implements RatingCalculator {

  /**
   * A logger.
   */
  final Logger logger = LogManager.getLogger(getClass());

  /**
   * Open source security rating.
   */
  final Rating rating;

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
   * An interface for interacting with a user.
   */
  UserCallback callback = NoUserCallback.INSTANCE;

  /**
   * Initializes a new calculator.
   *
   * @param rating A rating.
   * @param fetcher An interface to GitHub.
   * @param nvd An interface to NVD.
   */
  AbstractRatingCalculator(Rating rating, GitHubDataFetcher fetcher, NVD nvd) {
    Objects.requireNonNull(rating, "Oh no! Rating can't be null!");
    Objects.requireNonNull(fetcher, "Oh no! An interface to GitHub can't be null!");
    Objects.requireNonNull(nvd, "Oh no! An interface to NVD can't be null!");

    this.rating = rating;
    this.fetcher = fetcher;
    this.nvd = nvd;
  }

  @Override
  public AbstractRatingCalculator set(UserCallback callback) {
    Objects.requireNonNull(callback, "Oh no! Callback can't be null!");
    this.callback = callback;
    return this;
  }

  @Override
  public AbstractRatingCalculator set(ValueCache<GitHubProject> cache) {
    Objects.requireNonNull(cache, "Oh no! Cache can't be null!");
    this.cache = cache;
    return this;
  }

  @Override
  public AbstractRatingCalculator calculateFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "Oh no! Project can't be null!");

    logger.info("Let's gather info and calculate a security rating for:");
    logger.info("  {}", project.scm());

    try {
      fetcher.repositoryFor(project);
    } catch (IOException e) {
      logger.error("Looks like something is wrong with the project!", e);
      logger.warn("Let's skip the project ...");
      return this;
    }

    ValueSet values = ValueHashSet.unknown(rating.allFeatures());
    for (DataProvider<GitHubProject> provider : dataProviders()) {

      // skip data providers that talk to users but the callback doesn't allow that
      if (provider.interactive() && !callback.canTalk()) {
        continue;
      }

      try {
        provider.set(callback).set(cache).update(project, values);
      } catch (Exception e) {
        logger.warn("Holy Moly, {} data provider failed!",
            provider.getClass().getSimpleName());
        logger.warn("The last thing that it said was", e);
        logger.warn("But we don't give up!");
      }
    }

    logger.info("Here is what we know about the project:");
    values.toSet().stream()
        .sorted(Comparator.comparing(value -> value.feature().name()))
        .forEach(value -> logger.info("   {}: {}", value.feature(), value));

    project.set(rating.calculate(values));

    return this;
  }

  /**
   * Initializes a list of data providers that are going to be used by the calculator.
   *
   * @return The list of data providers.
   * @throws IOException If something went wrong during the initialization.
   */
  abstract List<DataProvider<GitHubProject>> dataProviders() throws IOException;
}
