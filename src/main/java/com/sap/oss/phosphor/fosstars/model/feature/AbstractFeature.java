package com.sap.oss.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.Visitor;
import com.sap.oss.phosphor.fosstars.model.value.UnknownValue;
import java.util.Objects;

/**
 * Base class for features.
 */
public abstract class AbstractFeature<T> implements Feature<T> {

  /**
   * The feature name.
   */
  protected final String name;

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  public AbstractFeature(String name) {
    Objects.requireNonNull(name, "Hey! Name can't be null!");
    name = name.trim();
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Hey! Name can't be empty!");
    }
    this.name = name;
  }

  @Override
  @JsonGetter("name")
  public final String name() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof AbstractFeature == false) {
      return false;
    }
    AbstractFeature<?> that = (AbstractFeature<?>) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  @Override
  public final String toString() {
    return name();
  }

  @Override
  public Value<T> unknown() {
    return new UnknownValue<>(this);
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }
}
