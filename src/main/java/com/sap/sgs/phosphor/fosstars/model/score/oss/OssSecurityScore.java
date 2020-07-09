package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.WeightedCompositeScore;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>This is a security score for open-source projects.
 * The score is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link ProjectSecurityTestingScore}</li>
 *   <li>{@link ProjectActivityScore}</li>
 *   <li>{@link ProjectPopularityScore}</li>
 *   <li>{@link CommunityCommitmentScore}</li>
 *   <li>{@link ProjectSecurityAwarenessScore}</li>
 *   <li>{@link UnpatchedVulnerabilitiesScore}</li>
 *   <li>{@link VulnerabilityDiscoveryAndSecurityTestingScore}</li>
 * </ul>
 */
public class OssSecurityScore extends WeightedCompositeScore {

  /**
   * A set of sub-scores.
   */
  private static final Set<Score> SUB_SCORES = new HashSet<>();

  static {
    ProjectSecurityTestingScore securityTestingScore = new ProjectSecurityTestingScore();

    SUB_SCORES.add(securityTestingScore);
    SUB_SCORES.add(new ProjectActivityScore());
    SUB_SCORES.add(new ProjectPopularityScore());
    SUB_SCORES.add(new CommunityCommitmentScore());
    SUB_SCORES.add(new ProjectSecurityAwarenessScore());
    SUB_SCORES.add(new UnpatchedVulnerabilitiesScore());
    SUB_SCORES.add(new VulnerabilityDiscoveryAndSecurityTestingScore(securityTestingScore));
  }

  /**
   * Initializes a new open-source security score.
   */
  public OssSecurityScore() {
    super("Security score for open-source projects", SUB_SCORES);
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
