package com.sap.sgs.phosphor.fosstars.model.feature.oss;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviews;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviewsDoneValue;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import java.io.IOException;

/**
 * This feature contains information about security reviews
 * which have been done for an open-source project.
 */
public class SecurityReviewsDone extends AbstractFeature<SecurityReviews> {

  /**
   * For deserialization.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
  }

  /**
   * Package-private constructor.
   */
  SecurityReviewsDone() {
    super("Security reviews for an open-source project");
  }

  @Override
  public Value<SecurityReviews> value(SecurityReviews reviews) {
    return new SecurityReviewsDoneValue(reviews);
  }

  /**
   * Takes a JSON string and tries to convert it to a {@link Value} object.
   *
   * @param string The JSON string.
   * @return An instance of {@link Value}.
   * @throws IllegalArgumentException If something went wrong.
   */
  @Override
  public Value<SecurityReviews> parse(String string) {
    try {
      return value(MAPPER.readValue(string, SecurityReviews.class));
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not parse security reviews!", e);
    }
  }

  @Override
  public Value<SecurityReviews> unknown() {
    return new UnknownValue<>(this);
  }
}
