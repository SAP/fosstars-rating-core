package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value of a feature which provides an integer number.
 */
public class IntegerValue extends AbstractValue<Integer> {

  private final Integer n;

  @JsonCreator
  public IntegerValue(
      @JsonProperty("feature") Feature<Integer> feature,
      @JsonProperty("n") Integer n) {

    super(feature);
    this.n = n;
  }

  @Override
  @JsonGetter("n")
  public Integer get() {
    return n;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof IntegerValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    IntegerValue that = (IntegerValue) o;
    return Objects.equals(n, that.n);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), n);
  }
}
