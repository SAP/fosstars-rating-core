package com.sap.oss.phosphor.fosstars.model.feature;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.DateValue;
import java.util.Date;

/**
 * A features which holds a date.
 */
public class DateFeature extends AbstractFeature<Date> {

  /**
   * Initializes a new feature.
   *
   * @param name A name of the feature.
   */
  @JsonCreator
  public DateFeature(@JsonProperty("name") String name) {
    super(name);
  }

  @Override
  public Value<Date> value(Date date) {
    return new DateValue(this, date);
  }

  @Override
  public Value<Date> parse(String string) {
    return value(date(string));
  }

}
