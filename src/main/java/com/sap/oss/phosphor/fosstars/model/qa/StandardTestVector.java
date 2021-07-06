package com.sap.oss.phosphor.fosstars.model.qa;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A standard test vector for a rating or a score. The class is immutable.
 */
public class StandardTestVector extends AbstractTestVector {

  /**
   * A set of feature values.
   */
  private final Set<Value<?>> values;

  /**
   * Initializes a new {@link StandardTestVector}.
   *
   * @param values A set of feature values.
   * @param expectedScore An interval for an expected score.
   * @param expectedLabel An expected label (can be null).
   * @param alias A alias of the test vector.
   */
  public StandardTestVector(
      Set<Value<?>> values, Interval expectedScore, Label expectedLabel, String alias) {

    this(values, expectedScore, expectedLabel, alias, false, false);
  }

  /**
   * Initializes a new {@link StandardTestVector}.
   *
   * @param values A set of feature values.
   * @param expectedScore An interval for an expected score.
   * @param expectedLabel An expected label (can be null).
   * @param alias A alias of the test vector.
   * @param expectedUnknownScore
   *        If it's set to true, then an unknown score value is expected.
   * @param expectedNotApplicableScore
   *        If it's set to true, then a not-applicable score value is expected.
   */
  @JsonCreator
  public StandardTestVector(
      @JsonProperty("values") Set<Value<?>> values,
      @JsonProperty("expectedScore") Interval expectedScore,
      @JsonProperty("expectedLabel") Label expectedLabel,
      @JsonProperty("alias") String alias,
      @JsonProperty(
          value = "expectedUnknownScore",
          defaultValue = "false") boolean expectedUnknownScore,
      @JsonProperty(
          value = "expectedNotApplicableScore",
          defaultValue = "false") boolean expectedNotApplicableScore) {

    super(expectedScore, expectedLabel, alias, expectedUnknownScore, expectedNotApplicableScore);

    Objects.requireNonNull(values, "Hey! Values can't be null!");

    if (values.isEmpty()) {
      throw new IllegalArgumentException("Hey! Values can't be empty");
    }

    this.values = new HashSet<>(values);
  }

  @JsonGetter("values")
  final Set<Value<?>> values() {
    return new HashSet<>(values);
  }

  @Override
  public Set<Value<?>> valuesFor(Rating rating) {
    return valuesFor(rating.score());
  }

  @Override
  public Set<Value<?>> valuesFor(Score score) {
    Set<Value<?>> convertedValues = new HashSet<>();
    for (Value<?> value : this.values) {
      convertedValues.add(prepare(value, score));
    }
    return convertedValues;
  }

  /**
   * Prepare a value for a score if necessary.
   *
   * @param value The value.
   * @param score The score.
   * @return A prepared or the same value.
   */
  private Value<?> prepare(Value<?> value, Score score) {
    if (value instanceof TestScoreValue) {
      TestScoreValue testScoreValue = (TestScoreValue) value;
      Score targetScore = subScoreIn(score, testScoreValue.scoreClassName())
          .orElseThrow(() -> new IllegalStateException(
              format("No target score found for %s", testScoreValue.scoreClassName())));
      if (testScoreValue.isUnknown()) {
        return new ScoreValue(targetScore).makeUnknown();
      }
      if (testScoreValue.isNotApplicable()) {
        return new ScoreValue(targetScore).makeNotApplicable();
      }
      return new ScoreValue(targetScore).set(testScoreValue.get());
    }

    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!StandardTestVector.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    StandardTestVector that = (StandardTestVector) o;
    return Objects.equals(values, that.values);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), values);
  }
}
