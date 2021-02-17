package com.sap.oss.phosphor.fosstars.model.score.oss;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>This is a security score for open-source projects.
 * The score is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link ArtifactVersionScore}</li>
 *   <li>{@link ArtifactReleaseHistoryScore}</li>
 *   <li>{@link ProjectActivityScore}</li>
 *   <li>{@link ProjectPopularityScore}</li>
 *   <li>{@link CommunityCommitmentScore}</li>
 * </ul>
 */
public class OssArtifactMaintenanceScore extends WeightedCompositeScore {

  /**
   * A set of sub-scores.
   */
  private static final Set<Score> SUB_SCORES = new HashSet<>();

  static {
    SUB_SCORES.add(new ArtifactVersionScore());
    SUB_SCORES.add(new ArtifactReleaseHistoryScore());
    SUB_SCORES.add(new ArtifactAgeScore());
    //    SUB_SCORES.add(new ProjectActivityScore());
    //    SUB_SCORES.add(new ProjectPopularityScore());
    //    SUB_SCORES.add(new CommunityCommitmentScore());
  }

  /**
   * Initializes a new open-source security score.
   */
  public OssArtifactMaintenanceScore() {
    super("Maintenance effort score for an artifact of open-source project", SUB_SCORES);
  }
}
