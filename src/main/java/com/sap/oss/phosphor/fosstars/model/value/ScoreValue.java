package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.Weight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The class holds a score value produced by {@link Score}.
 */
public class ScoreValue extends AbstractValue<Double, ScoreValue> implements Confidence {

  /**
   * A score.
   */
  private final Score score;

  /**
   * A score value.
   */
  private double value;

  /**
   * A confidence.
   */
  private double confidence;

  /**
   * A weight of the score.
   */
  private double weight;

  /**
   * A list of values which were used to build the score value.
   */
  private final List<Value<?>> usedValues;

  /**
   * A flag that tells if the score value is unknown.
   */
  private boolean isUnknown;

  /**
   * A flag that tells if the score value is not applicable.
   */
  private boolean isNotApplicable;

  /**
   * Initializes a score value for a specified score.
   *
   * @param score The score.
   */
  public ScoreValue(Score score) {
    this(score, Score.MIN, Weight.MAX, Confidence.MIN, Collections.emptyList());
  }

  /**
   * Initializes a score value for a specified score.
   *
   * @param score The score.
   * @param value The score value.
   * @param weight The weight.
   * @param confidence The confidence.
   * @param usedValues A list of values which were used to produce the score value.
   */
  public ScoreValue(
      Score score, double value, double weight, double confidence, List<Value<?>> usedValues) {

    this(score, value, weight, confidence, usedValues, Collections.emptyList(), false, false);
  }

  /**
   * Initializes a score value for a specified score.
   *
   * @param score The score.
   * @param value The score value.
   * @param weight The weight.
   * @param confidence The confidence.
   * @param usedValues A list of values which were used to produce the score value.
   * @param explanation A list of explanation which explain how the score value was calculated.
   * @param isUnknown A flag that indicates that the value is unknown.
   * @param isNotApplicable A flag that indicates that the value is N/A.
   */
  @JsonCreator
  public ScoreValue(
      @JsonProperty("score") Score score,
      @JsonProperty("value") double value,
      @JsonProperty("weight") double weight,
      @JsonProperty("confidence") double confidence,
      @JsonProperty("usedValues") List<Value<?>> usedValues,
      @JsonProperty("explanation") List<String> explanation,
      @JsonProperty(value = "isUnknown", defaultValue = "false") boolean isUnknown,
      @JsonProperty(value = "isNotApplicable", defaultValue = "false") boolean isNotApplicable) {

    super(score, explanation);

    Objects.requireNonNull(usedValues, "Values can't be null!");

    this.score = score;
    this.value = Score.check(value);
    this.weight = Weight.check(weight);
    this.confidence = Confidence.check(confidence);
    this.usedValues = new ArrayList<>(usedValues);
    this.isUnknown = isUnknown;
    this.isNotApplicable = isNotApplicable;
  }

  @JsonGetter("score")
  public Score score() {
    return score;
  }

  @Override
  @JsonGetter("isUnknown")
  public boolean isUnknown() {
    return isUnknown;
  }

  @Override
  @JsonGetter("isNotApplicable")
  public boolean isNotApplicable() {
    return isNotApplicable;
  }

  /*
   * This is only for deserialization.
   */
  @JsonGetter("value")
  private Double value() {
    return value;
  }

  @Override
  public Double get() {
    if (isUnknown()) {
      throw new IllegalStateException(
          "It's an unknown value, get() method is not supposed to be called!");
    }
    return value;
  }

  @Override
  public Double orElse(Double other) {
    return isUnknown() || isNotApplicable() ? other : get();
  }

  /**
   * Get a list of used values.
   *
   * @return A list of values which were used to calculate the score value.
   */
  @JsonGetter("usedValues")
  public List<Value<?>> usedValues() {
    return new ArrayList<>(usedValues);
  }

  /**
   * Add a number of values to the list of used values.
   *
   * @param values The values to be added.
   * @return The same score value.
   */
  public ScoreValue usedValues(Value<?>... values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    Collections.addAll(usedValues, values);
    return this;
  }

  /**
   * Get a list of feature values that are used in the score value
   * or in its underlying score values.
   *
   * @return A list of feature values.
   */
  public List<Value<?>> usedFeatureValues() {
    return usedFeatureValuesIn(this);
  }

  /**
   * Get a list of feature values that are used in a score value
   * or in its underlying score values.
   *
   * @param scoreValue The score value to be checked.
   * @return A list of feature values.
   */
  private static List<Value<?>> usedFeatureValuesIn(ScoreValue scoreValue) {
    List<Value<?>> usedFeatureValues = new ArrayList<>();

    for (Value<?> value : scoreValue.usedValues) {
      if (value instanceof ScoreValue) {
        usedFeatureValues.addAll(usedFeatureValuesIn((ScoreValue) value));
      } else {
        usedFeatureValues.add(value);
      }
    }

    return usedFeatureValues;
  }

