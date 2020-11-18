package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.sgs.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.sgs.phosphor.fosstars.model.weight.ScoreWeights;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>The score shows how an open-source project uses static analysis for security testing.</p>
 * <p>It's based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link CodeqlScore}</li>
 *   <li>{@link LgtmScore}</li>
 *   <li>{@link FindSecBugsScore}</li>
 * </ul>
 * <p>The above sub-scores may not apply to all projects. The score considers only the sub-scores
 * that is applicable to a particular project.</p>
 */
public class StaticAnalysisScore extends WeightedCompositeScore {

  /**
   * Initializes weights for sub-scores.
   *
   * @return The weights of sub-scores.
   */
  private static ScoreWeights initWeights() {
    return ScoreWeights.empty()
        .set(CodeqlScore.class, new ImmutableWeight(1.0))
        .set(LgtmScore.class, new ImmutableWeight(1.0))
        .set(FindSecBugsScore.class, new ImmutableWeight(0.5));
  }

  /**
   * Initializes a new score.
   */
  public StaticAnalysisScore() {
    super("How a project uses static analysis for security testing",
        setOf(new CodeqlScore(), new LgtmScore(), new FindSecBugsScore()),
        initWeights());
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
