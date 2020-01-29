package com.sap.sgs.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import java.util.Date;
import java.util.Objects;

/**
 * A value of a feature which provides a date.
 */
public class DateValue extends AbstractValue<Date> {

  /**
   * The date.
   */
  private final Date date;

  @JsonCreator
  public DateValue(
      @JsonProperty("feature") Feature<Date> feature,
      @JsonProperty("date") Date date) {

    super(feature);
    this.date = date;
  }

  @Override
  @JsonGetter("date")
  public Date get() {
    return date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof DateValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    DateValue dateValue = (DateValue) o;
    return Objects.equals(date, dateValue.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), date);
  }
}
