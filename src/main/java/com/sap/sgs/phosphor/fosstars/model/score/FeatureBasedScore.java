package com.sap.sgs.phosphor.fosstars.model.score;

import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Visitor;
import com.sap.sgs.phosphor.fosstars.model.Weight;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A base class for scores which are based only on features. The class is immutable.
 */
public abstract class FeatureBasedScore extends AbstractScore {

  private final Set<Feature> features;

  public FeatureBasedScore(String name, Feature... features) {
    this(name, setOf("You gave me a duplicate feature!", features));
  }

  public FeatureBasedScore(String name, Set<Feature> features) {
    super(name);
    this.features = Collections.unmodifiableSet(features);
  }

  @Override
  @JsonIgnore
  public final Set<Feature> features() {
    return features;
  }

  @Override
  @JsonIgnore
  public final Set<Score> subScores() {
    return Collections.emptySet();
  }

  @Override
  public Optional<Weight> weightOf(Score score) {
    return Optional.empty();
  }

  @Override
  public void accept(Visitor visitor) {
    super.accept(visitor);
    for (Feature feature : features) {
      feature.accept(visitor);
    }
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
