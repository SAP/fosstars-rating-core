package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value for a feature that holds a set of programming languages.
 */
public class LanguagesValue extends AbstractKnownValue<Languages> {

  /**
   * A language.
   */
  private final Languages languages;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param languages The languages.
   */
  @JsonCreator
  public LanguagesValue(
      @JsonProperty("feature") Feature<Languages> feature,
      @JsonProperty("languages") Languages languages) {

    super(feature);
    this.languages = Objects.requireNonNull(languages, "Languages can't be null!");
  }

  @Override
  @JsonGetter("languages")
  public Languages get() {
    return languages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof LanguagesValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    LanguagesValue that = (LanguagesValue) o;
    return Objects.equals(languages, that.languages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), languages);
  }

  @Override
  public String toString() {
    return languages.toString();
  }
}
