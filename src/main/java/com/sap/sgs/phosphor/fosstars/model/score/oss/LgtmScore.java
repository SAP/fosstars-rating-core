package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORSE_LGTM_GRADE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import java.util.EnumMap;

/**
 * The score shows if and how a project addresses issues reported by LGTM.
 * The score is based on the following features
 * <ul>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#USES_LGTM}</li>
 *   <li>{@link com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#WORSE_LGTM_GRADE}</li>
 * </ul>
 */
public class LgtmScore extends FeatureBasedScore {

  private static final double LGTM_USAGE_POINTS = 2.0;

  private static final EnumMap<LgtmGrade, Double> GRADE_TO_POINTS = new EnumMap<>(LgtmGrade.class);

  static {
    GRADE_TO_POINTS.put(LgtmGrade.A_PLUS, 8.0);
    GRADE_TO_POINTS.put(LgtmGrade.A, 8.0);
    GRADE_TO_POINTS.put(LgtmGrade.B, 6.0);
    GRADE_TO_POINTS.put(LgtmGrade.C, 4.0);
    GRADE_TO_POINTS.put(LgtmGrade.D, 2.0);
    GRADE_TO_POINTS.put(LgtmGrade.E, 8.0);
  }

  /**
   * Initializes a new {@link LgtmScore}.
   */
  LgtmScore() {
    super("If and how a project addresses issues reported by LGTM.",
        USES_LGTM, WORSE_LGTM_GRADE);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> usesLgtm = findValue(values, USES_LGTM,
        "Hey! You have to tell me if the project uses LGTM!");
    Value<LgtmGrade> worseLgtmGrade = findValue(values, WORSE_LGTM_GRADE,
        "Hey! You have to tell me the worse LGTM grade for the project!");

    ScoreValue scoreValue = new ScoreValue(this);

    usesLgtm.processIfKnown(uses -> {
      if (uses) {
        scoreValue.increase(LGTM_USAGE_POINTS);
      }
    });

    worseLgtmGrade.processIfKnown(grade -> scoreValue.increase(GRADE_TO_POINTS.get(grade)));

    scoreValue.confidence(Confidence.make(usesLgtm, worseLgtmGrade));

    return scoreValue;
  }
}
