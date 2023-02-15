package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.AbstractScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * <p>The scores assesses how well an open-source project scans dependencies
 * for known vulnerabilities. It is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link DependabotScore}</li>
 *   <li>{@link OwaspDependencyScanScore}</li>
 *   <li>{@link SnykDependencyScanScore}</li>
 * </ul>
 */
public class DependencyScanScore extends AbstractScore {

  /**
   * A score that shows how a project uses Dependabot.
   */
  private final DependabotScore dependabotScore;

  /**
   * A score that shows how a project uses OWASP Dependency Check.
   */
  private final OwaspDependencyScanScore owaspDependencyCheckScore;

  /**
   * A score that shows how a project uses Snyk.
   */
  private final SnykDependencyScanScore snykDependencyScanScore;

  /**
   * Initializes a new score.
   */
  public DependencyScanScore() {
    super("How a project scans its dependencies for vulnerabilities");
    this.snykDependencyScanScore = new SnykDependencyScanScore();
    this.dependabotScore = new DependabotScore();
    this.owaspDependencyCheckScore = new OwaspDependencyScanScore();
  }

  @Override
  public Set<Feature<?>> features() {
    return Collections.emptySet();
  }

  @Override
  public Set<Score> subScores() {
    return setOf(dependabotScore, snykDependencyScanScore, owaspDependencyCheckScore);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Objects.requireNonNull(values, "Oh no! Values is null!");

    ScoreValue snykDependencyScanScoreValue = calculateIfNecessary(snykDependencyScanScore, values);
    ScoreValue dependabotScoreValue = calculateIfNecessary(dependabotScore, values);
    ScoreValue owaspDependencyCheckScoreValue
        = calculateIfNecessary(owaspDependencyCheckScore, values);

    ScoreValue scoreValue = scoreValue(MIN, dependabotScoreValue,
        snykDependencyScanScoreValue, owaspDependencyCheckScoreValue);

    if (allUnknown(dependabotScoreValue, snykDependencyScanScoreValue,
        owaspDependencyCheckScoreValue)) {
      return scoreValue.makeUnknown();
    }

    if (allNotApplicable(dependabotScoreValue, snykDependencyScanScoreValue,
        owaspDependencyCheckScoreValue)) {
      return scoreValue.makeNotApplicable();
    }

    scoreValue.increase(snykDependencyScanScoreValue.orElse(MIN));
    scoreValue.increase(dependabotScoreValue.orElse(MIN));
    scoreValue.increase(owaspDependencyCheckScoreValue.orElse(MIN));
    scoreValue.confidence(Confidence.make(dependabotScoreValue, snykDependencyScanScoreValue,
        owaspDependencyCheckScoreValue));

    return scoreValue;
  }
}
