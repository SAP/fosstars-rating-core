package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value for a feature that holds a string.
 */
public class StringValue extends AbstractKnownValue<String> {

  /**
   * Version of an artifact.
   */
  private final String content;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param content The versions.
   */
  @JsonCreator
  public StringValue(
      @JsonProperty("feature") Feature<String> feature,
      @JsonProperty("content") String content) {

    super(feature);
    this.content = Objects.requireNonNull(content, "Content can't be null!");
  }

  @Override
  @JsonGetter("content")
  public String get() {
    return content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof StringValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    StringValue that = (StringValue) o;
    return Objects.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), content);
  }

  @Override
  public String toString() {
    return content;
  }
}