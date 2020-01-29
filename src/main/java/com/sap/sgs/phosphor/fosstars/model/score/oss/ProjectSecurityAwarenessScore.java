package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;

/**
 * The security awareness score is currently based on the following features:
 * - if a project publishes security advisories
 * - if a project has a security team
 */
public class ProjectSecurityAwarenessScore extends FeatureBasedScore {

  ProjectSecurityAwarenessScore() {
    super("How well open-source community is aware about security",
        HAS_SECURITY_POLICY, HAS_SECURITY_TEAM);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> securityAdvisories = findValue(values, HAS_SECURITY_POLICY,
        "Hey! You have to tell me if the project publishes security advisories!");
    Value<Boolean> securityTeam = findValue(values, HAS_SECURITY_TEAM,
        "Hey! You have to tell me if the project has a security team!");

    double points = 0.0;

    if (!securityAdvisories.isUnknown() && securityAdvisories.get().equals(true)) {
      points += 5.0;
    }
    if (!securityTeam.isUnknown() && securityTeam.get().equals(true)) {
      points += 8.0;
    }

    return new ScoreValue(Score.adjust(points), Confidence.make(securityAdvisories, securityTeam));
  }
}
