package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value of a feature which indicates the availability status of any entity. This feature will
 * indicate how the entity is used in the project. Availability status mandatory, optional or not
 * found.
 */
public class AvailabilityValue extends AbstractValue<Status> {

  /**
   * A availability status value of type {@link AvailabilityStatus}.
   */
  private final Status availability;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param availability status mandatory, optional or not found.
   */
  @JsonCreator
  public AvailabilityValue(
      @JsonProperty("feature") Feature<Status> feature,
      @JsonProperty("availability") Status availability) {
    super(feature);
    this.availability = availability;
  }

  @Override
  @JsonGetter("availability")
  public Status get() {
    return availability;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof AvailabilityValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    AvailabilityValue that = (AvailabilityValue) o;
    return Objects.equals(availability, that.availability);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), availability);
  }

  @Override
  public String toString() {
    return String.format("%s",availability);
  }
}