package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.DATA_CONFIDENTIALITY;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;
import static java.lang.String.format;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.DataConfidentialityType;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * This scoring function outputs am impact factor
 * for security risk introduced by an open source project.
 * The factor is based on confidentiality of data that the project likely processes.
 */
public class DataConfidentialityRiskImpactFactor extends FeatureBasedScore {

  /**
   * Create a new scoring function.
   */
  public DataConfidentialityRiskImpactFactor() {
    super("Impact factor of confidentiality of data that an open source project likely processes",
        DATA_CONFIDENTIALITY);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<DataConfidentialityType> data = findValue(values, DATA_CONFIDENTIALITY,
        "Hey! You have to tell me what kind of data the project processes!");

    ScoreValue scoreValue = scoreValue(MIN, data);

    if (data.isUnknown()) {
      return scoreValue.makeUnknown();
    }

    switch (data.get()) {
      case CONFIDENTIAL:
      case PERSONAL:
        return scoreValue.set(MAX);
      case INTERNAL:
        return scoreValue.set(5.0);
      case TEST:
      case PUBLIC:
        return scoreValue.set(MIN);
      default:
        throw new IllegalArgumentException(format("Oops! Unexpected data category: %s", data));
    }
  }
}
