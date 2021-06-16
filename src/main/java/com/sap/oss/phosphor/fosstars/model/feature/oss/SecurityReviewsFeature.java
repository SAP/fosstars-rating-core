package com.sap.oss.phosphor.fosstars.model.feature.oss;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviews;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviewsValue;

/**
 * A features that holds security reviews.
 */
public class SecurityReviewsFeature extends AbstractFeature<SecurityReviews> {

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  public SecurityReviewsFeature(@JsonProperty("name") String name) {
    super(name);
  }

  @Override
  public Value<SecurityReviews> value(SecurityReviews reviews) {
    return new SecurityReviewsValue(this, reviews);
  }

  @Override
  public Value<SecurityReviews> parse(String string) {
    throw new UnsupportedOperationException("Unfortunately I can't parse security reviews");
  }
}
