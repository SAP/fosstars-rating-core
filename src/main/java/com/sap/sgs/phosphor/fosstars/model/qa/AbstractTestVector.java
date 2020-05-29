package com.sap.sgs.phosphor.fosstars.model.qa;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Label;
import java.util.Objects;

/**
 * A base class for test vectors.
 */
public abstract class AbstractTestVector implements TestVector {

  /**
   * An interval for an expected score.
   */
  final Interval expectedScore;

  /**
   * If it's set to true, then a not-applicable score value is expected.
   */
  final boolean expectedNotApplicableScore;

  /**
   * An expected label.
   */
  final Label expectedLabel;

  /**
   * An alias of the test vector.
   */
  final String alias;

  /**
   * Initializes a new {@link StandardTestVector}.
   *
   * @param expectedScore An interval for an expected score.
   * @param expectedLabel An expected label (can be null).
   * @param alias A alias of the test vector.
   * @param expectedNotApplicableScore
   *        If it's set to true, then a not-applicable score value is expected.
   */
  AbstractTestVector(Interval expectedScore, Label expectedLabel, String alias,
      boolean expectedNotApplicableScore) {

    Objects.requireNonNull(alias, "Hey! alias can't be null!");

    if (expectedScore == null && !expectedNotApplicableScore) {
      throw new IllegalArgumentException(
          "Hey! Expected score can't be null unless a not-applicable value is expected!");
    }

    this.expectedScore = expectedScore;
    this.expectedLabel = expectedLabel;
    this.alias = alias;
    this.expectedNotApplicableScore = expectedNotApplicableScore;
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
    if (o instanceof AbstractTestVector == false) {
      return false;
    }
    AbstractTestVector that = (AbstractTestVector) o;
    return expectedNotApplicableScore == that.expectedNotApplicableScore
        && Objects.equals(expectedScore, that.expectedScore)
        && Objects.equals(expectedLabel, that.expectedLabel)
        && Objects.equals(alias, that.alias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(expectedScore, expectedNotApplicableScore, expectedLabel, alias);
  }
}
