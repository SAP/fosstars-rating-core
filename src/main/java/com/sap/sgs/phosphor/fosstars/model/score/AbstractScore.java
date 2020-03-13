package com.sap.sgs.phosphor.fosstars.model.score;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.Visitor;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A base class for scores.
 */
public abstract class AbstractScore implements Score {

  static final String EMPTY_DESCRIPTION = "";

  /**
   * Score name.
   */
  private final String name;

  /**
   * Description.
   */
  @JsonIgnore
  private final String description;

  /**
   * Initializes a new score.
   *
   * @param name A name of the score.
   */
  AbstractScore(String name) {
    this(name, EMPTY_DESCRIPTION);
  }

  /**
   * Initializes a new score.
   *
   * @param name A name of the score.
   * @param description A description of the score (may be empty).
   */
  AbstractScore(String name, String description) {
    Objects.requireNonNull(name, "Hey! Score name can't be null!");
    Objects.requireNonNull(description, "Hey! Score description can't be null!");
    if (name.isEmpty()) {
      throw new IllegalArgumentException("Hey! Score name can't be empty!");
    }
    this.name = name;
    this.description = description;
  }

  @Override
  @JsonGetter("name")
  public final String name() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public ScoreValue calculate(Set<Value> values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    return calculate(values.toArray(new Value[0]));
  }

  @Override
  public ScoreValue calculate(ValueSet values) {
    Objects.requireNonNull(values, "Hey! Value set can't be null!");
    return calculate(values.toArray());
  }

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  @Override
  public Value<Double> unknown() {
    return UnknownValue.of(this);
  }

  @Override
  public Value<Double> value(Double n) {
    return new ScoreValue(
        this, Score.check(n), Weight.MAX, Confidence.MAX, Collections.emptyList());
  }

  @Override
  public Value<Double> parse(String string) {
    return value(Double.parseDouble(string));
  }

  @Override
  public Set<Feature> allFeatures() {
    Set<Feature> allFeatures = new HashSet<>();
    fillOutFeatures(this, allFeatures);
    return Collections.unmodifiableSet(allFeatures);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof AbstractScore == false) {
      return false;
    }
    AbstractScore that = (AbstractScore) o;
    return Objects.equals(name, that.name) && Objects.equals(description, this.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description);
  }

  private static void fillOutFeatures(Score score, Set<Feature> allFeatures) {
    allFeatures.addAll(score.features());
    for (Score subScore : score.subScores()) {
      fillOutFeatures(subScore, allFeatures);
    }
  }

  /**
   * Initializes a score value for the score.
   * The method adjusts the specified score value so that it fits to the valid range [0, 10].
   *
   * @param value The score value.
   * @param usedValues The values which were used to produce the score value.
   * @return A new {@link ScoreValue}.
   */
  protected ScoreValue scoreValue(double value, Value... usedValues) {
    return new ScoreValue(
        this,
        Score.adjust(value),
        Weight.MAX,
        Confidence.make(usedValues),
        Arrays.asList(usedValues));
  }
}
