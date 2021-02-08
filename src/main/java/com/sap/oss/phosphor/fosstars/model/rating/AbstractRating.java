package com.sap.oss.phosphor.fosstars.model.rating;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.Visitor;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
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
   * Initializes a rating.
   *
   * @param name A name of the rating.
   * @param score A score which the rating is based on.
   */
  public AbstractRating(String name, Score score) {
    Objects.requireNonNull(name, "Name can't be null!");
    Objects.requireNonNull(score, "Score can't be null!");
    this.name = name;
    this.score = score;
  }

  @Override
  public final RatingValue calculate(Set<Value<?>> values) {
    return calculate(score.calculate(values));
  }

  @Override
  public RatingValue calculate(Value<?>... values) {
    return calculate(score.calculate(values));
  }

  @Override
  public RatingValue calculate(ValueSet values) {
    return calculate(score.calculate(values));
  }

  private RatingValue calculate(ScoreValue scoreValue) {
    if (scoreValue.isNotApplicable()) {
      return new RatingValue(scoreValue, Label.NOT_APPLICABLE);
    }
    return new RatingValue(scoreValue, label(scoreValue));
  }

  /**
   * Assign a label for to a score value.
   *
   * @param scoreValue The score value.
   * @return The label.
   */
  protected abstract Label label(ScoreValue scoreValue);

  @Override
  @JsonGetter("score")
  public Score score() {
    return score;
  }

  @Override
  @JsonGetter("name")
  public final String name() {
    return name;
  }

  @Override
  public Set<Feature<?>> allFeatures() {
    return score.allFeatures();
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
    return Objects.equals(score, that.score);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score);
  }

}
