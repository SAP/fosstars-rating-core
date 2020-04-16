package com.sap.sgs.phosphor.fosstars.model.score;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Tunable;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.Visitor;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.sgs.phosphor.fosstars.model.weight.MutableWeight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A base class for scores which are based on a weighted average of other scores (sub-scores).
 */
public class WeightedCompositeScore extends AbstractScore implements Tunable {

  /**
   * The default weight for sub-scores.
   */
  private static final double DEFAULT_WEIGHT = 1.0;

  /**
   * A set of weighted sub-scores.
   */
  private final Set<WeightedScore> weightedScores;

  /**
   * Makes a score from a list of sub-scores.
   */
  public WeightedCompositeScore(String name, Score... scores) {
    this(name, prepare(scores));
  }

  /**
   * This is a constructor which is used by Jackson during deserialization.
   * TODO: Check if Jackson changes access for this constructor, and don't revert it back.
   *       If so, is it a security issue?
   */
  @JsonCreator
  WeightedCompositeScore(
      @JsonProperty("name") String name,
      @JsonProperty("weightedScores") Set<WeightedScore> weightedScores) {

    super(name);
    this.weightedScores = check(weightedScores);
  }

  /**
   * This is a getter for Jackson to get the set of weighted scores.
   * The method returns a copy of the set to prevent modification of the set by the caller.
   * TODO: Figure out if Jackson changes access level for this method, and don't revert it back.
   *       If so, is it a security issue?
   */
  @JsonProperty("weightedScores")
  private Set<WeightedScore> weightedScores() {
    return new HashSet<>(weightedScores);
  }

  @Override
  public List<Weight> parameters() {
    // TODO: should it return all parameters which are used by underlying sub-scores?
    List<Weight> weights = new ArrayList<>();
    for (WeightedScore weightedScore : weightedScores) {
      weights.add(weightedScore.weight);
    }
    return weights;
  }

