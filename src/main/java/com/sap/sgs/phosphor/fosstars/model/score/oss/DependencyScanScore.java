package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.AbstractScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * <p>The scores assesses how well an open-source project scans dependencies
 * for known vulnerabilities. It is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link DependabotScore}</li>
 *   <li>{@link OwaspDependencyScanScore}</li>
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
   * Initializes a new score.
   */
  public DependencyScanScore() {
    super("How a project scans its dependencies for vulnerabilities");
    this.dependabotScore = new DependabotScore();
    this.owaspDependencyCheckScore = new OwaspDependencyScanScore();
  }

  @Override
  public Set<Feature> features() {
    return Collections.emptySet();
  }

  @Override
  public Set<Score> subScores() {
    return setOf(dependabotScore, owaspDependencyCheckScore);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Objects.requireNonNull(values, "Oh no! Values is null!");

    ScoreValue dependabotScoreValue = calculateIfNecessary(dependabotScore, values);
    ScoreValue owaspDependencyCheckScoreValue
        = calculateIfNecessary(owaspDependencyCheckScore, values);

    ScoreValue scoreValue = scoreValue(MIN, dependabotScoreValue, owaspDependencyCheckScoreValue);

    if (allUnknown(dependabotScoreValue, owaspDependencyCheckScoreValue)) {
      return scoreValue.makeUnknown();
    }

    if (allNotApplicable(dependabotScoreValue, owaspDependencyCheckScoreValue)) {
      return scoreValue.makeNotApplicable();
    }

    scoreValue.increase(dependabotScoreValue.orElse(MIN));
    scoreValue.increase(owaspDependencyCheckScoreValue.orElse(MIN));
    scoreValue.confidence(Confidence.make(dependabotScoreValue, owaspDependencyCheckScoreValue));

    return scoreValue;
  }

  /**
   * This class implements a verification procedure for {@link DependencyScanScore}.
   * The class loads test vectors, and provides methods to verify a {@link DependencyScanScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "DependencyScanScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link DependencyScanScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(DependencyScanScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link Verification}.
     */
    static Verification createFor(DependencyScanScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
