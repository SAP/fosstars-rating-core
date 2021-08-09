package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.oss.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.oss.phosphor.fosstars.model.weight.ScoreWeights;

/**
 * <p>This scoring function calculates a likelihood coefficient that is used in the security risk
 * for open source project. The coefficient is based on the following:</p>
 * <ul>
 *   <li>{@link OssSecurityScore}</li>
 *   <li>{@link AdoptedRiskLikelihoodFactor}</li>
 * </ul>
 */
public class RiskLikelihoodCoefficient extends WeightedCompositeScore {

  /**
   * Initializes weights for sub-scores.
   *
   * @return The weights of sub-scores.
   */
  private static ScoreWeights initWeights() {
    return ScoreWeights.empty()
        .set(OssSecurityScore.class, new ImmutableWeight(0.8))
        .set(AdoptedRiskLikelihoodFactor.class, new ImmutableWeight(0.2));
  }

  /**
   * Initializes a new score.
   */
  public RiskLikelihoodCoefficient() {
    super("Likelihood coefficient for security risk of open source project",
        setOf(new OssSecurityScore(), new AdoptedRiskLikelihoodFactor()),
        initWeights());
  }
}
