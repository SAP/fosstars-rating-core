package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * <p>A value of a feature that contains a enum item.</p>
 *
 * @param <T> Enum type.
 */
public class EnumValue<T extends Enum<T>> extends AbstractKnownValue<T> {

  /**
   * The value item.
   */
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
  private final T value;

  /**
   * Initialize {@link EnumValue} for a feature with a value.
   *
   * @param feature The feature.
   * @param value The value.
   */
  @JsonCreator
  public EnumValue(
      @JsonProperty("feature") Feature<T> feature,
      @JsonProperty("value") T value) {

    super(feature);

    Objects.requireNonNull(value, "Value can't be null!");
    this.value = value;
  }

  @Override
  @JsonGetter("value")
  public T get() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof EnumValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    EnumValue<?> enumValue = (EnumValue<?>) o;
    return Objects.equals(value, enumValue.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
