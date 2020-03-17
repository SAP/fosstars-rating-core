package com.sap.sgs.phosphor.fosstars.model.score;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.Visitor;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A base class for scores.
 */
public abstract class AbstractScore implements Score {

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
   * Initializes a new score.
   *
   * @param name A name of the score.
   */
  AbstractScore(String name) {
    this(name, EMPTY_DESCRIPTION);
  }

  /**
   * Initializes a new score.
   *
   * @param name A name of the score.
   * @param description A description of the score (may be empty).
   */
  AbstractScore(String name, String description) {
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
  public ScoreValue calculate(Set<Value> values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    return calculate(values.toArray(new Value[0]));
  }

  @Override
  public ScoreValue calculate(ValueSet values) {
    Objects.requireNonNull(values, "Hey! Value set can't be null!");
    return calculate(values.toArray());
  }

  @Override
  public void accept(Visitor visitor) {
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
  public Set<Feature> allFeatures() {
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
    return Objects.equals(name, that.name) && Objects.equals(description, this.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description);
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
  private static Set<Feature> fillOutFeatures(Score score, Set<Feature> allFeatures) {
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
  protected ScoreValue scoreValue(double value, Value... usedValues) {
    return new ScoreValue(
        this,
        Score.adjust(value),
        Weight.MAX,
        Confidence.make(usedValues),
        Arrays.asList(usedValues));
  }

  /**
   * The method tries to get a value for a specified score. First, the method checks
   * if the set of values already contains a value for the specified score. If yes, the method
   * just returns the existing value. Otherwise, the method tries to calculate a value
   * of the specified score.
   *
   * @param score The score.
   * @param values The set of values.
   * @return A value of the specified score.
   * @throws IllegalArgumentException If a value for the score is not a {@link ScoreValue}.
   */
  static ScoreValue calculateIfNecessary(Score score, ValueHashSet values) {
    Optional<Value> something = values.of(score);

    // if the set of values doesn't contains a value for the specified score, then calculate it
    Value value = something.orElseGet(() -> UnknownValue.of(score));
    if (value.isUnknown()) {
      return score.calculate(values);
    }

    // if the set of values contains a value for the specified score, then return it
    if (value instanceof ScoreValue) {
      return (ScoreValue) value;
    }

    throw new IllegalArgumentException(String.format(
        "Hey! I expected a ScoreValue for a score but got %s!", value.getClass()));
  }
}
