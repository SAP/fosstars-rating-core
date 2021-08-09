package com.sap.oss.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import java.util.Objects;

/**
 * A feature which holds a boolean value. In other words, it answers to a yes/no question.
 */
public class BooleanFeature extends AbstractFeature<Boolean> {

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  @JsonCreator
  public BooleanFeature(@JsonProperty("name") String name) {
    super(name);
  }

  /**
   * Creates a true value of the feature.
   *
   * @return A true value of the feature.
   */
  public BooleanValue yes() {
    return value(true);
  }

  /**
   * Creates a false value of the feature.
   *
   * @return A false value of the feature.
   */
  public BooleanValue no() {
    return value(false);
  }

  /**
   * Creates a value of the features.
   *
   * @param value True or false.
   * @return A value of the features.
   */
  public BooleanValue value(Boolean value) {
    return new BooleanValue(this, value);
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
