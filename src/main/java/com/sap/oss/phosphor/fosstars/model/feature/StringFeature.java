package com.sap.oss.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.StringValue;

/**
 * This feature holds a string.
 */
public class StringFeature extends AbstractFeature<String> {

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  @JsonCreator
  public StringFeature(@JsonProperty("name") String name) {
    super(name);
  }

  @Override
  public Value<String> value(String content) {
    return new StringValue(this, content);
  }

  @Override
  public Value<String> parse(String string) {
    return new StringValue(this, string);
  }
}