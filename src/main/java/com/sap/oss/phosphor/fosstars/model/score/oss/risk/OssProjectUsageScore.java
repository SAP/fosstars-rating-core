package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures.HOW_MANY_COMPONENTS_USE_OSS_PROJECT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static java.lang.String.format;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.Quantity;
import com.sap.oss.phosphor.fosstars.model.feature.oss.risk.OssRiskFeatures;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * This scoring function shows how much an open source project is used, for example, in a company.
 * Currently, the function simply translates
 * {@link OssRiskFeatures#HOW_MANY_COMPONENTS_USE_OSS_PROJECT} to a score value.
 */
public class OssProjectUsageScore extends FeatureBasedScore {

  /**
   * Create a new scoring function.
   */
  public OssProjectUsageScore() {
    super("How much an open source project is used", HOW_MANY_COMPONENTS_USE_OSS_PROJECT);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Quantity> usage = findValue(values, HOW_MANY_COMPONENTS_USE_OSS_PROJECT,
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
