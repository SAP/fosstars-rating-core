package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The class holds a score value produced by {@link Score}.
 */
public class ScoreValue implements Value<Double>, Confidence {

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
   * A list of values which were used to build the score value.
   */
  private final List<Value> usedValues;

  /**
   * Initializes a score value for a specified score.
   *
   * @param score The score.
   */
  public ScoreValue(Score score) {
    this(score, Score.MIN, Confidence.MIN, Collections.emptyList());
  }

  /**
   * Initializes a score value for a specified score.
   *
   * @param score The score.
   * @param value The score value.
   * @param confidence The confidence.
   * @param usedValues The values which were used to produce the score value.
   */
  public ScoreValue(Score score, double value, double confidence, Value... usedValues) {
    this(score, value, confidence, Arrays.asList(usedValues));
  }

  /**
   * Initializes a score value for a specified score.
   *
   * @param score The score.
   * @param value The score value.
   * @param confidence The confidence.
   * @param usedValues A list of values which were used to produce the score value.
   */
  @JsonCreator
  public ScoreValue(
      @JsonProperty("score") Score score,
      @JsonProperty("value") double value,
      @JsonProperty("confidence") double confidence,
      @JsonProperty("usedValues") List<Value> usedValues) {

    this.score = Objects.requireNonNull(score, "Score can't be null!");
    this.value = Score.check(value);
    this.confidence = Confidence.check(confidence);
    this.usedValues = new ArrayList<>(Objects.requireNonNull(usedValues, "Values can't be null!"));
  }

  @Override
  @JsonGetter("score")
  public Score feature() {
    return score;
  }

  @Override
  @JsonIgnore
  public boolean isUnknown() {
    return false;
  }

  @Override
  @JsonGetter("value")
  public Double get() {
    return value;
  }

  @JsonGetter("usedValues")
  public List<Value> usedValues() {
    return usedValues;
  }

  /**
   * Add a number of values to the list of used values.
   *
   * @param values The values to be added.
   * @return The same score value.
   */
  public ScoreValue usedValues(Value... values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    this.usedValues.addAll(Arrays.asList(values));
    return this;
  }

  @Override
  public Value<Double> processIfKnown(Processor<Double> processor) {
    if (!isUnknown()) {
      processor.process(get());
    }
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

  /**
   * Returns the confidence.
   */
  @Override
  @JsonGetter("confidence")
  public double confidence() {
    return confidence;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ScoreValue == false) {
      return false;
    }
    ScoreValue that = (ScoreValue) o;
    return Double.compare(that.value, value) == 0
        && Double.compare(that.confidence, confidence) == 0
        && Objects.equals(score, that.score)
        && Objects.equals(usedValues, that.usedValues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score, value, confidence, usedValues);
  }
}
