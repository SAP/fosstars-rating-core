package com.sap.oss.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.DoubleValue;
import java.util.Objects;

/**
 * A feature which holds a double.
 */
public class DoubleFeature extends AbstractFeature<Double> {

  /**
   * Initializes a new feature.
   *
   * @param name A name of the feature.
   */
  @JsonCreator
  public DoubleFeature(@JsonProperty("name") String name) {
    super(name);
  }

  @Override
  public DoubleValue value(Double n) {
    return new DoubleValue(this, n);
  }

  @Override
  public Value<Double> parse(String string) {
    Objects.requireNonNull(string, "Hey! String can't be null!");
    if (string.isEmpty()) {
      throw new IllegalArgumentException("Hey! String can't be empty!");
    }
    return value(Double.parseDouble(string));
  }
}