  @Override
  @JsonIgnore
  public boolean isImmutable() {
    for (WeightedScore weightedScore : weightedScores) {
      if (!weightedScore.weight.isImmutable()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void makeImmutable() {
    Set<WeightedScore> immutableWeightedScores = new HashSet<>();
    for (WeightedScore weightedScore : weightedScores) {
      if (weightedScore.weight.isImmutable()) {
        immutableWeightedScores.add(weightedScore);
      } else {
        immutableWeightedScores.add(
            new WeightedScore(
                weightedScore.score,
                new ImmutableWeight(weightedScore.weight.value())));
      }
    }

    weightedScores.clear();
    weightedScores.addAll(immutableWeightedScores);
  }

  /**
   * Calculate an overall score value as a weighted average of the underlying sub-scores.
   *
   * @param values A number of values.
   * @return An overall score.
   */
  @Override
  public final ScoreValue calculate(Value... values) {
    ValueHashSet valueSet = new ValueHashSet(values);

    double weightSum = 0.0;
    double scoreSum = 0.0;
    double confidenceSum = 0.0;
    ScoreValue scoreValue = new ScoreValue(this);
    boolean allNotApplicable = true;
    for (WeightedScore weightedScore : weightedScores) {
      double weight = weightedScore.weight.value();
      ScoreValue subScoreValue = calculateIfNecessary(weightedScore.score, valueSet);
      if (subScoreValue.isNotApplicable()) {
        continue;
      }
      allNotApplicable = false;
      subScoreValue.weight(weightedScore.weight.value());
      scoreValue.usedValues(subScoreValue);
      scoreSum += weight * subScoreValue.get();
      confidenceSum += weight * subScoreValue.confidence();
      weightSum += weight;
    }

    if (allNotApplicable) {
      return scoreValue.makeNotApplicable();
    }

    if (weightSum == 0) {
      throw new IllegalArgumentException("Oh no! Looks like all weights are zero!");
    }

    scoreValue.set(Score.adjust(scoreSum / weightSum));
    scoreValue.confidence(Confidence.adjust(confidenceSum / weightSum));

    return scoreValue;
  }

  /**
   * The score doesn't use any feature directory
   * so that this method returns an empty set.
   *
   * @return An empty set of features.
   */
  @Override
  public final Set<Feature> features() {
    return Collections.emptySet();
  }

  @Override
  public final Set<Score> subScores() {
    Set<Score> scores = new HashSet<>();
    for (WeightedScore weightedScore : weightedScores) {
      scores.add(weightedScore.score);
    }
    return scores;
  }

  /**
   * Looks for a sub-score by its type.
   *
   * @param clazz The class instance of the sub-score.
   * @param <T> The type of the sub-score
   * @return An {@link Optional} instance with the sub-score.
   */
  public <T> Optional<T> subScore(Class<T> clazz) {
    Objects.requireNonNull(clazz, "Hey! Class can't be null!");
    if (!Score.class.isAssignableFrom(clazz)) {
      throw new IllegalArgumentException(String.format(
          "I expected an instance of Score but you gave me %s!", clazz.getCanonicalName()));
    }
    for (Score subScore : subScores()) {
      if (subScore.getClass() == clazz) {
        return Optional.of(clazz.cast(subScore));
      }
    }
    return Optional.empty();
  }

  @Override
  public void accept(Visitor visitor) {
    super.accept(visitor);
    for (WeightedScore weightedScore : weightedScores) {
      weightedScore.score.accept(visitor);
      weightedScore.weight.accept(visitor);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof WeightedCompositeScore == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    WeightedCompositeScore that = (WeightedCompositeScore) o;
    return Objects.equals(weightedScores, that.weightedScores);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), weightedScores);
  }

  /**
   * Converts a number of scores to a set of weighted scores with the default weight.
   */
  private static Set<WeightedScore> prepare(Score... scores) {
    Objects.requireNonNull(scores, "Hey! Scores can't be null!");
    if (scores.length == 0) {
      throw new IllegalArgumentException("Hey! Scores can't be empty!");
    }
    Set<WeightedScore> weightedScores = new HashSet<>();
    for (Score score : scores) {
      boolean success = weightedScores.add(
          new WeightedScore(score, new MutableWeight(DEFAULT_WEIGHT)));
      if (!success) {
        throw new IllegalArgumentException(String.format(
            "Score already exists in the rating: %s", score.name()));
      }
    }

    // let's be paranoids and check the weights even here
    return check(weightedScores);
  }

  /**
   * Checks if the specified scores have correct weights.
   *
   * @return The same set of weighted scores if the weights are correct.
   * @throws IllegalArgumentException if at least one of the weights are not correct.
   */
  private static Set<WeightedScore> check(Set<WeightedScore> weightedScores) {
    Objects.requireNonNull(weightedScores, "Hey! Weighted score can't be null!");
    if (weightedScores.isEmpty()) {
      throw new IllegalArgumentException("Hey! Weighted scores can't be empty!");
    }

    for (WeightedScore weightedScore : weightedScores) {
      Objects.requireNonNull(weightedScore, "Weighted score can't be null!");
      Objects.requireNonNull(weightedScore.weight, "Weight can't be null!");
      Objects.requireNonNull(weightedScore.score, "Score can't be null!");

      double weight = weightedScore.weight.value();
      if (!Weight.INTERVAL.contains(weight)) {
        throw new IllegalArgumentException(
            String.format("Weight %s doesn't belong to %s",
                String.valueOf(weight), Weight.INTERVAL));
      }
    }

    // return a new set to prevent modification of the set by the caller
    return new HashSet<>(weightedScores);
  }

  /**
   * A score with a weight. Or, just weighted score. The class is immutable.
   */
  static class WeightedScore {

    // those fields need to be public for successful serialization with Jackson
    // otherwise, it needs getters and setters
    public final Score score;
    public final Weight weight;

    @JsonCreator
    WeightedScore(@JsonProperty("score") Score score, @JsonProperty("weight") Weight weight) {
      this.score = score;
      this.weight = weight;
    }

    /**
     * The equals() method considers only the score.
     */
    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null) {
        return false;
      }
      if (o.getClass() != getClass()) {
        return false;
      }
      WeightedScore that = (WeightedScore) o;
      return Objects.equals(score, that.score);
    }

    /**
     * The hashCode() method considers only the score.
     */
    @Override
    public int hashCode() {
      return Objects.hashCode(score);
    }
  }

}
