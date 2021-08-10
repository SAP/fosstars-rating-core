package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.oss.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.oss.phosphor.fosstars.model.weight.ScoreWeights;

/**
 * <p>This scoring function aggregates likelihood factors
 * that are used in calculating security risk introduced by an open source project.
 * This is based on the following factors:</p>
 * <ul>
 *   <li>{@link UsageRiskLikelihoodFactor}</li>
 *   <li>{@link FunctionalityRiskLikelihoodFactor}</li>
 *   <li>{@link HandlingUntrustedDataRiskLikelihoodFactor}</li>
 * </ul>
 */
public class RiskLikelihoodFactors extends WeightedCompositeScore {

  /**
   * Initializes weights for sub-scores.
   *
   * @return The weights of sub-scores.
   */
  private static ScoreWeights initWeights() {
    return ScoreWeights.empty()
        .set(UsageRiskLikelihoodFactor.class, new ImmutableWeight(1.0))
        .set(FunctionalityRiskLikelihoodFactor.class, new ImmutableWeight(0.8))
        .set(HandlingUntrustedDataRiskLikelihoodFactor.class, new ImmutableWeight(0.8));
  }

  /**
   * Initializes a new score.
   */
  public RiskLikelihoodFactors() {
    super("Aggregated likelihood factors for security risk of open source project",
        setOf(
            new UsageRiskLikelihoodFactor(),
            new FunctionalityRiskLikelihoodFactor(),
            new HandlingUntrustedDataRiskLikelihoodFactor()),
        initWeights());
  }
}
