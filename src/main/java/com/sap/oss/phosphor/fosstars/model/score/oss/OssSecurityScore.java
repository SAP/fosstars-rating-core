package com.sap.oss.phosphor.fosstars.model.score.oss;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
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
    SUB_SCORES.add(new SecurityReviewScore());
  }

  /**
   * Initializes a new open-source security score.
   */
  public OssSecurityScore() {
    super("Security score for open-source projects", SUB_SCORES);
  }
}
