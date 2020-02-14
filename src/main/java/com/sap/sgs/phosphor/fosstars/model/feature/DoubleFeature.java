package com.sap.sgs.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.DoubleValue;

/**
 * A feature which holds a double.
 */
public class DoubleFeature extends AbstractFeature<Double> {

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
    return value(Double.parseDouble(string));
  }
}
