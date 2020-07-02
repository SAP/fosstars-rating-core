package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.WeightedCompositeScore;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>The security testing score uses the following sub-scores.</p>
 * <ul>
 *  <li>{@link DependencyScanScore}</li>
 *  <li>{@link NoHttpToolScore}</li>
 *  <li>{@link MemorySafetyTestingScore}</li>
 *  <li>{@link StaticAnalysisScore}</li>
 *  <li>{@link FuzzingScore}</li>
 * </ul>
 * <p>There is plenty room for improvements.
 * The score can take into account a lot of other information.</p>
 */
public class ProjectSecurityTestingScore extends WeightedCompositeScore {

  /**
   * Initializes a new score.
   */
  ProjectSecurityTestingScore() {
    super("How well security testing is done for an open-source project",
        new DependencyScanScore(),
        new NoHttpToolScore(),
        new MemorySafetyTestingScore(),
        new StaticAnalysisScore(),
        new FuzzingScore());
  }

  /**
   * This class implements a verification procedure for {@link ProjectSecurityTestingScore}.
   * The class loads test vectors,
   * and provides methods to verify a {@link ProjectSecurityTestingScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "ProjectSecurityTestingScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link ProjectSecurityTestingScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(ProjectSecurityTestingScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link ProjectSecurityTestingScore}.
     */
    static Verification createFor(ProjectSecurityTestingScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
