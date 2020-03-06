package com.sap.sgs.phosphor.fosstars.model.value;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import java.util.Objects;

/**
 * The class holds a rating value produced by {@link Rating}.
 * TODO: RatingValue should implement the Value interface.
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
   * Initializes a {@link RatingValue} with a score value and a label.
   *
   * @param scoreValue The score value.
   * @param label The label.
   */
  public RatingValue(ScoreValue scoreValue, Label label) {
    Objects.requireNonNull(scoreValue, "Hey! Score value can't be null");
    Objects.requireNonNull(label, "Hey! Label can't be null!");
    this.scoreValue = scoreValue;
    this.label = label;
  }

  /**
   * Returns the score value.
   */
  public double score() {
    return scoreValue.get();
  }

  /**
   * Returns the confidence.
   */
  @Override
  public double confidence() {
    return scoreValue.confidence();
  }

  /**
   * Returns the label.
   */
  public Label label() {
    return label;
  }

  /**
   * Returns the score value.
   */
  public ScoreValue scoreValue() {
    return scoreValue;
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
    return Objects.equals(scoreValue, that.scoreValue)
        && label == that.label;
  }

  @Override
  public int hashCode() {
    return Objects.hash(scoreValue, label);
  }
}
