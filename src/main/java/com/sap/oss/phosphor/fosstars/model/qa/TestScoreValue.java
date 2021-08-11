package com.sap.oss.phosphor.fosstars.model.qa;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import java.util.List;
import java.util.function.Predicate;

/**
 * A score value for a test vector.
 */
public class TestScoreValue implements Value<Double> {

  /**
   * A class name of a score which the value is for.
   */
  private final String scoreClassName;

  /**
   * A score value.
   */
  private final Double value;

  /**
   * Shows whether or not the value is unknown.
   */
  private final boolean isUnknown;

  /**
   * Shows whether or not the value is N/A.
   */
  private final boolean isNotApplicable;

  /**
   * Create a new test score value.
   *
   * @param scoreClassName A class name of a score which the value is for.
   * @param value A score value.
   * @param isUnknown Shows whether or not the value is unknown.
   * @param isNotApplicable Shows whether or not the value is N/A.
   */
  public TestScoreValue(
      @JsonProperty("score") String scoreClassName,
      @JsonProperty("value") Double value,
      @JsonProperty("unknown") boolean isUnknown,
      @JsonProperty("notApplicable") boolean isNotApplicable) {

    this.scoreClassName = requireNonNull(scoreClassName, "Oops! Class name is null!");
    this.value = Score.check(value);
    this.isUnknown = isUnknown;
    this.isNotApplicable = isNotApplicable;
  }

  /**
   * Return a class name of a score which the value is for.
   *
   * @return The score class name.
   */
  public String scoreClassName() {
    return scoreClassName;
  }

  @Override
  public Double get() {
    return value;
  }

  @Override
  public Double orElse(Double other) {
    return isUnknown ? other : value;
  }

  @Override
  public boolean isUnknown() {
    return isUnknown;
  }

  @Override
  public boolean isNotApplicable() {
    return isNotApplicable;
  }

  @Override
  public Feature<Double> feature() {
    throw new UnsupportedOperationException("Oops! I can't do that!");
  }

  @Override
  public List<String> explanation() {
    throw new UnsupportedOperationException("Oops! I can't do that!");
  }

  @Override
  public Value<Double> explain(String note, Object... params) {
    throw new UnsupportedOperationException("Oops! I can't do that!");
  }

  @Override
  public Value<Double> explainIf(Predicate<Double> condition, String note, Object... params) {
    throw new UnsupportedOperationException("Oops! I can't do that!");
  }

  @Override
  public Value<Double> explainIf(Double value, String note, Object... params) {
    throw new UnsupportedOperationException("Oops! I can't do that!");
  }

  @Override
  public Value<Double> processIfKnown(Processor<Double> processor) {
    throw new UnsupportedOperationException("Oops! I can't do that!");
  }

  @Override
  public Value<Double> processIfUnknown(Runnable processor) {
    throw new UnsupportedOperationException("Oops! I can't do that!");
  }

  /**
   * Create a new test score value.
   *
   * @param scoreClass A score class.
   * @param value A score value.
   * @return A test score value.
   */
  public static TestScoreValue testScoreValue(Class<? extends Score> scoreClass, double value) {
    requireNonNull(scoreClass, "Oops! Score is null!");
    return new TestScoreValue(scoreClass.getCanonicalName(), value, false, false);
  }
}
