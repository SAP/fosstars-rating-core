package com.sap.sgs.phosphor.fosstars.model.qa;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Set;

/**
 * A test vector for a rating or a score.
 * A test vector contains feature values, an interval for an expected score
 * and an optional expected label.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StandardTestVector.class)
})
public interface TestVector {

  /**
   * Shows that no expected label is specified in a test vector.
   */
  Label NO_LABEL = null;

  /**
   * Returns feature values.
   */
  Set<Value> values();

  /**
   * Returns an expected score.
   */
  Interval expectedScore();

  /**
   * Checks if the test vector has a label.
   *
   * @return True if the test vector has a label, false otherwise.
   */
  boolean hasLabel();

  /**
   * Returns an expected label.
   */
  Label expectedLabel();

  /**
   * Returns an alias.
   */
  String alias();

  /**
   * Returns true if a not-applicable score value is expected, false otherwise.
   */
  boolean expectsNotApplicableScore();

}
