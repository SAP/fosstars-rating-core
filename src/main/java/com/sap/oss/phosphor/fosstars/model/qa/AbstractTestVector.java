package com.sap.oss.phosphor.fosstars.model.qa;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Score;
import java.util.Objects;
import java.util.Optional;

/**
 * A base class for test vectors.
 */
public abstract class AbstractTestVector implements TestVector {

  /**
   * An interval for an expected score.
   */
  final Interval expectedScore;

  /**
   * If it's set to true, then an unknown score value is expected.
   */
  final boolean expectedUnknownScore;

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
   * @param expectedUnknownScore
   *        If it's set to true, then an unknown score value is expected.
   * @param expectedNotApplicableScore
   *        If it's set to true, then a not-applicable score value is expected.
   */
  AbstractTestVector(Interval expectedScore, Label expectedLabel, String alias,
      boolean expectedUnknownScore,
      boolean expectedNotApplicableScore) {

    Objects.requireNonNull(alias, "Hey! alias can't be null!");

    if (expectedScore == null && !expectedNotApplicableScore && !expectedUnknownScore) {
      throw new IllegalArgumentException("Hey! Expected score can't be null "
          + "unless a not-applicable or unknown value is expected!");
    }

    this.expectedScore = expectedScore;
    this.expectedLabel = expectedLabel;
    this.alias = alias;
    this.expectedUnknownScore = expectedUnknownScore;
    this.expectedNotApplicableScore = expectedNotApplicableScore;
  }

  @Override
  @JsonGetter("expectedScore")
  public final Interval expectedScore() {
    return expectedScore;
  }

  @Override
  @JsonGetter("expectedUnknownScore")
  public boolean expectsUnknownScore() {
    return expectedUnknownScore;
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
    return expectedUnknownScore == that.expectedUnknownScore
        && expectedNotApplicableScore == that.expectedNotApplicableScore
        && Objects.equals(expectedScore, that.expectedScore)
        && Objects.equals(expectedLabel, that.expectedLabel)
        && Objects.equals(alias, that.alias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        expectedScore, expectedUnknownScore, expectedNotApplicableScore, expectedLabel, alias);
  }

  /**
   * Looks for a sub-score in a score.
   *
   * @param score The score.
   * @param scoreClassName A class name of the sub-score.
   * @return The sub-score if it's found.
   * @throws IllegalArgumentException If no sub-score found.
   */
  static Optional<Score> subScoreIn(Score score, String scoreClassName) {
    Class<? extends Score> scoreClass = score.getClass();
    if (scoreClassName.equals(scoreClass.getName())
        || scoreClassName.equals(scoreClass.getSimpleName())
        || scoreClassName.equals(scoreClass.getCanonicalName())) {

      return Optional.of(score);
    }

    for (Score subScore : score.subScores()) {
      Optional<Score> result = subScoreIn(subScore, scoreClassName);
      if (result.isPresent()) {
        return result;
      }
    }

    return Optional.empty();
  }
}
