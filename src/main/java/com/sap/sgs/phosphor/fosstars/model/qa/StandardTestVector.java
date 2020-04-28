package com.sap.sgs.phosphor.fosstars.model.qa;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * A standard test vector for a rating or a score. The class is immutable.
 */
public class StandardTestVector implements TestVector {

  /**
   * A set of feature values.
   */
  private final Set<Value> values;

  /**
   * An interval for an expected score.
   */
  private final Interval expectedScore;

  /**
   * If it's set to true, then a not-applicable score value is expected.
   */
  private final boolean expectedNotApplicableScore;

  /**
   * An expected label.
   */
  private final Label expectedLabel;

  /**
   * An alias of the test vector.
   */
  private final String alias;

  /**
   * Initializes a new {@link StandardTestVector}.
   *
   * @param values A set of feature values.
   * @param expectedScore An interval for an expected score.
   * @param expectedLabel An expected label (can be null).
   * @param alias A alias of the test vector.
   */
  public StandardTestVector(
      Set<Value> values, Interval expectedScore, Label expectedLabel, String alias) {

    this(values, expectedScore, expectedLabel, alias, false);
  }

  /**
   * Initializes a new {@link StandardTestVector}.
   *
   * @param values A set of feature values.
   * @param expectedScore An interval for an expected score.
   * @param expectedLabel An expected label (can be null).
   * @param alias A alias of the test vector.
   * @param expectedNotApplicableScore
   *        If it's set to true, then a not-applicable score value is expected.
   */
  @JsonCreator
  public StandardTestVector(
      @JsonProperty("values") Set<Value> values,
      @JsonProperty("expectedScore") Interval expectedScore,
      @JsonProperty("expectedLabel") Label expectedLabel,
      @JsonProperty("alias") String alias,
      @JsonProperty(
          value = "expectedNotApplicableScore",
          defaultValue = "false") boolean expectedNotApplicableScore) {

    Objects.requireNonNull(values, "Hey! Values can't be null!");
    Objects.requireNonNull(alias, "Hey! alias can't be null!");

    if (values.isEmpty()) {
      throw new IllegalArgumentException("Hey! Values can't be empty");
    }

    if (expectedScore == null && !expectedNotApplicableScore) {
      throw new IllegalArgumentException(
          "Hey! Expected score can't be null unless a not-applicable value is expected!");
    }

    this.values = values;
    this.expectedScore = expectedScore;
    this.expectedLabel = expectedLabel;
    this.alias = alias;
    this.expectedNotApplicableScore = expectedNotApplicableScore;
  }

  @Override
  @JsonGetter("values")
  public final Set<Value> values() {
    return Collections.unmodifiableSet(values);
  }

  @Override
  @JsonGetter("expectedScore")
  public final Interval expectedScore() {
    return expectedScore;
  }

  @Override
  @JsonGetter("expectedNotApplicableScore")
  public boolean expectsNotApplicableScore() {
    return expectedNotApplicableScore;
  }

  @Override
  public boolean hasLabel() {
    return expectedLabel != NO_LABEL;
  }

  @Override
  @JsonGetter("expectedLabel")
  public final Label expectedLabel() {
    return expectedLabel;
  }

  @Override
  @JsonGetter("alias")
  public final String alias() {
    return alias;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof StandardTestVector == false) {
      return false;
    }
    StandardTestVector vector = (StandardTestVector) o;
    return Objects.equals(values, vector.values)
        && Objects.equals(expectedScore, vector.expectedScore)
        && Objects.equals(expectedLabel, vector.expectedLabel)
        && Objects.equals(alias, vector.alias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(values, expectedScore, expectedLabel, alias);
  }

}
