package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.Objects;

/**
 * A base class for known features values.
 * Unknown values can be created with {@link Feature#unknown()} and {@link UnknownValue}.
 */
public abstract class AbstractValue<T> implements Value<T> {

  /**
   * A corresponding feature.
   */
  private final Feature<T> feature;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   */
  AbstractValue(@JsonProperty("feature") Feature<T> feature) {
    Objects.requireNonNull(feature, "Feature can't be null!");
    this.feature = feature;
  }

  @Override
  @JsonGetter("feature")
  public final Feature<T> feature() {
    return feature;
  }

  @Override
  @JsonIgnore
  public final boolean isUnknown() {
    return false;
  }

  @Override
  @JsonIgnore
  public boolean isNotApplicable() {
    return false;
  }

  @Override
  public Value<T> processIfKnown(Processor<T> processor) {
    Objects.requireNonNull(processor, "Processor can't be null!");
    processor.process(get());
    return this;
  }

  @Override
  public Value<T> processIfUnknown(Runnable processor) {
    return this;
  }

  @Override
  public T orElse(T other) {
    return isUnknown() || isNotApplicable() ? other : get();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof AbstractValue == false) {
      return false;
    }
    AbstractValue<?> that = (AbstractValue<?>) o;
    return Objects.equals(feature, that.feature);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(feature);
  }
}
