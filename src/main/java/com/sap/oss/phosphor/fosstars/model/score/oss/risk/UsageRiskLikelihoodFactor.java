package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.PROJECT_USAGE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static java.lang.String.format;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.Quantity;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * This scoring function outputs a likelihood factor
 * for security risk introduced by an open source project.
 * The factor is based on how much the project is used, for example, in a company.
 */
public class UsageRiskLikelihoodFactor extends FeatureBasedScore {

  /**
   * Create a new scoring function.
   */
  public UsageRiskLikelihoodFactor() {
    super("Likelihood factor of usage of an open source project",
        PROJECT_USAGE);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Quantity> usage = findValue(values, PROJECT_USAGE,
        "Hey! You have to tell me how much you use the project!");

    ScoreValue scoreValue = scoreValue(MIN, usage);

    if (usage.isUnknown()) {
      return scoreValue.makeUnknown();
    }

    switch (usage.get()) {
      case FEW:
        return scoreValue.set(1.0);
      case SOME:
        return scoreValue.set(4.0);
      case QUITE_A_LOT:
        return scoreValue.set(7.0);
      case A_LOT:
        return scoreValue.set(MAX);
      default:
        throw new IllegalArgumentException(format("Oops! Unexpected usage: %s", usage));
    }
  }
}
