package com.sap.sgs.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import java.util.Objects;

/**
 * A feature which holds a boolean value. In other words, it answers to a yes/no question.
 */
public class BooleanFeature extends AbstractFeature<Boolean> {

  @JsonCreator
  public BooleanFeature(@JsonProperty("name") String name) {
    super(name);
  }

  public BooleanValue value(Boolean object) {
    return new BooleanValue(this, object);
  }

  @Override
  public Value<Boolean> parse(String string) {
    Objects.requireNonNull(string, "String can't be null");
    switch (string.trim().toLowerCase()) {
      case "true":
        return value(true);
      case "false":
        return value(false);
      default:
        throw new IllegalArgumentException(String.format("Unexpected value: %s", string));
    }
  }

}
