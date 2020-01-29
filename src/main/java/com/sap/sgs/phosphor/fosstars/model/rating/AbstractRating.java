package com.sap.sgs.phosphor.fosstars.model.rating;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.RatingValue;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.Version;
import com.sap.sgs.phosphor.fosstars.model.Visitor;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A base class for ratings.
 */
public abstract class AbstractRating implements Rating {

  /**
   * Rating name.
   */
  private final String name;

  /**
   * A score which is used in the raging.
   */
  private final Score score;

  /**
   * The rating's version.
   */
  private final Version version;

  public AbstractRating(String name, Score score, Version version) {
    this.name = name;
    this.score = score;
    this.version = version;
  }

  @Override
  public final RatingValue calculate(Set<Value> values) {
    return calculate(score.calculate(values));
  }

  @Override
  public RatingValue calculate(Value... values) {
    return calculate(score.calculate(values));
  }

  @Override
  public RatingValue calculate(ValueSet values) {
    return calculate(score.calculate(values));
  }

  private RatingValue calculate(ScoreValue scoreValue) {
    return new RatingValue(scoreValue, label(scoreValue.score()));
  }

  protected abstract Label label(double score);

  @Override
  @JsonGetter("score")
  public Score score() {
    return score;
  }

  @Override
  @JsonGetter("version")
  public final Version version() {
    return version;
  }

  @Override
  @JsonGetter("name")
  public final String name() {
    return name;
  }

  @Override
  public Feature[] allFeatures() {
    Set<Feature> allFeatures = new HashSet<>();
    findFeatures(score, allFeatures);
    return allFeatures.toArray(new Feature[0]);
  }

  @Override
  public void accept(Visitor visitor) {
    score.accept(visitor);
    visitor.visit(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof AbstractRating == false) {
      return false;
    }
    AbstractRating that = (AbstractRating) o;
    return Objects.equals(score, that.score) &&
        version == that.version;
  }

  @Override
  public int hashCode() {
    return Objects.hash(score, version);
  }

  private static void findFeatures(Score score, Set<Feature> allFeatures) {
    allFeatures.addAll(score.features());
    for (Score subScore : score.subScores()) {
      findFeatures(subScore, allFeatures);
    }
  }

}
