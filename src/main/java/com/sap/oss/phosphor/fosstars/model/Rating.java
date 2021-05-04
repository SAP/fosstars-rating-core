package com.sap.oss.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import java.util.Set;

/**
 * This is an interface for a rating. A rating takes feature values and calculate a score.
 * Then, the rating can convert the score to a label.
 * All ratings have to support serialization to JSON with Jackson.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface Rating {

  /**
   * Get a name of the rating.
   *
   * @return A name of the rating.
   */
  String name();

  /**
   * Get a score that the rating uses.
   *
   * @return A score on which the rating is based.
   */
  Score score();

  /**
   * Takes a set of feature values and calculates a score.
   *
   * @param values A set of values.
   * @return A rating value.
   */
  RatingValue calculate(Set<Value<?>> values);

  /**
   * Takes a number of feature values and calculates a score.
   *
   * @param values A number of values.
   * @return A rating value.
   */
  RatingValue calculate(Value<?>... values);

  /**
   * Takes a set of feature values and calculates a score.
   *
   * @param values A set of values.
   * @return A rating value.
   */
  RatingValue calculate(ValueSet values);

  /**
   * Returns all features which are used by the rating.
   *
   * @return A number of features.
   */
  Set<Feature<?>> allFeatures();

  /**
   * Accept a visitor.
   *
   * @param visitor The visitor.
   */
  void accept(Visitor visitor);
}
