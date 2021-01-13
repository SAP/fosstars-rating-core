package com.sap.oss.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.value.EnumValue;
import java.util.Objects;

/**
 * This is a feature that holds a enum.
 *
 * @param <T> A type of the enum.
 */
public class EnumFeature<T extends Enum<T>> extends AbstractFeature<T> {

  /**
   * A class of the enum.
   */
  private final Class<T> enumClass;

  /**
   * Initializes a feature.
   *
   * @param enumClass A class of the enum.
   * @param name A name of the feature.
   */
  @JsonCreator
  public EnumFeature(
      @JsonProperty("enumClass") Class<T> enumClass,
      @JsonProperty("name") String name) {

    super(name);
    Objects.requireNonNull(enumClass, "Oh no! Enum class is null!");
    this.enumClass = enumClass;
  }

  @Override
  public EnumValue<T> value(T object) {
    return new EnumValue<>(this, object);
  }

  @Override
  public EnumValue<T> parse(String string) {
    return value(Enum.valueOf(enumClass, string));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof EnumFeature == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    EnumFeature<?> that = (EnumFeature<?>) o;
    return Objects.equals(enumClass, that.enumClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), enumClass);
  }

  /**
   * Get the class of the enum. This method is used during serialization.
   *
   * @return The class of the enum.
   */
  @JsonGetter("enumClass")
  private Class<T> enumClass() {
    return enumClass;
  }

}
