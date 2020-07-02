package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value of a feature which answers a yes/no question.
 */
public class BooleanValue extends AbstractValue<Boolean> {

  /**
   * A boolean value.
   */
  private final Boolean flag;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param flag True or false.
   */
  @JsonCreator
  public BooleanValue(
      @JsonProperty("feature") Feature<Boolean> feature,
      @JsonProperty("flag") Boolean flag) {

    super(feature);
    this.flag = flag;
  }

  @Override
  @JsonGetter("flag")
  public Boolean get() {
    return flag;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof BooleanValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    BooleanValue that = (BooleanValue) o;
    return Objects.equals(flag, that.flag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), flag);
  }

  @Override
  public String toString() {
    return flag ? "Yes" : "No";
  }
}
