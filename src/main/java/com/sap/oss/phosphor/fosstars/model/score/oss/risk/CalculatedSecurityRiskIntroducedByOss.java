package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.AbstractScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Set;

/**
 * This scoring function calculates a security risk introduced by an open source project.
 * It is based on the following sub-scores:
 * <ul>
 *   <li>{@link RiskLikelihoodScore}</li>
 *   <li>{@link RiskImpactScore}</li>
 * </ul>
 * <pre>security risk = likelihood * impact</pre>
 */
public class CalculatedSecurityRiskIntroducedByOss extends AbstractScore {

  /**
   * A scoring function that calculates likelihood.
   */
  private final RiskLikelihoodScore likelihoodScore;

  /**
   * A scoring function that calculates impact.
   */
  private final RiskImpactScore impactScore;

  /**
   * Creates a new scoring function with default parameters.
   */
  public CalculatedSecurityRiskIntroducedByOss() {
    this(new RiskLikelihoodScore(), new RiskImpactScore());
  }

  /**
   * Creates a new scoring function.
   *
   * @param likelihoodScore A scoring function that calculates likelihood.
   * @param impactScore A scoring function that calculates impact.
   */
  public CalculatedSecurityRiskIntroducedByOss(
      RiskLikelihoodScore likelihoodScore, RiskImpactScore impactScore) {

    super("Security risk introduced by an open source project");

    requireNonNull(likelihoodScore, "Oops! Likelihood score can't be null!");
    requireNonNull(impactScore, "Oops! Impact score can't be null!");

    this.likelihoodScore = likelihoodScore;
    this.impactScore = impactScore;
  }

  @Override
  public Set<Feature<?>> features() {
    return emptySet();
  }

  @Override
  public Set<Score> subScores() {
    return setOf(likelihoodScore, impactScore);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    ScoreValue likelihood = calculateIfNecessary(likelihoodScore, values);
    ScoreValue impact = calculateIfNecessary(impactScore, values);

    ScoreValue risk = scoreValue(MIN, likelihood, impact);

    if (likelihood.isUnknown() || impact.isUnknown()) {
      return risk.makeUnknown();
    }

    return risk.set(likelihood.get() * impact.get() / MAX);
  }
}
