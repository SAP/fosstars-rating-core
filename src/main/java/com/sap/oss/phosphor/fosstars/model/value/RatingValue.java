package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Rating;
import java.util.Objects;

/**
 * <p>The class holds a rating value produced by {@link Rating}.</p>
 * <p>TODO: RatingValue should implement the Value interface.</p>
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
  @JsonCreator
  public RatingValue(
      @JsonProperty("scoreValue") ScoreValue scoreValue,
      @JsonProperty("label") Label label) {

    this.scoreValue = Objects.requireNonNull(scoreValue, "Hey! Score value can't be null");
    this.label = Objects.requireNonNull(label, "Hey! Label can't be null!");
  }

  /**
   * Get the score value.
   *
   * @return The score value.
   */
  public double score() {
    return scoreValue.get();
  }

  /**
   * Get the confidence.
   *
   * @return The confidence.
   */
  @Override
  public double confidence() {
    return scoreValue.confidence();
  }

  /**
   * Get the label.
   *
   * @return The label.
   */
  @JsonGetter("label")
  public Label label() {
    return label;
  }

  /**
   * Get the score value.
   *
   * @return The score value.
   */
  @JsonGetter("scoreValue")
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
