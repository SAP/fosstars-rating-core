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
import java.util.Collections;
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

  /**
   * Initializes a rating.
   *
   * @param name A name of the rating.
   * @param score A score which the rating is based on.
   * @param version A version of the rating.
   */
  public AbstractRating(String name, Score score, Version version) {
    Objects.requireNonNull(name, "Name can't be null!");
    Objects.requireNonNull(score, "Score can't be null!");
    Objects.requireNonNull(version, "Version can't be null!");
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
  public Set<Feature> allFeatures() {
    Set<Feature> allFeatures = new HashSet<>();
    fillOutFeatures(score, allFeatures);
    return Collections.unmodifiableSet(allFeatures);
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
    return Objects.equals(score, that.score) && version == that.version;
  }

  @Override
  public int hashCode() {
    return Objects.hash(score, version);
  }

  private static void fillOutFeatures(Score score, Set<Feature> allFeatures) {
    allFeatures.addAll(score.features());
    for (Score subScore : score.subScores()) {
      fillOutFeatures(subScore, allFeatures);
    }
  }

}
