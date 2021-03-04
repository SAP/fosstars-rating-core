package com.sap.oss.phosphor.fosstars.model.score.oss;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>The artifact version score evaluates release history of artifacts of an open-source project.
 * The score is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link ArtifactVersionScore}</li>
 *   <li>{@link ArtifactReleaseHistoryScore}</li>
 *   <li>{@link ProjectActivityScore}</li>
 * </ul>
 */
public class OssArtifactVersionScore extends WeightedCompositeScore {

  /**
   * A set of sub-scores.
   */
  private static final Set<Score> SUB_SCORES = new HashSet<>();

  static {
    SUB_SCORES.add(new ArtifactVersionScore());
    SUB_SCORES.add(new ArtifactReleaseHistoryScore());
    SUB_SCORES.add(new ArtifactAgeScore());
  }

  /**
   * Initializes a new open-source security score.
   */
  public OssArtifactVersionScore() {
    super("Score for artifact release history of an open-source project", SUB_SCORES);
  }
}
