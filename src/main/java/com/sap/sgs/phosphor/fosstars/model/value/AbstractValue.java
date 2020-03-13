package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Objects;

/**
 * A base class for features values.
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
  public final Feature feature() {
    return feature;
  }

  @Override
  @JsonIgnore
  public boolean isUnknown() {
    return false;
  }

  @Override
  public Value<T> processIfKnown(Processor<T> processor) {
    Objects.requireNonNull(processor, "Processor can't be null!");
    if (!isUnknown()) {
      processor.process(get());
    }
    return this;
  }

  @Override
  public Value<T> processIfUnknown(Runnable processor) {
    Objects.requireNonNull(processor, "Processor can't be null!");
    if (isUnknown()) {
      processor.run();
    }
    return this;
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
