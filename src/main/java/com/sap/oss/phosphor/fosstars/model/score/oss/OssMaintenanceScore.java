package com.sap.oss.phosphor.fosstars.model.score.oss;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>This is a security score for open-source projects.
 * The score is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link ArtifactMaintenanceScore}</li>
 *   <li>{@link ArtifactVersionReleaseScore}</li>
 *   <li>{@link ProjectActivityScore}</li>
 *   <li>{@link ProjectPopularityScore}</li>
 *   <li>{@link CommunityCommitmentScore}</li>
 * </ul>
 */
public class OssMaintenanceScore extends WeightedCompositeScore {

  /**
   * A set of sub-scores.
   */
  private static final Set<Score> SUB_SCORES = new HashSet<>();

  static {
    SUB_SCORES.add(new ArtifactMaintenanceScore());
    SUB_SCORES.add(new ArtifactVersionReleaseScore());
    SUB_SCORES.add(new ArtifactVersionAgeScore());
    //    SUB_SCORES.add(new ProjectActivityScore());
    //    SUB_SCORES.add(new ProjectPopularityScore());
    //    SUB_SCORES.add(new CommunityCommitmentScore());
  }

  /**
   * Initializes a new open-source security score.
   */
  public OssMaintenanceScore() {
    super("Maintenance score for open-source projects", SUB_SCORES);
  }
}
