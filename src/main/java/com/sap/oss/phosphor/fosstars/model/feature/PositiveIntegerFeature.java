package com.sap.oss.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.IntegerValue;

/**
 * A feature which holds a positive integer (more or equal to 0).
 */
public class PositiveIntegerFeature extends AbstractFeature<Integer> {

  /**
   * Initializes a new feature.
   *
   * @param name A name of the feature.
   */
  @JsonCreator
  public PositiveIntegerFeature(@JsonProperty("name") String name) {
    super(name);
  }

  @Override
  public IntegerValue value(Integer object) {
    return new IntegerValue(this, check(object));
  }

  @Override
  public Value<Integer> parse(String string) {
    return value(Integer.valueOf(string));
  }

  /**
   * Checks if an integer is more or equal to 0.
   *
   * @param n The integer to be checked.
   */
  private static Integer check(Integer n) {
    if (n < 0) {
      throw new IllegalArgumentException("Feature value can't be negative!");
    }

    return n;
  }
}
