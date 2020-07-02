package com.sap.sgs.phosphor.fosstars.model.qa;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Set;

/**
 * A test vector for a rating or a score.
 * A test vector contains feature values, an interval for an expected score
 * and an optional expected label.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StandardTestVector.class),
    @JsonSubTypes.Type(value = ScoreTestVector.class)
})
public interface TestVector {

  /**
   * Shows that no expected label is specified in a test vector.
   */
  Label NO_LABEL = null;

  /**
   * Get a set of feature values.
   *
   * @return The values.
   */
  Set<Value> values();

  /**
   * Returns the values resolved for a specified score.
   *
   * @param score The score.
   * @return The values.
   */
  Set<Value> valuesFor(Score score);

  /**
   * Returns an expected interval for a score value.
   *
   * @return An exptected interval for score value.
   */
  Interval expectedScore();

  /**
   * Checks if the test vector has a label.
   *
   * @return True if the test vector has a label, false otherwise.
   */
  boolean hasLabel();

  /**
   * Get an exptected label.
   *
   * @return An expected label.
   */
  Label expectedLabel();

  /**
   * Get an alias of the test vector.
   *
   * @return An alias.
   */
  String alias();

  /**
   * Tells if the test vector expects a N/A value.
   *
   * @return True if a not-applicable score value is expected, false otherwise.
   */
  boolean expectsNotApplicableScore();

}
