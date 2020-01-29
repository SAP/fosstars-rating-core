package com.sap.sgs.phosphor.fosstars.model;

import java.util.Objects;

/**
 * The class holds a score produced by {@link Score}.
 */
public class ScoreValue implements Confidence {

  /**
   * A score value.
   */
  private final double score;

  /**
   * A confidence.
   */
  private final double confidence;

  /**
   * @param score A score value.
   * @param confidence A confidence.
   */
  public ScoreValue(double score, double confidence) {
    this.score = Score.check(score);
    this.confidence = Confidence.check(confidence);
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
