package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.feature.oss.SecurityReviewsFeature;
import java.util.Objects;

/**
 * A value that holds security reviews.
 */
public class SecurityReviewsValue extends AbstractKnownValue<SecurityReviews> {

  /**
   * A set of security reviews.
   */
  private final SecurityReviews reviews;

  /**
   * Create a value with security reviews.
   *
   * @param feature A features for the value.
   * @param reviews A set of security reviews.
   */
  public SecurityReviewsValue(
      @JsonProperty("feature") SecurityReviewsFeature feature,
      @JsonProperty("reviews") SecurityReviews reviews) {

    super(feature);
    Objects.requireNonNull(reviews, "Reviews can't be null!");
    this.reviews = new SecurityReviews(reviews);
  }

  @Override
  @JsonGetter("reviews")
  public SecurityReviews get() {
    return new SecurityReviews(reviews);
  }

  @Override
  public String toString() {
    return reviews.toString();
  }
}
