package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.AVAILABILITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.CONFIDENTIALITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.INTEGRITY_IMPACT;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.oss.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.oss.phosphor.fosstars.model.weight.ScoreWeights;

/**
 * <p>This scoring function aggregates impact factors
 * that are used in calculating security risk introduced by an open source project.
 * This is based on the following factors:</p>
 * <ul>
 *   <li>{@link DataConfidentialityRiskImpactFactor}</li>
 *   <li>{@link ConfidentialityRiskImpactFactor}</li>
 *   <li>{@link IntegrityRiskImpactFactor}</li>
 *   <li>{@link AvailabilityRiskImpactFactor}</li>
 * </ul>
 */
public class RiskImpactScore extends WeightedCompositeScore {

  /**
   * This scoring function outputs am impact factor for security risk of open source project.
   * The factor is based on potential confidentiality impact in case of a security problem
   * in an open source project.
   */
  public static class ConfidentialityRiskImpactFactor extends ImpactScore {

    /**
     * Create a new scoring function.
     */
    public ConfidentialityRiskImpactFactor() {
      super("Confidentiality impact factor", CONFIDENTIALITY_IMPACT);
    }
  }

  /**
   * This scoring function outputs am impact factor for security risk of open source project.
   * The factor is based on potential integrity impact in case of a security problem
   * in an open source project.
   */
  public static class IntegrityRiskImpactFactor extends ImpactScore {

    /**
     * Create a new scoring function.
     */
    public IntegrityRiskImpactFactor() {
      super("Integrity impact factor", INTEGRITY_IMPACT);
    }
  }

  /**
   * This scoring function outputs am impact factor for security risk of open source project.
   * The factor is based on potential availability impact in case of a security problem
   * in an open source project.
   */
  public static class AvailabilityRiskImpactFactor extends ImpactScore {

    /**
     * Create a new scoring function.
     */
    public AvailabilityRiskImpactFactor() {
      super("Availability impact factor", AVAILABILITY_IMPACT);
    }
  }

  /**
   * Initializes weights for sub-scores.
   *
   * @return The weights of sub-scores.
   */
  private static ScoreWeights initWeights() {
    return ScoreWeights.empty()
        .set(DataConfidentialityRiskImpactFactor.class, new ImmutableWeight(1.0))
        .set(ConfidentialityRiskImpactFactor.class, new ImmutableWeight(0.8))
        .set(IntegrityRiskImpactFactor.class, new ImmutableWeight(0.8))
        .set(AvailabilityRiskImpactFactor.class, new ImmutableWeight(0.8));
  }

  /**
   * Initializes a new score.
   */
  public RiskImpactScore() {
    super("Aggregated impact factors for security risk of open source project",
        setOf(
            new DataConfidentialityRiskImpactFactor(),
            new ConfidentialityRiskImpactFactor(),
            new IntegrityRiskImpactFactor(),
            new AvailabilityRiskImpactFactor()),
        initWeights());
  }
}