  /**
   * Recursively looks for a used sub-score value of a specified score.
   *
   * @param subScoreClass A class of the sub-score.
   * @return An {@link Optional} with the sub-score value.
   */
  public Optional<ScoreValue> findUsedSubScoreValue(Class<? extends Score> subScoreClass) {
    return findUsedSubScoreValueIn(this, subScoreClass);
  }

  /**
   * Recursively looks for a used sub-score value of a specified score.
   *
   * @param scoreValue The score value to be checked.
   * @param subScoreClass A class of the sub-score.
   * @return An {@link Optional} with the sub-score value.
   */
  private static Optional<ScoreValue> findUsedSubScoreValueIn(
      ScoreValue scoreValue, Class<? extends Score> subScoreClass) {

    for (Value<?> usedValue : scoreValue.usedValues) {
      if (usedValue instanceof ScoreValue) {
        ScoreValue subScoreValue = (ScoreValue) usedValue;

        if (subScoreClass.isInstance(subScoreValue.score)) {
          return Optional.of(subScoreValue);
        }

        Optional<ScoreValue> result = findUsedSubScoreValueIn(subScoreValue, subScoreClass);
        if (result.isPresent()) {
          return result;
        }
      }
    }

    return Optional.empty();
  }

  /**
   * Get the weight of the score value.
   *
   * @return The weight of the score value.
   */
  @JsonGetter("weight")
  public Double weight() {
    return weight;
  }

  /**
   * Sets a weight.
   *
   * @param value The weight to be set.
   * @return The same score value.
   * @throws IllegalArgumentException If the weight is incorrect.
   */
  public ScoreValue weight(double value) {
    this.weight = Weight.check(value);
    return this;
  }

  @Override
  public ScoreValue processIfKnown(Processor<Double> processor) {
    Objects.requireNonNull(processor, "Processor can't be null!");
    if (!isUnknown()) {
      processor.process(get());
    }
    return this;
  }

  @Override
  public ScoreValue processIfUnknown(Runnable processor) {
    Objects.requireNonNull(processor, "Processor can't be null!");
    if (isUnknown()) {
      processor.run();
    }
    return this;
  }

  /**
   * Set a value.
   *
   * @param value The value.
   * @return The same score value.
   */
  public ScoreValue set(double value) {
    this.value = Score.adjust(value);
    return this;
  }

  /**
   * Increase the score with a specified value.
   * If the resulting score value is more than {@link Score#MAX},
   * then the score value is set to {@link Score#MAX}.
   *
   * @param delta The value (must be positive).
   * @return This ScoreValue instance.
   * @throws IllegalArgumentException If the delta is negative.
   */
  public ScoreValue increase(double delta) {
    if (delta < 0) {
      throw new IllegalArgumentException("Delta can't be negative!");
    }
    value += delta;
    if (value > Score.MAX) {
      value = Score.MAX;
    }
    return this;
  }

  /**
   * Decrease the score with a specified value.
   * In the resulting score value is less than {@link Score#MIN},
   * then the score value is set to {@link Score#MIN}.
   *
   * @param delta The value (must be positive).
   * @return This ScoreValue instance.
   * @throws IllegalArgumentException If the delta is negative.
   */
  public ScoreValue decrease(double delta) {
    if (delta < 0) {
      throw new IllegalArgumentException("Delta can't be negative!");
    }
    value -= delta;
    if (value < Score.MIN) {
      value = Score.MIN;
    }
    return this;
  }

  /**
   * Set a confidence value.
   *
   * @param value The confidence value to be set.
   * @return This ScoreValue instance.
   */
  public ScoreValue confidence(double value) {
    this.confidence = Confidence.check(value);
    return this;
  }

  @Override
  @JsonGetter("confidence")
  public double confidence() {
    return confidence;
  }

  /**
   * Set the confidence to {@link Confidence#MIN}.
   *
   * @return The same score value.
   */
  public ScoreValue withMinConfidence() {
    this.confidence = Confidence.MIN;
    return this;
  }

  /**
   * Mark the value as not-applicable.
   *
   * @return The same score value.
   */
  public ScoreValue makeNotApplicable() {
    isNotApplicable = true;
    return this;
  }

  /**
   * Mark the value as unknown.
   *
   * @return The same score value.
   */
  public ScoreValue makeUnknown() {
    isUnknown = true;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !ScoreValue.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ScoreValue that = (ScoreValue) o;
    return Double.compare(that.value, value) == 0
        && Double.compare(that.confidence, confidence) == 0
        && Double.compare(that.weight, weight) == 0 && isUnknown == that.isUnknown
        && isNotApplicable == that.isNotApplicable
        && score.equals(that.score) && usedValues.equals(that.usedValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        super.hashCode(), score, value, confidence, weight, usedValues, isUnknown, isNotApplicable);
  }
}
