package com.sap.oss.phosphor.fosstars.model.feature.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.oss.phosphor.fosstars.model.value.Languages;
import com.sap.oss.phosphor.fosstars.model.value.LanguagesValue;

/**
 * This feature contains a set of programming languages that are used
 * in an open-source project.
 */
public class LanguagesFeature extends AbstractFeature<Languages> {

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  @JsonCreator
  public LanguagesFeature(@JsonProperty("name") String name) {
    super(name);
  }

  @Override
  public Value<Languages> value(Languages languages) {
    return new LanguagesValue(this, languages);
  }

  @Override
  public Value<Languages> parse(String string) {
    throw new UnsupportedOperationException("Unfortunately I can't parse languages");
  }
}
