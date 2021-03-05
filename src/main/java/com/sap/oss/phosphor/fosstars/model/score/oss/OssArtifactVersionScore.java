package com.sap.oss.phosphor.fosstars.model.score.oss;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.oss.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.oss.phosphor.fosstars.model.weight.ScoreWeights;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>The artifact version score evaluates release history of artifacts of an open-source project.
 * The score is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link ArtifactVersionScore}</li>
 *   <li>{@link ArtifactReleaseHistoryScore}</li>
 *   <li>{@link ProjectActivityScore}</li>
 *   <li>{@link ArtifactVersionVulnerabilityScore}</li>
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
    SUB_SCORES.add(new ArtifactVersionVulnerabilityScore());
  }

  /**
   * Initializes a new open-source security score.
   */
  public OssArtifactVersionScore() {
    super("Security score for an artifact of an open-source project",
        SUB_SCORES, initWeights());
  }

  /**
   * Initializes weights for sub-scores.
   *
   * @return The weights of sub-scores.
   */
  private static ScoreWeights initWeights() {
    return ScoreWeights.empty()
        .set(ArtifactVersionScore.class, new ImmutableWeight(0.1))
        .set(ArtifactAgeScore.class, new ImmutableWeight(0.1))
        .set(ArtifactReleaseHistoryScore.class, new ImmutableWeight(0.5))
        .set(ArtifactVersionVulnerabilityScore.class, new ImmutableWeight(1.0));
  }
}
