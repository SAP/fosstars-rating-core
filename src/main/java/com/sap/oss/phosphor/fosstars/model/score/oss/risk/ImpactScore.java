package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.Impact;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Objects;

/**
 * This scoring function outputs an impact factor
 * for security risk introduced by an open source project.
 * The factor is based on a specified security impact,
 * for example, confidentiality, integrity or availability impact.
 */
public class ImpactScore extends FeatureBasedScore {

  /**
   * A feature that describes impact.
   */
  private final Feature<Impact> feature;

  /**
   * Creates a new scoring function based on a feature that describes impact.
   *
   * @param name A name of the score.
   * @param feature The feature.
   */
  public ImpactScore(
      @JsonProperty("name") String name,
      @JsonProperty("feature") Feature<Impact> feature) {

    super(name, feature);
    this.feature = Objects.requireNonNull(feature, "Oops! Feature can't be null!");
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Impact> impact = findValue(values, feature, "Hey! You have to tell me about impact!");

    ScoreValue scoreValue = scoreValue(MIN, impact);

    if (impact.isUnknown()) {
      return scoreValue.makeUnknown();
    }

    switch (impact.get()) {
      case NEGLIGIBLE:
        return scoreValue.set(MIN);
      case LOW:
        return scoreValue.set(2.0);
      case MEDIUM:
        return scoreValue.set(5.0);
      case HIGH:
        return scoreValue.set(MAX);
      default:
        throw new IllegalArgumentException(format("Oops! Unexpected impact: %s", impact));
    }
  }

  /**
   * Returns the underlying feature. This method is necessary for serialization with Jackson.
   *
   * @return The underlying feature.
   */
  @JsonGetter
  private Feature<Impact> feature() {
    return feature;
  }
}
