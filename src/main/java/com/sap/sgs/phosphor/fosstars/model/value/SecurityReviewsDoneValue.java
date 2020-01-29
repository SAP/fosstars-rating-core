package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import java.util.Objects;

/**
 * A value for the {@link com.sap.sgs.phosphor.fosstars.model.feature.oss.SecurityReviewsDone} feature.
 */
public class SecurityReviewsDoneValue implements Value<SecurityReviews> {

  /**
   * A collection of security reviews.
   */
  private final SecurityReviews reviews;

  /**
   * @param reviews A collection of security reviews.
   */
  public SecurityReviewsDoneValue(@JsonProperty("reviews") SecurityReviews reviews) {
    Objects.requireNonNull(reviews, "Reviews can't be null!");
    this.reviews = reviews;
  }

  @Override
  public final Feature<SecurityReviews> feature() {
    return OssFeatures.SECURITY_REVIEWS_DONE;
  }

  @Override
  @JsonIgnore
  public final boolean isUnknown() {
    return false;
  }

  @Override
  @JsonGetter("reviews")
  public final SecurityReviews get() {
    return reviews;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof SecurityReviewsDoneValue == false) {
      return false;
    }
    SecurityReviewsDoneValue that = (SecurityReviewsDoneValue) o;
    return Objects.equals(reviews, that.reviews);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(reviews);
  }
}
