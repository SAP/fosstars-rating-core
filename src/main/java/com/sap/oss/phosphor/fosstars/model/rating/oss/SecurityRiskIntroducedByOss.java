package com.sap.oss.phosphor.fosstars.model.rating.oss;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.rating.AbstractRating;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.CalculatedSecurityRiskIntroducedByOss;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.RiskImpactScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.risk.RiskLikelihoodScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Optional;

/**
 * This rating calculates a security risk introduced by an open source project.
 * The rating is based on {@link CalculatedSecurityRiskIntroducedByOss}
 * score and {@link #RISK_MATRIX};
 */
public class SecurityRiskIntroducedByOss extends AbstractRating  {

  /**
   * A set of labels for the rating.
   */
  public enum OssSecurityRiskLabel implements Label {

    NOTE, LOW, MEDIUM, HIGH, CRITICAL, UNCLEAR
  }

  /**
   * A matrix for determining a risk label based on impact (rows) and likelihood (columns).
   */
  private static final OssSecurityRiskLabel[][] RISK_MATRIX = new OssSecurityRiskLabel[][] {
      { OssSecurityRiskLabel.NOTE,    OssSecurityRiskLabel.LOW,     OssSecurityRiskLabel.MEDIUM },
      { OssSecurityRiskLabel.LOW,     OssSecurityRiskLabel.MEDIUM,  OssSecurityRiskLabel.HIGH },
      { OssSecurityRiskLabel.MEDIUM,  OssSecurityRiskLabel.HIGH,    OssSecurityRiskLabel.CRITICAL }
  };

  /**
   * A threshold for the unclear label (confidence).
   */
  private static final double UNCLEAR_THRESHOLD = 0.8;

  /**
   * A threshold for medium likelihood or impact.
   */
  private static final double MEDIUM_THRESHOLD = 5.0;

  /**
   * A threshold for high likelihood or impact.
   */
  private static final double HIGH_THRESHOLD = 8.0;

  /**
   * Creates a new rating procedure with default parameters.
   */
  SecurityRiskIntroducedByOss() {
    super("Security risk introduced by an open source project",
        new CalculatedSecurityRiskIntroducedByOss());
  }

  /**
   * Creates a new rating procedure.
   *
   * @param score A {@link CalculatedSecurityRiskIntroducedByOss}.
   */
  public SecurityRiskIntroducedByOss(CalculatedSecurityRiskIntroducedByOss score) {
    super("Security risk introduced by an open source project", score);
  }

  /**
   * Get an underlying score.
   *
   * @return An underlying {@link CalculatedSecurityRiskIntroducedByOss}.
   */
  public CalculatedSecurityRiskIntroducedByOss score() {
    return (CalculatedSecurityRiskIntroducedByOss) super.score();
  }

  @Override
  protected Label label(ScoreValue scoreValue) {
    requireNonNull(scoreValue, "Oh no! Score value is null!");

    if (!scoreValue.score().equals(score())) {
      throw new IllegalArgumentException(
          format("Oh no! Invalid score value: %s", scoreValue.score().getClass()));
    }

    if (scoreValue.isUnknown()) {
      return OssSecurityRiskLabel.UNCLEAR;
    }

    Optional<ScoreValue> likelihood = scoreValue.findUsedSubScoreValue(RiskLikelihoodScore.class);
    if (!likelihood.isPresent()) {
      throw new IllegalArgumentException("Oh no! Could not find likelihood!");
    }

    Optional<ScoreValue> impact = scoreValue.findUsedSubScoreValue(RiskImpactScore.class);
    if (!impact.isPresent()) {
      throw new IllegalArgumentException("Oh no! Could not find impact!");
    }

    if (likelihood.get().isUnknown() || impact.get().isUnknown()) {
      return OssSecurityRiskLabel.UNCLEAR;
    }

    if (scoreValue.confidence() < UNCLEAR_THRESHOLD) {
      return OssSecurityRiskLabel.UNCLEAR;
    }

    return RISK_MATRIX[indexFor(impact.get())][indexFor(likelihood.get())];
  }

  /**
   * Get an index in the risk matrix for a score value that contains likelihood or impact.
   *
   * @param value The score value.
   * @return An index in the risk matrix.
   */
  private static int indexFor(ScoreValue value) {
    if (value.get() < MEDIUM_THRESHOLD) {
      return 0;
    }

    if (value.get() < HIGH_THRESHOLD) {
      return 1;
    }

    return 2;
  }
}
