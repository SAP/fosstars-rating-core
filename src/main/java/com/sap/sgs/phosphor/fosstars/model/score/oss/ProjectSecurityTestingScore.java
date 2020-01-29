package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS_DONE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviews;

/**
 * The security testing score uses the following features:
 * <ul>
 *  <li>info about security reviews</li>
 *  <li>if an open-source project is scanned for vulnerable dependencies</li>
 * </ul>
 *
 * Security reviews contribute to the score the most.
 * There is plenty room for improvements. The score can take into account a lot of other information.
 */
public class ProjectSecurityTestingScore extends FeatureBasedScore {

  ProjectSecurityTestingScore() {
    super("How well security testing is done for an open-source project",
        SECURITY_REVIEWS_DONE,
        SCANS_FOR_VULNERABLE_DEPENDENCIES);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<SecurityReviews> reviews = findValue(values, SECURITY_REVIEWS_DONE,
        "Hey! Tell me if any security review have been done for the project!");
    Value<Boolean> dependenciesScanned = findValue(values, SCANS_FOR_VULNERABLE_DEPENDENCIES,
        "Hey! Tell me if the project is scanned for vulnerable dependencies");

    double points = 0.0;
    if (!reviews.isUnknown() && reviews.get().done()) {
      points += 7.0;
    }
    if (!dependenciesScanned.isUnknown() && dependenciesScanned.get().equals(true)) {
      points += 3.0;
    }

    return new ScoreValue(Score.adjust(points), Confidence.make(reviews, dependenciesScanned));
  }
}
