package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.Objects;

/**
 * A base class for known features values.
 * Unknown values can be created with {@link Feature#unknown()} and {@link UnknownValue}.
 *
 * @param <T> Type of data.
 */
public abstract class AbstractKnownValue<T> extends AbstractValue<T, AbstractKnownValue<T>> {

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   */
  public AbstractKnownValue(@JsonProperty("feature") Feature<T> feature) {
    super(feature);
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
  public final Value<T> processIfKnown(Processor<T> processor) {
    Objects.requireNonNull(processor, "Processor can't be null!");
    processor.process(get());
    return this;
  }

  @Override
  public final Value<T> processIfUnknown(Runnable processor) {
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
    if (!AbstractKnownValue.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return 17 * super.hashCode();
  }
}
