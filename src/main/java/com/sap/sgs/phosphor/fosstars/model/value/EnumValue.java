package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * <p>A value of a feature which contains a enum item.</p>
 *
 * <p>For some reason, deserialization of this class doesn't work.
 * It currently fails with the "Cannot deserialize Class java.lang.Enum (of type enum) as a Bean"
 * error. It looks like a problem with Jackson Databind, maybe it is related to the
 * <a href="https://github.com/FasterXML/jackson-databind/issues/2605">issue #2605</a>.</p>
 *
 * @param <T> Enum type.
 */
public class EnumValue<T extends Enum> extends AbstractValue<T> {

  /**
   * The value item.
   */
  private final T value;

  /**
   * Initialize {@link EnumValue} for a feature with a value.
   *
   * @param feature The feature.
   * @param value The value.
   */
  @JsonCreator
  public EnumValue(
      @JsonProperty("feature") Feature feature,
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
