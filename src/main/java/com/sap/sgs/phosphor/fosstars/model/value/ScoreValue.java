package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Objects;

/**
 * The class holds a score produced by {@link Score}.
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
   * Initializes a score value for a specified score.
   *
   * @param score The score.
   */
  public ScoreValue(Score score) {
    this(score, Score.MIN, Confidence.MIN);
  }

  /**
   * Initializes a score value for a specified score and confidence.
   *
   * @param score The score.
   * @param value The score value.
   * @param confidence The confidence.
   */
  @JsonCreator
  public ScoreValue(
      @JsonProperty("score") Score score,
      @JsonProperty("value") double value,
      @JsonProperty("confidence") double confidence) {

    Objects.requireNonNull(score, "Score can't be null!");
    this.score = score;
    this.value = Score.check(value);
    this.confidence = Confidence.check(confidence);
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
        && Double.compare(that.confidence, confidence) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, confidence);
  }
}
