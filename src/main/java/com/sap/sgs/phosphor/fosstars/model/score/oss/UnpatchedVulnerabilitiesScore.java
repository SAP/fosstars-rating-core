package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;
import static com.sap.sgs.phosphor.fosstars.model.value.Vulnerability.Resolution.UNPATCHED;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.sgs.phosphor.fosstars.model.value.Vulnerability;

/**
 * The score analyses unpatched vulnerabilities in an open-source project.
 * For each unpatched vulnerability, the score functions applies a penalty to the overall score.
 * The penalty depends on severity of a particular vulnerability.
 */
public class UnpatchedVulnerabilitiesScore extends FeatureBasedScore {

  /**
   * The default CVSS score for a vulnerability if no score specified.
   */
  private static final double DEFAULT_CVSS = 10.0;

  /**
   * The penalty for vulnerabilities with high severity.
   */
  private static final double HIGH_SEVERITY_PENALTY = 10.0;

  /**
   * The penalty for vulnerabilities with medium severity.
   */
  private static final double MEDIUM_SEVERITY_PENALTY = 2.0;

  /**
   * The penalty for vulnerabilities with low severity.
   */
  private static final double LOW_SEVERITY_PENALTY = 1.0;

  /**
   * Default constructor.
   */
  UnpatchedVulnerabilitiesScore() {
    super("How well vulnerabilities are patched", VULNERABILITIES);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Vulnerabilities> vulnerabilities = findValue(values, VULNERABILITIES,
        "Hey! Give me info about vulnerabilities!");

    if (vulnerabilities.isUnknown()) {
      return new ScoreValue(Score.MIN, Confidence.MIN);
    }

    int highSeverity = 0;
    int mediumSeverity = 0;
    int lowSeverity = 0;

    // TODO: should confidence depend on a number of vulnerabilities with unknown resolution?
    for (Vulnerability entry : vulnerabilities.get().entries()) {
      if (entry.resolution() != UNPATCHED) {
        continue;
      }

      CVSS cvss = entry.cvss();
      double value = cvss.isUnknown() ? DEFAULT_CVSS : cvss.value();

      if (value >= 7.0) {
        highSeverity++;
      } else if (value >= 4) {
        mediumSeverity++;
      } else {
        lowSeverity++;
      }
    }

    double score = Score.MAX;

    score -= HIGH_SEVERITY_PENALTY * highSeverity;
    score -= MEDIUM_SEVERITY_PENALTY * mediumSeverity;
    score -= LOW_SEVERITY_PENALTY * lowSeverity;

    return new ScoreValue(Score.adjust(score), Confidence.MAX);
  }
}
