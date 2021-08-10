package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static java.util.Collections.emptySet;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.AbstractScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Objects;
import java.util.Set;

/**
 * <p>This scoring function calculates likelihood score that is used in the security risk
 * for open source project. This is based on the following:</p>
 * <ul>
 *   <li>{@link RiskLikelihoodCoefficient}</li>
 *   <li>{@link RiskLikelihoodFactors}</li>
 * </ul>
 * <pre>likelihood score = coefficient * likelihood factors</pre>
 */
public class RiskLikelihoodScore extends AbstractScore {

  /**
   * A scoring function that calculates a likelihood coefficient.
   */
  private final RiskLikelihoodCoefficient coefficientScore = new RiskLikelihoodCoefficient();

  /**
   * A scoring function that aggregates likelihood factors.
   */
  private final RiskLikelihoodFactors factorsScore = new RiskLikelihoodFactors();

  /**
   * Creates a new scoring function.
   */
  public RiskLikelihoodScore() {
    super("Likelihood score for security risk of open source project");
  }

  @Override
  public Set<Feature<?>> features() {
    return emptySet();
  }

  @Override
  public Set<Score> subScores() {
    return setOf(coefficientScore, factorsScore);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Objects.requireNonNull(values, "Oops! Values can't be null!");

    ScoreValue coefficient = calculateIfNecessary(coefficientScore, values);
    ScoreValue factors = calculateIfNecessary(factorsScore, values);

    if (coefficient.isUnknown() || factors.isUnknown()) {
      return scoreValue(MIN, coefficient, factors).makeUnknown();
    }

    return scoreValue(coefficient.get() / MAX * factors.get(), coefficient, factors);
  }
}
