package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.WeightedCompositeScore;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is a security score for open-source projects.
 * The score takes into account multiple factors including:
 * <ul>
 *   <li>Information about vulnerabilities</li>
 *   <li>Security testing</li>
 *   <li>Security awareness of the community</li>
 *   <li>Project activity</li>
 *   <li>and so on.</li>
 * </ul>
 */
public class OssSecurityScore extends WeightedCompositeScore {

  /**
   * Initializes a new open-source security score.
   */
  public OssSecurityScore() {
    super("Security score for open-source projects",
        new ProjectActivityScore(),
        new ProjectPopularityScore(),
        new CommunityCommitmentScore(),
        new ProjectSecurityAwarenessScore(),
        new ProjectSecurityTestingScore(),
        new UnpatchedVulnerabilitiesScore());
  }

  /**
   * This class implements a verification procedure for {@link OssSecurityScore}.
   * The class loads test vectors, and provides methods to verify a {@link OssSecurityScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "OssSecurityScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link OssSecurityScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(OssSecurityScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link OssSecurityScore}.
     */
    static Verification createFor(OssSecurityScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }

}
