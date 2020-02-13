package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.Score;

/**
 * This class holds a list of scores for open-source projects.
 */
public class OssScores {

  // don't allow creating instances of this class
  private OssScores() {

  }

  /**
   * Shows how actively an open-source project is developed.
   */
  public static final ProjectActivityScore PROJECT_ACTIVITY = new ProjectActivityScore();

  /**
   * Shows how popular an open-source project is.
   */
  public static final ProjectPopularityScore PROJECT_POPULARITY = new ProjectPopularityScore();

  /**
   * Shows how an open-source community is committed to support and maintain an open-source project.
   */
  public static final CommunityCommitmentScore COMMUNITY_COMMITMENT
      = new CommunityCommitmentScore();

  /**
   * Shows how well developers and community are aware about security.
   */
  public static final ProjectSecurityAwarenessScore PROJECT_SECURITY_AWARENESS
      = new ProjectSecurityAwarenessScore();

  /**
   * Shows how well security testing is done for an open-source project.
   */
  public static final Score PROJECT_SECURITY_TESTING = new ProjectSecurityTestingScore();

  /**
   * Shows how well vulnerabilities are patched.
   */
  public static final Score UNPATCHED_VULNERABILITIES = new UnpatchedVulnerabilitiesScore();

  /**
   * Shows how fast vulnerabilities are patched.
   */
  public static final Score VULNERABILITY_LIFETIME = new VulnerabilityLifetimeScore();

  /**
   * A security rating for an open-source project.
   */
  public static final Score SECURITY_SCORE = new OssSecurityScore();

}
