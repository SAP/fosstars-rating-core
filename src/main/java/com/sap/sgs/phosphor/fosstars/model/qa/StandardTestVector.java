package com.sap.sgs.phosphor.fosstars.model.qa;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * A standard test vector for a rating or a score. The class is immutable.
 */
public class StandardTestVector extends AbstractTestVector {

  /**
   * A set of feature values.
   */
  private final Set<Value> values;

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

    super(expectedScore, expectedLabel, alias, expectedNotApplicableScore);

    Objects.requireNonNull(values, "Hey! Values can't be null!");

    if (values.isEmpty()) {
      throw new IllegalArgumentException("Hey! Values can't be empty");
    }

    this.values = values;
  }

  @Override
  @JsonGetter("values")
  public final Set<Value> values() {
    return Collections.unmodifiableSet(values);
  }

  @Override
  public Set<Value> valuesFor(Score score) {
    return values();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof StandardTestVector == false) {
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
