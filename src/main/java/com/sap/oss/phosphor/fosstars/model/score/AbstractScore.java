package com.sap.oss.phosphor.fosstars.model.score;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.Visitor;
import com.sap.oss.phosphor.fosstars.model.Weight;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.UnknownValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A base class for scores.
 */
public abstract class AbstractScore implements Score {

  /**
   * No description.
   */
  static final String EMPTY_DESCRIPTION = "";

  /**
   * Score name.
   */
  private final String name;

  /**
   * Description.
   */
  @JsonIgnore
  private final String description;

  /**
   * A logger.
   */
  @JsonIgnore
  protected final Logger logger = LogManager.getLogger(getClass());

  /**
   * Initializes a new score.
   *
   * @param name A name of the score.
   */
  public AbstractScore(String name) {
    this(name, EMPTY_DESCRIPTION);
  }

  /**
   * Initializes a new score.
   *
   * @param name A name of the score.
   * @param description A description of the score (may be empty).
   */
  public AbstractScore(String name, String description) {
    Objects.requireNonNull(name, "Hey! Score name can't be null!");
    Objects.requireNonNull(description, "Hey! Score description can't be null!");

    if (name.isEmpty()) {
      throw new IllegalArgumentException("Hey! Score name can't be empty!");
    }

    this.name = name;
    this.description = description;
  }

  @Override
  @JsonGetter("name")
  public final String name() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public ScoreValue calculate(Set<Value<?>> values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    return calculate(values.toArray(new Value[0]));
  }

  @Override
  public ScoreValue calculate(ValueSet values) {
    Objects.requireNonNull(values, "Hey! Value set can't be null!");
    return calculate(values.toSet());
  }

  @Override
  public void accept(Visitor visitor) {
    for (Score subScore : subScores()) {
      subScore.accept(visitor);
    }
    for (Feature feature : features()) {
      feature.accept(visitor);
    }
    visitor.visit(this);
  }

  @Override
  public Value<Double> unknown() {
    return UnknownValue.of(this);
  }

  @Override
  public Value<Double> value(Double n) {
    return new ScoreValue(
        this, Score.check(n), Weight.MAX, Confidence.MAX, Collections.emptyList());
  }

  @Override
  public Value<Double> parse(String string) {
    return value(Double.parseDouble(string));
  }

