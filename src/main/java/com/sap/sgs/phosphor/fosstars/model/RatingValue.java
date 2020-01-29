package com.sap.sgs.phosphor.fosstars.model;

import java.util.Objects;

/**
 * The class holds a rating value produced by {@link Rating}
 */
public class RatingValue implements Confidence {

  /**
   * A score value.
   */
  private final ScoreValue scoreValue;

  /**
   * A label.
   */
  private final Label label;

  /**
   * @param scoreValue A score value.
   * @param label A label.
   */
  public RatingValue(ScoreValue scoreValue, Label label) {
    Objects.requireNonNull(scoreValue, "Hey! Score value can't be null");
    Objects.requireNonNull(label, "Hey! Label can't be null!");
    this.scoreValue = scoreValue;
    this.label = label;
  }

  /**
   * @return The score value.
   */
  public double score() {
    return scoreValue.score();
  }

  /**
   * @return The confidence.
   */
  @Override
  public double confidence() {
    return scoreValue.confidence();
  }

  /**
   * @return The label.
   */
  public Label label() {
    return label;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof RatingValue == false) {
      return false;
    }
    RatingValue that = (RatingValue) o;
    return Objects.equals(scoreValue, that.scoreValue) &&
        Objects.equals(label, that.label);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scoreValue, label);
  }
}
