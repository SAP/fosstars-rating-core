package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.oss.phosphor.fosstars.model.weight.ScoreWeights;

/**
 * <p>This scoring function calculates a likelihood coefficient
 * that is used in calculating security risk introduced by an open source project.
 * The coefficient is based on the following:</p>
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
   * Creates a new scoring function with default parameters.
   */
  RiskLikelihoodCoefficient() {
    this(new OssSecurityScore(), new AdoptedRiskLikelihoodFactor());
  }

  /**
   * Creates a new scoring function.
   *
   * @param ossSecurityScore An {@link OssSecurityScore}.
   * @param adoptedRiskLikelihoodFactor An {@link AdoptedRiskLikelihoodFactor}.
   */
  public RiskLikelihoodCoefficient(
      OssSecurityScore ossSecurityScore, AdoptedRiskLikelihoodFactor adoptedRiskLikelihoodFactor) {

    super("Likelihood coefficient for security risk of open source project",
        setOf(ossSecurityScore, adoptedRiskLikelihoodFactor),
        initWeights());
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    ScoreValue scoreValue = super.calculate(values);
    if (scoreValue.isUnknown() || scoreValue.isNotApplicable()) {
      return scoreValue;
    }
    return scoreValue.set(MAX - scoreValue.get());
  }
}
