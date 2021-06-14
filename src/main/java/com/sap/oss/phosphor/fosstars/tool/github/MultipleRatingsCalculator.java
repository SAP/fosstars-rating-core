package com.sap.oss.phosphor.fosstars.tool.github;

import static java.util.Arrays.asList;

import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.ValueCache;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.tool.SubjectCache;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class calculates ratings for multiple subjects.
 */
class MultipleRatingsCalculator implements RatingCalculator {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(MultipleRatingsCalculator.class);

  /**
   * A calculator that calculates a rating for a single subject.
   */
  private final RatingCalculator calculator;

  /**
   * A cache of processed subjects.
   */
  private SubjectCache subjectCache = SubjectCache.empty();

  /**
   * A filename where the cache of subjects should be stored.
   */
  private String subjectCacheFile;

  /**
   * A list of subjects for which a rating couldn't be calculated.
   */
  private final List<Subject> failedSubjects = new ArrayList<>();

  /**
   * Initializes a new calculator that calculates ratings for multiple subjects.
   *
   * @param calculator A calculator that calculates a rating for a single subject.
   */
  MultipleRatingsCalculator(RatingCalculator calculator) {
    Objects.requireNonNull(calculator, "Oh no! Calculator is null!");
    this.calculator = calculator;
  }

  @Override
  public MultipleRatingsCalculator set(UserCallback callback) {
    Objects.requireNonNull(callback, "Oh no! Callback is null!");
    calculator.set(callback);
    return this;
  }

  @Override
  public MultipleRatingsCalculator set(ValueCache<Subject> cache) {
    Objects.requireNonNull(cache, "Oh no! Cache is null!");
    calculator.set(cache);
    return this;
  }

  /**
   * Set a cache of processed subjects.
   *
   * @param subjectCache The cache.
   * @return The same {@link MultipleRatingsCalculator}.
   */
  public MultipleRatingsCalculator set(SubjectCache subjectCache) {
    this.subjectCache = Objects.requireNonNull(subjectCache, "Oh no! Cache can't be null!");
    return this;
  }

  /**
   * Sets a file where the cache of processed subjects should be stored.
   *
   * @param filename The file.
   * @return The same {@link MultipleRatingsCalculator}.
   */
  MultipleRatingsCalculator storeCacheTo(String filename) {
    Objects.requireNonNull(filename, "Hey! Filename can't be null!");
    subjectCacheFile = filename;
    return this;
  }

  @Override
  public MultipleRatingsCalculator calculateFor(Subject subject) throws IOException {
    Optional<RatingValue> cachedRatingValue = subjectCache.cachedRatingValueFor(subject);
    if (cachedRatingValue.isPresent()) {
      subject.set(cachedRatingValue.get());
      LOGGER.info("Found a cached rating for {}", subject);
      return this;
    }

    calculator.calculateFor(subject);
    subjectCache.add(subject);

    return this;
  }

  /**
   * Calculates ratings for multiple subjects.
   * First, the method checks if a rating value for a subject is already available in cache.
   *
   * @param subjects The subjects.
   * @return The same calculator.
   */
  MultipleRatingsCalculator calculateFor(Subject... subjects) {
    Objects.requireNonNull(subjects, "Oh no! Subjects is null!");
    return calculateFor(asList(subjects));
  }

  /**
   * Calculates ratings for multiple subjects.
   * First, the method checks if a rating value for a subject is already available in cache.
   *
   * @param subjects The subjects.
   * @return The same calculator.
   */
  MultipleRatingsCalculator calculateFor(List<? extends Subject> subjects) {
    Objects.requireNonNull(subjects, "Oh no! Subjects is null!");

    failedSubjects.clear();

    for (Subject subject : subjects) {
      try {
        calculateFor(subject);

        if (subjectCacheFile != null) {
          LOGGER.info("Storing the cache to {}", subjectCacheFile);
          subjectCache.store(subjectCacheFile);
        }
      } catch (Exception e) {
        LOGGER.warn("Oh no! Could not calculate a rating for {}", subject.purl());
        LOGGER.warn(e);
        failedSubjects.add(subject);
      }
    }

    return this;
  }

  /**
   * Returns a list of subjects for which ratings couldn't be calculated.
   *
   * @return The list of subjects.
   */
  List<Subject> failedSubjects() {
    return new ArrayList<>(failedSubjects);
  }

}
