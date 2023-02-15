package com.sap.oss.phosphor.fosstars.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Set;

/**
 * <p>This is an interface for a score.
 * A score can be based on features and other scores (sub-scores).
 * A score takes a number of features, and calculates a score values based on them.
 * The score value has to belong to the interval [0, 10].</p>
 *
 * <p>A score itself is a feature which holds a score value (a double in the range [0, 10])
 * for a specific score.</p>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface Score extends Feature<Double> {

  /**
   * The minimum value of a score.
   */
  double MIN = 0.00;

  /**
   * The maximum value of a score.
   */
  double MAX = 10.0;

  /**
   * A valid interval for a score value.
   */
  Interval INTERVAL = DoubleInterval.init().from(0).to(10).closed().make();

  /**
   * Get a name of the score.
   *
   * @return A name of the score.
   */
  String name();

  /**
   * Get a description of the score.
   *
   * @return A description of the score.
   */
  String description();

  /**
   * Get a set of features which the score directly uses.
   *
   * @return A set of features which the score uses directly.
   */
  Set<Feature<?>> features();

  /**
   * Returns all features which are used by the score.
   *
   * @return A number of features.
   */
  Set<Feature<?>> allFeatures();

  /**
   * Get sub-scores which the score directly uses.
   *
   * @return A set of sub-scores which the score uses directly.
   */
  Set<Score> subScores();

  /**
   * Takes a set of values and calculates a score.
   *
   * @param values A set of values.
   * @return A score value.
   * @throws IllegalArgumentException If the provided values don't contain required features
   *                                  which are used by the score.
   */
  ScoreValue calculate(Set<Value<?>> values);

  /**
   * Takes a number of values and calculates a score.
   *
   * @param values A number of values.
   * @return A score value.
   * @throws IllegalArgumentException If the provided values don't contain required features
   *                                  which are used by the score.
   */
  ScoreValue calculate(Value<?>... values);

  /**
   * Takes an instance of {@link ValueSet} and calculates a score.
   *
   * @param values A set of values.
   * @return A score value.
   * @throws IllegalArgumentException If the provided values don't contain required features
   *                                  which are used by the score.
   */
  ScoreValue calculate(ValueSet values);

  /**
   * Accept a visitor.
   *
   * @param visitor The visitor.
   */
  void accept(Visitor visitor);

  /**
   * Checks if a score is correct.
   *
   * @param score The score to be checked.
   * @return The same score if it's correct.
   * @throws IllegalArgumentException If the score is not correct.
   */
  static double check(double score) {
    if (score < MIN || score > MAX) {
      throw new IllegalArgumentException(
          String.format("Score is not in the range [0, 1]: %f", score));
    }
    return score;
  }

  /**
   * Checks if a score is in the valid range, and returns an adjusted value if necessary.
   *
   * @param value A score to be checked.
   * @return {@link #MIN} if the score is less than {@link Score#MIN},
   *         {@link #MAX} if the score is greater than {@link Score#MAX},
   *         or the original score otherwise.
   */
  static double adjust(double value) {
    if (value < Score.MIN) {
      return Score.MIN;
    }
    if (value > Score.MAX) {
      return Score.MAX;
    }
    return value;
  }

  /**
   * Get an Interval with the provided range.
   *
   * @param min An interval start value.
   * @param max An interval end value.
   * @return Interval with the range provided from min and max param values.
   */
  static Interval makeInterval(double min, double max) {
    return DoubleInterval.init().from(min).to(max).closed().make();
  }
}
