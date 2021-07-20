package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.Objects;

/**
 * An unknown value for a feature.
 */
public final class UnknownValue<T> extends AbstractValue<T, UnknownValue<T>> {

  /**
   * This factory method returns an unknown values of a specified feature.
   *
   * @param feature The feature.
   * @param <T> Value type.
   * @return An unknown value for the specified feature.
   */
  public static <T> UnknownValue<T> of(Feature<T> feature) {
    return new UnknownValue<>(feature);
  }

  /**
   * Initializes an unknown value for a feature.
   *
   * @param feature The feature.
   */
  public UnknownValue(@JsonProperty("feature") Feature<T> feature) {
    super(feature);
  }

  @Override
  @JsonIgnore
  public final boolean isUnknown() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isNotApplicable() {
    return false;
  }

  @Override
  public final T get() {
    throw new IllegalStateException(
        "It's an unknown value, get() method is not supposed to be called!");
  }

  @Override
  public T orElse(T other) {
    return other;
  }

  @Override
  public final Value<T> processIfKnown(Processor<T> processor) {
    return this;
  }

  @Override
  public final Value<T> processIfUnknown(Runnable processor) {
    Objects.requireNonNull(processor, "Processor can't be null!");
    processor.run();
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !UnknownValue.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return 17 * super.hashCode();
  }

  @Override
  public String toString() {
    return "Unknown";
  }
}
