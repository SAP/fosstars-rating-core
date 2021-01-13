package com.sap.oss.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.IntegerValue;
import java.util.Objects;

/**
 * A feature which holds an integer which belongs to a specific interval [a, b].
 */
public class BoundedIntegerFeature extends AbstractFeature<Integer> {

  /**
   * Left boundary.
   */
  private final int from;

  /**
   * Right boundary.
   */
  private final int to;

  /**
   * Initializes a new feature.
   *
   * @param name A name of the feature.
   * @param from A left boundary.
   * @param to A right boundary.
   */
  @JsonCreator
  public BoundedIntegerFeature(
      @JsonProperty("name") String name,
      @JsonProperty("from") int from,
      @JsonProperty("to") int to) {

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
  public IntegerValue value(Integer object) {
    return new IntegerValue(this, check(object));
  }

  @Override
  public Value<Integer> parse(String string) {
    return value(Integer.valueOf(string));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof BoundedIntegerFeature == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    BoundedIntegerFeature that = (BoundedIntegerFeature) o;
    return from == that.from && to == that.to;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), from, to);
  }

  /**
   * Checks if an integer belongs to the interval.
   *
   * @param n The integer to be checked.
   */
  private Integer check(Integer n) {
    if (n < from || n > to) {
      throw new IllegalArgumentException(
          String.format("Feature value has to be in [%d, %d] but %d given!", from, to, n));
    }

    return n;
  }

}
