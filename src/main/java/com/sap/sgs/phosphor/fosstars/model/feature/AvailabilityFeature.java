package com.sap.sgs.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.AvailabilityValue;
import com.sap.sgs.phosphor.fosstars.model.value.Status;
import java.util.Objects;

/**
 * A feature which holds a {@link Status} value. Availability status mandatory, optional
 * or not found.
 */
public class AvailabilityFeature extends AbstractFeature<Status> {

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  @JsonCreator
  public AvailabilityFeature(@JsonProperty("name") String name) {
    super(name);
  }

  /**
   * Creates a value of the features.
   *
   * @param value of type {@link Status}.
   * @return A value of the feature.
   */
  public AvailabilityValue value(Status value) {
    return new AvailabilityValue(this, value);
  }

  @Override
  public Value<Status> parse(String string) {
    Objects.requireNonNull(string, "String can't be null");
    return value(Status.valueOf(string));
  }
}