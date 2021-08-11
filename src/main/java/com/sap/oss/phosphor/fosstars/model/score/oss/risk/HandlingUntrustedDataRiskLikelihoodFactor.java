package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static java.lang.String.format;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.Likelihood;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * This scoring function outputs a likelihood factor
 * for security risk introduced by an open source project.
 * The factor is based on likelihood that the project handles data from untrusted sources.
 */
public class HandlingUntrustedDataRiskLikelihoodFactor extends FeatureBasedScore {

  /**
   * Create a new scoring function.
   */
  public HandlingUntrustedDataRiskLikelihoodFactor() {
    super("Likelihood factor of handling untrusted data", HANDLING_UNTRUSTED_DATA_LIKELIHOOD);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Likelihood> likelihood = findValue(values, HANDLING_UNTRUSTED_DATA_LIKELIHOOD,
        "Hey! You have to tell me likelihood that the project handles untrusted data!");

    ScoreValue scoreValue = scoreValue(MIN, likelihood);

    if (likelihood.isUnknown()) {
      return scoreValue.makeUnknown();
    }

    switch (likelihood.get()) {
      case NEGLIGIBLE:
        return scoreValue.set(1.0);
      case LOW:
        return scoreValue.set(2.0);
      case MEDIUM:
        return scoreValue.set(5.0);
      case HIGH:
        return scoreValue.set(MAX);
      default:
        throw new IllegalArgumentException(format("Oops! Unexpected usage: %s", likelihood));
    }
  }
}
