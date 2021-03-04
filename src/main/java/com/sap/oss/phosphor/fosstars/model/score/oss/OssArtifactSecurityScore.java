package com.sap.oss.phosphor.fosstars.model.score.oss;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>This is a security score for open-source artifacts.
 * The score is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link OssArtifactVersionScore}</li>
 *   <li>{@link OssSecurityScore}</li>
 * </ul>
 */
public class OssArtifactSecurityScore extends WeightedCompositeScore {

  /**
   * A set of sub-scores.
   */
  private static final Set<Score> SUB_SCORES = new HashSet<>();

  static {
    SUB_SCORES.add(new OssArtifactVersionScore());
    SUB_SCORES.add(new OssSecurityScore());
  }

  /**
   * Initializes a new open-source security score.
   */
  public OssArtifactSecurityScore() {
    super("Security score for an artifact of an open-source project", SUB_SCORES);
  }
}
