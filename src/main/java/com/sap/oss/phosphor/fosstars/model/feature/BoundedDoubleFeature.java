package com.sap.oss.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.DoubleValue;
import java.util.Objects;

/**
 * A feature which holds an double which belongs to a specific interval [a, b].
 */
public class BoundedDoubleFeature extends AbstractFeature<Double> {

  /**
   * Left boundary.
   */
  protected final double from;

  /**
   * Right boundary.
   */
  protected final double to;

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
    int compare = Double.compare(from, to);
    if (compare == 0) {
      throw new IllegalArgumentException("The interval can't be just a point!");
    }
    if (compare > 0) {
      throw new IllegalArgumentException("The right boundary is lesser than the left one!");
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

    return Double.compare(from, that.from) == 0
        && Double.compare(to, that.to) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), from, to);
  }

  /**
   * Checks if an double belongs to the interval.
   *
   * @param n The double to be checked.
   * @return The same value if is't valid, otherwise throws an {@link IllegalArgumentException}.
   */
  private Double check(Double n) {
    if (Double.compare(n, from) < 0 || Double.compare(n, to) > 0) {
      throw new IllegalArgumentException(
          String.format("Feature value has to be in [%2.2f, %2.2f] but %2.2f given!", from, to, n));
    }

    return n;
  }
}