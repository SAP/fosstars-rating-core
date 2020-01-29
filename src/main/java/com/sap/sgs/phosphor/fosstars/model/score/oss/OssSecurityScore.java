package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.score.oss.OssScores.COMMUNITY_COMMITMENT;
import static com.sap.sgs.phosphor.fosstars.model.score.oss.OssScores.PROJECT_ACTIVITY;
import static com.sap.sgs.phosphor.fosstars.model.score.oss.OssScores.PROJECT_POPULARITY;
import static com.sap.sgs.phosphor.fosstars.model.score.oss.OssScores.PROJECT_SECURITY_AWARENESS;
import static com.sap.sgs.phosphor.fosstars.model.score.oss.OssScores.PROJECT_SECURITY_TESTING;
import static com.sap.sgs.phosphor.fosstars.model.score.oss.OssScores.UNPATCHED_VULNERABILITIES;
import static com.sap.sgs.phosphor.fosstars.model.score.oss.OssScores.VULNERABILITY_LIFETIME;

import com.sap.sgs.phosphor.fosstars.model.score.WeightedCompositeScore;

/**
 * This is a security score for open-source projects
 * which takes into account multiple factors including:
 *
 * <ul>
 *   <li>Information about vulnerabilities</li>
 *   <li>Security testing</li>
 *   <li>Security awareness of the community</li>
 *   <li>Project activity</li>
 *   <li>and so on.</li>
 * </ul>
 */
public class OssSecurityScore extends WeightedCompositeScore {

  OssSecurityScore() {
    super("Security score for open-source projects",
        PROJECT_ACTIVITY,
        PROJECT_POPULARITY,
        COMMUNITY_COMMITMENT,
        PROJECT_SECURITY_AWARENESS,
        PROJECT_SECURITY_TESTING,
        UNPATCHED_VULNERABILITIES,
        VULNERABILITY_LIFETIME);
  }

}
