package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Date;
import java.util.Objects;

/**
 * A value of a feature which provides a date.
 */
public class DateValue extends AbstractKnownValue<Date> {

  /**
   * The date.
   */
  @JsonFormat(
      shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
      locale = "us_US")
  private final Date date;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param date A date.
   */
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
