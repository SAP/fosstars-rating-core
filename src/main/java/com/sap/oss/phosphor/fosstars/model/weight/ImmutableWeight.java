package com.sap.oss.phosphor.fosstars.model.weight;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Parameter;
import com.sap.oss.phosphor.fosstars.model.Weight;
import java.util.Objects;

/**
 * An immutable weight.
 */
public class ImmutableWeight extends AbstractWeight {

  /**
   * Weight value.
   */
  private final double value;

  @JsonCreator
  public ImmutableWeight(@JsonProperty("value") double value) {
    this.value = Weight.check(value);
  }

  @Override
  public Parameter value(double v) {
    throw new UnsupportedOperationException("You can't update me because I am immutable!");
  }

  @Override
  @JsonGetter("value")
  public final Double value() {
    return value;
  }

  @Override
  @JsonIgnore
  public boolean isImmutable() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ImmutableWeight == false) {
      return false;
    }
    ImmutableWeight weight = (ImmutableWeight) o;
    return Double.compare(weight.value, value) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }
}
