package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value of a feature which provides an integer number.
 */
public class IntegerValue extends AbstractKnownValue<Integer> {

  /**
   * The integer.
   */
  private final Integer number;

  /**
   * Initializes an integer value of a feature.
   *
   * @param feature The feature.
   * @param number The integer.
   */
  @JsonCreator
  public IntegerValue(
      @JsonProperty("feature") Feature<Integer> feature,
      @JsonProperty("number") Integer number) {

    super(feature);
    this.number = number;
  }

  @Override
  @JsonGetter("number")
  public Integer get() {
    return number;
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
    return Objects.equals(number, that.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), number);
  }

  @Override
  public String toString() {
    return String.valueOf(number);
  }
}
