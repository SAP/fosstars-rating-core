package com.sap.oss.phosphor.fosstars.model.score.oss;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.oss.phosphor.fosstars.model.weight.ScoreWeights;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * <p>This scoring functions assesses how the given artifact version relates to
 * released versions, the release history of artifacts and if there are
 * known vulnerabilities for the given artifact version
 * of an open-source project.
 * The score is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link ArtifactLatestReleaseAgeScore}</li>
 *   <li>{@link ArtifactReleaseHistoryScore}</li>
 *   <li>{@link ArtifactVersionUpToDateScore}</li>
 *   <li>{@link ArtifactVersionVulnerabilityScore}</li>
 * </ul>
 */
public class ArtifactVersionSecurityScore extends WeightedCompositeScore {

  /**
   * A description of the score.
   */
  private static final String DESCRIPTION
      = "This scoring functions assesses how the given artifact version relates to "
      + "released versions, the release history of artifacts and if there are "
      + "known vulnerabilities for the given artifact version "
      + "of an open-source project. "
      + "If there are vulnerabilities with high severity for given version, "
      + "the score is min. "
      + "If there are at least two vulnerabilities with medium severity for given version, "
      + "the score is min.";

  /**
   * A set of sub-scores.
   */
  private static final Set<Score> SUB_SCORES = new HashSet<>();

  static {
    SUB_SCORES.add(new ArtifactVersionUpToDateScore());
    SUB_SCORES.add(new ArtifactReleaseHistoryScore());
    SUB_SCORES.add(new ArtifactLatestReleaseAgeScore());
    SUB_SCORES.add(new ArtifactVersionVulnerabilityScore());
  }

  /**
   * Initializes a new open-source security score.
   */
  public ArtifactVersionSecurityScore() {
    super("Security score for an artifact version of an open-source project",
        SUB_SCORES, initWeights());
  }

  /**
   * Initializes weights for sub-scores.
   *
   * @return The weights of sub-scores.
   */
  private static ScoreWeights initWeights() {
    return ScoreWeights.empty()
        .set(ArtifactVersionUpToDateScore.class, new ImmutableWeight(0.1))
        .set(ArtifactLatestReleaseAgeScore.class, new ImmutableWeight(0.1))
        .set(ArtifactReleaseHistoryScore.class, new ImmutableWeight(0.5))
        .set(ArtifactVersionVulnerabilityScore.class, new ImmutableWeight(1.0));
  }

  @Override
  public ScoreValue calculate(Set<Value<?>> values) {
    ScoreValue scoreValue = super.calculate(values);

    Optional<ScoreValue> versionScore
        = scoreValue.findUsedSubScoreValue(ArtifactVersionUpToDateScore.class);
    Optional<ScoreValue> vulnerabilityScore
        = scoreValue.findUsedSubScoreValue(ArtifactVersionVulnerabilityScore.class);

    if (!versionScore.isPresent() || !vulnerabilityScore.isPresent()) {
      throw new IllegalStateException("Sub-scores should have been already calculated!");
    }

    if (versionScore.get().isUnknown() || vulnerabilityScore.get().isUnknown()) {
      return scoreValue.makeUnknown().withMinConfidence();
    }

    if (vulnerabilityScore.get().get() < scoreValue.get()) {
      return scoreValue.set(vulnerabilityScore.get().get());
    }

    return scoreValue;
  }

  @Override
  public String description() {
    return DESCRIPTION;
  }
}
