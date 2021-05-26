package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;

/**
 * A not-applicable value of a feature.
 */
public final class NotApplicableValue<T> extends AbstractKnownValue<T> {

  /**
   * This factory method returns a not-applicable value of a specified feature.
   *
   * @param feature The feature.
   * @param <T> Feature value type.
   * @return A not-applicable value for the feature.
   */
  public static <T> NotApplicableValue<T> of(Feature<T> feature) {
    return new NotApplicableValue<>(feature);
  }

  /**
   * Initializes a not-applicable value for a feature.
   *
   * @param feature The feature.
   */
  public NotApplicableValue(@JsonProperty("feature") Feature<T> feature) {
    super(feature);
  }

  @Override
  @JsonIgnore
  public final boolean isNotApplicable() {
    return true;
  }

  @Override
  public final T get() {
    throw new UnsupportedOperationException(
        "It's a not-applicable value, get() method is not supposed to be called!");
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String toString() {
    return "N/A";
  }
}
