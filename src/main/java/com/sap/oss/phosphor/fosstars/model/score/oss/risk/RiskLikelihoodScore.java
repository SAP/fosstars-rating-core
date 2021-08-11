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
 * <p>This scoring function calculates likelihood score
 * that is used in calculating security risk introduced by an open source project.
 * This is based on the following:</p>
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
  private final RiskLikelihoodCoefficient coefficientScore;

  /**
   * A scoring function that aggregates likelihood factors.
   */
  private final RiskLikelihoodFactors factorsScore;

  /**
   * Creates a new scoring function with default parameters.
   */
  RiskLikelihoodScore() {
    this(new RiskLikelihoodCoefficient(), new RiskLikelihoodFactors());
  }

  /**
   * Creates a new scoring function.
   *
   * @param coefficientScore A scoring function that calculates a likelihood coefficient.
   * @param factorsScore A scoring function that aggregates likelihood factors.
   */
  public RiskLikelihoodScore(
      RiskLikelihoodCoefficient coefficientScore, RiskLikelihoodFactors factorsScore) {

    super("Likelihood score for security risk of open source project");

    requireNonNull(coefficientScore, "Oops! Coefficient score can't be null!");
    requireNonNull(factorsScore, "Oops! Likelihood factors score can't be null!");

    this.coefficientScore = coefficientScore;
    this.factorsScore = factorsScore;
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
    requireNonNull(values, "Oops! Values can't be null!");

    ScoreValue coefficient = calculateIfNecessary(coefficientScore, values);
    ScoreValue factors = calculateIfNecessary(factorsScore, values);

    if (coefficient.isUnknown() || factors.isUnknown()) {
      return scoreValue(MIN, coefficient, factors).makeUnknown();
    }

    return scoreValue(coefficient.get() / MAX * factors.get(), coefficient, factors);
  }
}
