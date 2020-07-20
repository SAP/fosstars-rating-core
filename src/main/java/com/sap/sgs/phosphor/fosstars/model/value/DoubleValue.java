package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value of a feature which provides a double number.
 */
public class DoubleValue extends AbstractValue<Double> {

  /**
   * A double value.
   */
  protected final double number;

  /**
   * Initializes a {@link DoubleValue} of a specified feature.
   *
   * @param feature The feature.
   * @param number The value.
   */
  @JsonCreator
  public DoubleValue(
      @JsonProperty("feature") Feature<Double> feature,
      @JsonProperty("number") Double number) {

    super(feature);
    this.number = number;
  }

  @Override
  @JsonGetter("number")
  public Double get() {
    return number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof DoubleValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    DoubleValue that = (DoubleValue) o;
    return Objects.equals(number, that.number);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), number);
  }
}
