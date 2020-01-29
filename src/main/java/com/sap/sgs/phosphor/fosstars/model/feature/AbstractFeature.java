package com.sap.sgs.phosphor.fosstars.model.feature;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.Visitor;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import java.util.Objects;

/**
 * Base class for features.
 */
public abstract class AbstractFeature<T> implements Feature<T> {

  /**
   * Feature name.
   */
  protected final String name;

  /**
   * @param name Feature name.
   */
  public AbstractFeature(String name) {
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
