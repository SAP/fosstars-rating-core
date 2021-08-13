package com.sap.oss.phosphor.fosstars.tool;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.sap.oss.phosphor.fosstars.data.DataProvider;
import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.data.NoValueCache;
import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.tool.format.PrettyPrinter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class calculates a rating for a project.
 */
public class SingleRatingCalculator implements RatingCalculator {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(SingleRatingCalculator.class);

  /**
   * A rating.
   */
  private final Rating rating;

  /**
   * A list of data providers.
   */
  private final List<DataProvider> providers;

  /**
   * A cache of feature values for GitHub projects.
   */
  private ValueCache<Subject> cache = NoValueCache.create();

  /**
   * An interface for interacting with a user.
   */
  private UserCallback callback = NoUserCallback.INSTANCE;

  /**
   * An adaptor for subjects.
   * The default adaptor returns the same subject if a data provider supports it.
   */
  private SubjectAdaptor subjectAdaptor
      = (subject, provider) -> Optional.of(subject).filter(provider::supports);

  /**
   * An action that should be run after calculating a rating.
   */
  private Consumer<Subject> doAfterAction;

  /**
   * Initializes a new calculator.
   *
   * @param rating A rating.
   * @param providers A list of data providers.
   */
  public SingleRatingCalculator(Rating rating, List<DataProvider> providers) {
    requireNonNull(rating, "Oh no! Rating can't be null!");
    requireNonNull(providers, "Oh no! A list of data providers can't be null!");

    this.rating = rating;
    this.providers = providers;
  }

  @Override
  public SingleRatingCalculator set(UserCallback callback) {
    requireNonNull(callback, "Oh no! Callback can't be null!");
    this.callback = callback;
    return this;
  }

  @Override
  public SingleRatingCalculator set(ValueCache<Subject> cache) {
    requireNonNull(cache, "Oh no! Cache can't be null!");
    this.cache = cache;
    return this;
  }

  @Override
  public SingleRatingCalculator set(SubjectAdaptor adaptor) {
    requireNonNull(adaptor, "Oh no! Subject adaptor is null!");
    this.subjectAdaptor = adaptor;
    return this;
  }

  /**
   * Set an action that should be run after calculating a rating.
   *
   * @param doAfterAction The action.
   * @return The same calculator.
   */
  public SingleRatingCalculator doAfter(Consumer<Subject> doAfterAction) {
    requireNonNull(doAfterAction, "Oh no! Action is null!");
    this.doAfterAction = doAfterAction;
    return this;
  }

  @Override
  public SingleRatingCalculator calculateFor(Subject subject) {
    requireNonNull(subject, "Oh no! Subjects can't be null!");

    LOGGER.info("Let's gather info and calculate a rating for:");
    LOGGER.info("  {}", subject);

    ValueSet values = ValueHashSet.unknown(rating.allFeatures());
    for (DataProvider provider : providers) {

      // skip data providers that talk to users but the callback doesn't allow that
      if (provider.interactive() && !callback.canTalk()) {
        continue;
      }

      Optional<Subject> adaptedSubject = subjectAdaptor.adapt(subject, provider);
      if (!adaptedSubject.isPresent()) {
        continue;
      }

      try {
        provider.set(callback).set(cache).update(adaptedSubject.get(), values);
      } catch (Exception e) {
        LOGGER.warn("Holy Moly, {} data provider failed!",
            provider.getClass().getSimpleName());
        LOGGER.warn("The last thing that it said was", e);
        LOGGER.warn("But we don't give up!");
      }
    }

    LOGGER.info("Here is what we know about the project:");
    PrettyPrinter printer = PrettyPrinter.withoutVerboseOutput();
    values.toSet().stream()
        .sorted(Comparator.comparing(value -> value.feature().name()))
        .forEach(value -> {
          String name = printer.nameOf(value.feature());
          LOGGER.info("   {}{} {}",
              name, name.endsWith("?") ? EMPTY : ":", printer.actualValueOf(value));
        });

    subject.set(rating.calculate(values));

    if (doAfterAction != null) {
      doAfterAction.accept(subject);
    }

    return this;
  }
}
