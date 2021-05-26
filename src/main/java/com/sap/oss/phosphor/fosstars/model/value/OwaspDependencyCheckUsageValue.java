package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * The value for
 * {@link com.sap.oss.phosphor.fosstars.model.feature.OwaspDependencyCheckUsageFeature}.
 */
public class OwaspDependencyCheckUsageValue extends AbstractKnownValue<OwaspDependencyCheckUsage> {

  /**
   * A usage status value of type {@link OwaspDependencyCheckUsage}.
   */
  private final OwaspDependencyCheckUsage value;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param value usage status mandatory, optional or not found.
   */
  @JsonCreator
  public OwaspDependencyCheckUsageValue(
      @JsonProperty("feature") Feature<OwaspDependencyCheckUsage> feature,
      @JsonProperty("value") OwaspDependencyCheckUsage value) {

    super(feature);
    Objects.requireNonNull(value, "Oh no! Value is null!");
    this.value = value;
  }

  @Override
  @JsonGetter("value")
  public OwaspDependencyCheckUsage get() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof OwaspDependencyCheckUsageValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    OwaspDependencyCheckUsageValue that = (OwaspDependencyCheckUsageValue) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}