package com.sap.oss.phosphor.fosstars.model.score;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Visitor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A base class for scores which are based only on features. The class is immutable.
 */
public abstract class FeatureBasedScore extends AbstractScore {

  /**
   * A set of features which are used in the score.
   */
  private final Set<Feature<?>> features;

  /**
   * Initializes a feature-based score with an empty description.
   *
   * @param name A name of the score.
   * @param features A number of features which are used in the score.
   */
  public FeatureBasedScore(String name, Feature<?>... features) {
    this(name, EMPTY_DESCRIPTION, setOf(features));
  }

  /**
   * Initializes a feature-based score.
   *
   * @param name A name of the score.
   * @param description A description of the score (may be empty).
   * @param features A number of features which are used in the score.
   */
  public FeatureBasedScore(String name, String description, Feature<?>... features) {
    this(name, description, setOf(features));
  }

  /**
   * Initializes a feature-based score.
   *
   * @param name A name of the score.
   * @param description A description of the score (may be empty).
   * @param features A set of features which are used in the score.
   */
  public FeatureBasedScore(String name, String description, Set<Feature<?>> features) {
    super(name, description);
    for (Feature<?> feature : features) {
      if (feature instanceof Score) {
        throw new IllegalArgumentException("Hey! I cannot work with scores!");
      }
    }
    this.features = new HashSet<>(features);
  }

  @Override
  @JsonIgnore
  public final Set<Feature<?>> features() {
    return new HashSet<>(features);
  }

  @Override
  @JsonIgnore
  public final Set<Score> subScores() {
    return Collections.emptySet();
  }

  @Override
  public void accept(Visitor visitor) {
    super.accept(visitor);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof FeatureBasedScore == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    FeatureBasedScore that = (FeatureBasedScore) o;
    return Objects.equals(features, that.features);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), features);
  }
}
