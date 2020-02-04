package com.sap.sgs.phosphor.fosstars.model;

import java.util.Objects;

/**
 * The class holds a score produced by {@link Score}.
 */
public class ScoreValue implements Confidence {

  /**
   * A score value.
   */
  private double score = Score.MIN;

  /**
   * A confidence.
   */
  private double confidence = Confidence.MIN;

  /**
   * The default constructor.
   */
  public ScoreValue() {

  }

  /**
   * @param score A score value.
   * @param confidence A confidence.
   */
  public ScoreValue(double score, double confidence) {
    this.score = Score.check(score);
    this.confidence = Confidence.check(confidence);
  }

  /**
   * Increase the score with a specified value. If the resulting score value is more than {@link Score#MAX},
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
    score += delta;
    if (score > Score.MAX) {
      score = Score.MAX;
    }
    return this;
  }

  /**
   * Decrease the score with a specified value. In the resulting score value is less than {@link Score#MIN},
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
    score -= delta;
    if (score < Score.MIN) {
      score = Score.MIN;
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
   * @return The score value.
   */
  public double score() {
    return score;
  }

  /**
   * @return The confidence.
   */
  @Override
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
    return Double.compare(that.score, score) == 0 &&
        Double.compare(that.confidence, confidence) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(score, confidence);
  }
}
