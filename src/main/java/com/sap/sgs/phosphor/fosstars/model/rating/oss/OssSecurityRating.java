package com.sap.sgs.phosphor.fosstars.model.rating.oss;

import static com.sap.sgs.phosphor.fosstars.model.score.oss.OssScores.SECURITY_SCORE;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Version;
import com.sap.sgs.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.sgs.phosphor.fosstars.model.score.oss.OssSecurityScore;

/**
 * This is a security rating for open-source projects
 * which is based on a security score for open-source project.
 */
public class OssSecurityRating extends AbstractRating {

  public enum SecurityLabel implements Label {
    BAD, OKAY, GOOD
  }

  /**
   * Initializes a security rating based on the security score for open-source projects.
   */
  @JsonCreator
  OssSecurityRating(@JsonProperty("version") Version version) {
    super("Security rating for open-source projects", SECURITY_SCORE, version);
  }

  @Override
  public OssSecurityScore score() {
    return (OssSecurityScore) super.score();
  }

  /**
   * Implements a mapping from a score to a label.
   */
  @Override
  protected SecurityLabel label(double score) {
    Score.check(score);

    if (score < 5.0) {
      return SecurityLabel.BAD;
    }

    if (score < 8.0) {
      return SecurityLabel.OKAY;
    }

    return SecurityLabel.GOOD;
  }

}
