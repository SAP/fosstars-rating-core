package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.IS_ADOPTED;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * This scoring function outputs a likelihood factor
 * for security risk introduced by an open source project.
 * The factor is based on whether the project is adopted by a team.
 */
public class AdoptedRiskLikelihoodFactor extends FeatureBasedScore {

  /**
   * Create a new scoring function.
   */
  public AdoptedRiskLikelihoodFactor() {
    super("Likelihood factor of adoption of an open source project by a team", IS_ADOPTED);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> adopted = findValue(values, IS_ADOPTED,
        "Hey! You have to tell whether the project has been adopted or not!");

    ScoreValue scoreValue = scoreValue(MIN, adopted);

    if (adopted.isUnknown()) {
      return scoreValue.makeUnknown();
    }

    return scoreValue.set(adopted.get() ? MAX : MIN);
  }
}
