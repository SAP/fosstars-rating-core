package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Objects;

/**
 * An unknown value for a feature.
 */
public final class UnknownValue<T> implements Value<T> {

  /**
   * A feature.
   */
  private final Feature feature;

  /**
   * This factory method returns an unknown values of the specified feature.
   */
  public static <T> Value<T> of(Feature<T> feature) {
    return new UnknownValue<>(feature);
  }

  /**
   * Initializes an unknown value for a feature.
   *
   * @param feature The feature.
   */
  public UnknownValue(@JsonProperty("feature") Feature feature) {
    this.feature = feature;
  }

  @Override
  @JsonGetter("feature")
  public final Feature feature() {
    return feature;
  }

  @Override
  @JsonIgnore
  public final boolean isUnknown() {
    return true;
  }

  @Override
  public final T get() {
    throw new UnsupportedOperationException(
        "It's an unknown value, get() method is not supposed to be called!");
  }

  @Override
  public Value<T> processIfKnown(Processor<T> processor) {
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof UnknownValue == false) {
      return false;
    }
    UnknownValue<?> that = (UnknownValue<?>) o;
    return Objects.equals(feature, that.feature);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(feature);
  }
}
