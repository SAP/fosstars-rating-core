package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.FUNCTIONALITY;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static java.lang.String.format;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * This scoring function outputs a likelihood factor
 * for security risk introduced by an open source project.
 * The factor is based on functionality that the project offers.
 */
public class FunctionalityRiskLikelihoodFactor extends FeatureBasedScore {

  /**
   * Create a new scoring function.
   */
  public FunctionalityRiskLikelihoodFactor() {
    super("Likelihood factor of functionality of an open source project", FUNCTIONALITY);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Functionality> functionality = findValue(values, FUNCTIONALITY,
        "Hey! You have to tell me what kind of functionality the project offers!");

    ScoreValue scoreValue = scoreValue(MIN, functionality);

    if (functionality.isUnknown()) {
      return scoreValue.makeUnknown();
    }

    switch (functionality.get()) {
      case APPLICATION_FRAMEWORK:
      case SDK:
      case SECURITY:
        return scoreValue.set(MAX);
      case OTHER:
        return scoreValue.set(5.0);
      case NETWORKING:
      case PARSER:
        return scoreValue.set(6.0);
      case LOGGER:
        return scoreValue.set(3.0);
      case ANNOTATIONS:
      case TESTING:
        return scoreValue.set(1.0);
      default:
        throw new IllegalArgumentException(
            format("Oops! Unexpected functionality: %s", functionality));
    }
  }
}