  @Override
  public Set<Feature<?>> allFeatures() {
    return fillOutFeatures(this, new HashSet<>());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof AbstractScore == false) {
      return false;
    }
    AbstractScore that = (AbstractScore) o;
    return Objects.equals(name, that.name) && Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description);
  }

  /**
   * Looks for a sub-score of a specified type.
   *
   * @param clazz The class which represents the type.
   * @param <T> The type.
   * @return A sub-score if found.
   * @throws IllegalArgumentException If a sub-score was not found.
   */
  public <T extends Score> Score score(Class<T> clazz) {
    Objects.requireNonNull(clazz, "Class can't be null!");
    for (Score subScore : subScores()) {
      if (clazz.equals(subScore.getClass())) {
        return subScore;
      }
    }
    throw new IllegalArgumentException(String.format(
        "Sub-score %s not found", clazz.getCanonicalName()));
  }

  /**
   * Collect all features which are used by a specified score and its sub-scores.
   * The method browses the underlying sub-scores recursively and adds features
   * to a specified set.
   *
   * @param score The score.
   * @param allFeatures A set of features to be filled out.
   * @return The specified set of features.
   */
  private static Set<Feature<?>> fillOutFeatures(Score score, Set<Feature<?>> allFeatures) {
    allFeatures.addAll(score.features());
    for (Score subScore : score.subScores()) {
      fillOutFeatures(subScore, allFeatures);
    }
    return allFeatures;
  }

  /**
   * Initializes a score value for the score.
   * The method adjusts the specified score value so that it fits to the valid range [0, 10].
   *
   * @param value The score value.
   * @param usedValues The values which were used to produce the score value.
   * @return A new {@link ScoreValue}.
   */
  protected ScoreValue scoreValue(double value, Value<?>... usedValues) {
    return new ScoreValue(
        this,
        Score.adjust(value),
        Weight.MAX,
        Confidence.make(usedValues),
        Arrays.asList(usedValues));
  }

  /**
   * Initializes a score value for the score.
   * The method adjusts the specified score value so that it fits to the valid range [0, 10].
   *
   * @param value The score value.
   * @param usedValues The values which were used to produce the score value.
   * @return A new {@link ScoreValue}.
   */
  protected ScoreValue scoreValue(double value, List<Value<?>> usedValues) {
    return new ScoreValue(
        this,
        Score.adjust(value),
        Weight.MAX,
        Confidence.make(usedValues),
        usedValues);
  }

  /**
   * Looks for a value of a specified feature in a list of values.
   *
   * @param feature The feature.
   * @param values The list of values.
   * @param <T> A type of the feature.
   * @return A value for the specified feature if it's in the list.
   * @throws IllegalArgumentException If a value was not found.
   */
  protected <T> Value<T> find(Feature<T> feature, Value<?>... values) {
    Objects.requireNonNull(values, "Oh no! Feature values can't be null!");
    Objects.requireNonNull(feature, "Oh no! Feature can't be null!");

    for (Value<?> value : values) {
      if (feature.equals(value.feature())) {
        return (Value<T>) value;
      }
    }

    throw new IllegalArgumentException(
        String.format("Oh no! We could not find feature: %s", feature.name()));
  }

  /**
   * The method calculates a score value for a specified score if the value is not available.
   *
   * @param score The score.
   * @param values The values that should be used to calculate the score value.
   * @return The calculated score value.
   * @see #calculateIfNecessary(Score, ValueSet)
   */
  protected static ScoreValue calculateIfNecessary(Score score, Value<?>... values) {
    return calculateIfNecessary(score, ValueHashSet.from(values));
  }

  /**
   * The method calculates a value for a specified score if the value is not available.
   * First, the method checks if the set of values already contains a value for the specified score.
   * If yes, the method just returns the existing value.
   * Otherwise, the method tries to calculate a value of the specified score.
   *
   * @param score The score.
   * @param values The set of values.
   * @return A value of the specified score.
   * @throws IllegalArgumentException If a value for the score is not a {@link ScoreValue}.
   */
  protected static ScoreValue calculateIfNecessary(Score score, ValueSet values) {
    Optional<Value<Double>> something = values.of(score);

    // if the set of values doesn't contain a value for the specified score, then calculate it
    Value<Double> value = something.orElseGet(() -> UnknownValue.of(score));
    if (value.isUnknown()) {
      return score.calculate(values);
    }

    // if the set of values contain a value for the specified score, then return it
    if (value instanceof ScoreValue) {
      return (ScoreValue) value;
    }

    throw new IllegalArgumentException(String.format(
        "Hey! I expected a ScoreValue for a score but got %s!", value.getClass()));
  }

  /**
   * Checks if all values are unknown.
   *
   * @param values The values to be checked.
   * @return True if all values are unknown, false otherwise.
   * @throws IllegalArgumentException If values are empty.
   */
  protected static boolean allUnknown(Value<?>... values) {
    return allUnknown(Arrays.asList(values));
  }

  /**
   * Checks if all values are unknown.
   *
   * @param values A list of values to be checked.
   * @return True if all values are unknown, false otherwise.
   * @throws IllegalArgumentException If values are empty.
   */
  protected static boolean allUnknown(List<Value<?>> values) {
    Objects.requireNonNull(values, "Oh no! Values is null!");

    if (values.size() == 0) {
      throw new IllegalStateException("Oh no! Values is empty!");
    }

    for (Value<?> value : values) {
      if (!value.isUnknown()) {
        return false;
      }
    }

    return true;
  }

  /**
   * Checks if all values are N/A.
   *
   * @param values The values to be checked.
   * @return True if all values are N/A, false otherwise.
   * @throws IllegalArgumentException If values are empty.
   */
  protected static boolean allNotApplicable(Value<?>... values) {
    Objects.requireNonNull(values, "Oh no! Values is null!");

    if (values.length == 0) {
      throw new IllegalStateException("Oh no! Values is empty!");
    }

    for (Value<?> value : values) {
      if (!value.isNotApplicable()) {
        return false;
      }
    }

    return true;
  }
}
