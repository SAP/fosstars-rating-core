package com.sap.sgs.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.DoubleValue;
import java.util.Objects;

/**
 * A feature which holds an double which belongs to a specific interval [a, b].
 */
public class BoundedDoubleFeature extends AbstractFeature<Double> {

  /**
   * Left boundary.
   */
  private final double from;

  /**
   * Right boundary.
   */
  private final double to;

  /**
   * Initializes a new feature.
   *
   * @param name A name of the feature.
   * @param from A left boundary.
   * @param to A right boundary.
   */
  @JsonCreator
  public BoundedDoubleFeature(
      @JsonProperty("name") String name,
      @JsonProperty("from") double from,
      @JsonProperty("to") double to) {

    super(name);
    if (from == to) {
      throw new IllegalArgumentException("The interval can't be just a point!");
    }
    if (from > to) {
      throw new IllegalArgumentException("The right boundary is greater than the left one!");
    }
    this.from = from;
    this.to = to;
  }

  @Override
  public DoubleValue value(Double object) {
    return new DoubleValue(this, check(object));
  }

  @Override
  public Value<Double> parse(String string) {
    return value(Double.valueOf(string));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof BoundedDoubleFeature == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    BoundedDoubleFeature that = (BoundedDoubleFeature) o;
    return from == that.from && to == that.to;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), from, to);
  }

  /**
   * Checks if an double belongs to the interval.
   *
   * @param n The double to be checked.
   * @return n after the check. Otherwise throws an {@link IllegalArgumentException}.
   */
  protected Double check(Double n) {
    if (n < from || n > to) {
      throw new IllegalArgumentException(
          String.format("Feature value has to be in [%d, %d] but %d given!", from, to, n));
    }

    return n;
  }
}