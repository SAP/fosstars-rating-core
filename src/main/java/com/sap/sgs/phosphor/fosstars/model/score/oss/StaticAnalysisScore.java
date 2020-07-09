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
 * <p>The score shows how an open-source project uses static analysis for security testing.</p>
 * <p>It's based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link LgtmScore}</li>
 *   <li>{@link FindSecBugsScore}</li>
 * </ul>
 * <p>The above sub-scores may not apply to all projects. The score considers only the sub-scores
 * that is applicable to a particular project.</p>
 */
public class StaticAnalysisScore extends AbstractScore {

  /**
   * A score that shows how a project uses LGTM.
   */
  private final LgtmScore lgtmScore;

  /**
   * A score that shows how a project uses FindSecBugs.
   */
  private final FindSecBugsScore findSecBugsScore;

  /**
   * Initializes a new score.
   */
  public StaticAnalysisScore() {
    super("How a project uses static analysis for security testing");
    this.lgtmScore = new LgtmScore();
    this.findSecBugsScore = new FindSecBugsScore();
  }

  /**
   * The score doesn't use any feature directly
   * so that this method returns an empty set.
   *
   * @return An empty set of features.
   */
  @Override
  public Set<Feature> features() {
    return Collections.emptySet();
  }

  @Override
  public Set<Score> subScores() {
    return setOf(lgtmScore, findSecBugsScore);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Objects.requireNonNull(values, "Oh no! Values is null!");

    ScoreValue lgtmScoreValue = calculateIfNecessary(lgtmScore, values);
    ScoreValue findSecBugsScoreValue = calculateIfNecessary(findSecBugsScore, values);

    ScoreValue scoreValue = scoreValue(MIN, lgtmScoreValue, findSecBugsScoreValue);

    if (allUnknown(lgtmScoreValue, findSecBugsScoreValue)) {
      return scoreValue.makeUnknown();
    }

    if (allNotApplicable(lgtmScoreValue, findSecBugsScoreValue)) {
      return scoreValue.makeNotApplicable();
    }

    scoreValue.increase(lgtmScoreValue.orElse(MIN));
    scoreValue.increase(findSecBugsScoreValue.orElse(MIN));
    scoreValue.confidence(Confidence.make(lgtmScoreValue, findSecBugsScoreValue));

    return scoreValue;
  }

  /**
   * This class implements a verification procedure for {@link StaticAnalysisScore}.
   * The class loads test vectors,
   * and provides methods to verify a {@link StaticAnalysisScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "StaticAnalysisScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification}
     * for a {@link StaticAnalysisScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(StaticAnalysisScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link StaticAnalysisScore}.
     */
    static Verification createFor(StaticAnalysisScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
