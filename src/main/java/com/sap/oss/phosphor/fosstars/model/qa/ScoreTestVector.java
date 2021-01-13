package com.sap.oss.phosphor.fosstars.model.qa;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A test vector for a score.
 */
public class ScoreTestVector extends AbstractTestVector {

  /**
   * Maps score classes to its values.
   */
  private final Map<Class<? extends Score>, Double> values;

  /**
   * Initializes a new {@link ScoreTestVector}.
   *
   * @param values A map of score values.
   * @param expectedScore An interval for an expected score.
   * @param expectedLabel An expected label (can be null).
   * @param alias A alias of the test vector.
   * @param expectedUnknownScore
   *        If it's set to true, then an unknown score value is expected.
   * @param expectedNotApplicableScore
   *        If it's set to true, then a not-applicable score value is expected.
   */
  @JsonCreator
  public ScoreTestVector(
      @JsonProperty("values") Map<Class<? extends Score>, Double> values,
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

    this.values = values;
  }

  @Override
  public Set<Value> values() {
    throw new UnsupportedOperationException("I need a score to create values!");
  }

  @Override
  public Set<Value> valuesFor(Score score) {
    Objects.requireNonNull(score, "Oh no! Score is null!");

    Set<Value> result = new HashSet<>();
    for (Map.Entry<Class<? extends Score>, Double> entry : values.entrySet()) {
      Score subScore = subScore(score, entry.getKey());
      Value value = subScore.value(entry.getValue());
      result.add(value);
    }

    return result;
  }

  /*
   * This getter is necessary for serialization.
   */
  @JsonGetter("values")
  private Map<Class<? extends Score>, Double> rawValues() {
    return values;
  }

  /**
   * Looks for a sub-score in a score.
   *
   * @param score The score.
   * @param scoreClass A class of the sub-score.
   * @return The sub-score if it's found.
   * @throws IllegalArgumentException If no sub-score found.
   */
  private static Score subScore(Score score, Class<? extends Score> scoreClass) {
    for (Score subScore : score.subScores()) {
      if (scoreClass.equals(subScore.getClass())) {
        return subScore;
      }
    }

    throw new IllegalArgumentException(
        String.format("Could not fine sub-score %s!", scoreClass.getCanonicalName()));
  }
}
