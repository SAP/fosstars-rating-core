package com.sap.oss.phosphor.fosstars.tool.github;

import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.NoValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class calculates a rating for a project.
 */
public class SingleSecurityRatingCalculator implements RatingCalculator {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(SingleSecurityRatingCalculator.class);

  /**
   * Open source security rating.
   */
  private final Rating rating;

  /**
   * A list of data providers.
   */
  private final List<DataProvider<GitHubProject>> providers;

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
   * @param providers A list of data providers.
   */
  SingleSecurityRatingCalculator(Rating rating, List<DataProvider<GitHubProject>> providers) {
    Objects.requireNonNull(rating, "Oh no! Rating can't be null!");
    Objects.requireNonNull(providers, "Oh no! A list of data providers can't be null!");

    this.rating = rating;
    this.providers = providers;
  }

  @Override
  public SingleSecurityRatingCalculator set(UserCallback callback) {
    Objects.requireNonNull(callback, "Oh no! Callback can't be null!");
    this.callback = callback;
    return this;
  }

  @Override
  public SingleSecurityRatingCalculator set(ValueCache<GitHubProject> cache) {
    Objects.requireNonNull(cache, "Oh no! Cache can't be null!");
    this.cache = cache;
    return this;
  }

  @Override
  public SingleSecurityRatingCalculator calculateFor(GitHubProject project) {
    Objects.requireNonNull(project, "Oh no! Project can't be null!");

    LOGGER.info("Let's gather info and calculate a security rating for:");
    LOGGER.info("  {}", project.scm());

    ValueSet values = ValueHashSet.unknown(rating.allFeatures());
    for (DataProvider<GitHubProject> provider : providers) {

      // skip data providers that talk to users but the callback doesn't allow that
      if (provider.interactive() && !callback.canTalk()) {
        continue;
      }

      try {
        provider.set(callback).set(cache).update(project, values);
      } catch (Exception e) {
        LOGGER.warn("Holy Moly, {} data provider failed!",
            provider.getClass().getSimpleName());
        LOGGER.warn("The last thing that it said was", e);
        LOGGER.warn("But we don't give up!");
      }
    }

    LOGGER.info("Here is what we know about the project:");
    values.toSet().stream()
        .sorted(Comparator.comparing(value -> value.feature().name()))
        .forEach(value -> LOGGER.info("   {}: {}", value.feature(), value));

    project.set(rating.calculate(values));

    return this;
  }
}
