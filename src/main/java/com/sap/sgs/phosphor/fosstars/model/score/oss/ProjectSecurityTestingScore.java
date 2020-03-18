package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.score.AverageCompositeScore;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * The security testing score uses the following sub-scores:
 * <ul>
 *  <li>{@link DependencyScanScore}</li>
 *  <li>{@link LgtmScore}</li>
 * </ul>
 * There is plenty room for improvements.
 * The score can take into account a lot of other information.
 */
public class ProjectSecurityTestingScore extends AverageCompositeScore {

  /**
   * Initializes a new score.
   */
  ProjectSecurityTestingScore() {
    super("How well security testing is done for an open-source project",
        new DependencyScanScore(),
        new LgtmScore());
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
    private static final String TEST_VECTORS_CSV = "ProjectSecurityTestingScoreTestVectors.csv";

    /**
     * Initializes a {@link Verification}
     * for a {@link ProjectSecurityTestingScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(ProjectSecurityTestingScore score, List<TestVector> vectors) {
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
      try (InputStream is = ProjectSecurityTestingScore.Verification.class
          .getResourceAsStream(TEST_VECTORS_CSV)) {

        return new Verification(score, loadTestVectorsFromCsvResource(score.subScores(), is));
      }
    }
  }
}
