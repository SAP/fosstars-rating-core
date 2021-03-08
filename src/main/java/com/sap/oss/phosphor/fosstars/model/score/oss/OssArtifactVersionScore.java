package com.sap.oss.phosphor.fosstars.model.score.oss;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.oss.phosphor.fosstars.model.weight.ScoreWeights;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>The artifact version score evaluates how the given version relates to
 * released versions, the release history of artifacts and if there are
 * known vulnerabilities for the given artifact version
 * of an open-source project.
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
   * A description of the score.
   */
  private static final String DESCRIPTION
      = "The artifact version score evaluates how the given version relates to "
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

  @Override
  public ScoreValue calculate(Set<Value<?>> values) {
    ScoreValue scoreValue = super.calculate(values);

    ScoreValue vulScore = calculateIfNecessary(
        new ArtifactVersionVulnerabilityScore(), new ValueHashSet(values));

    if (vulScore.isUnknown()) {
      return scoreValue.set(Score.MIN).makeUnknown().withMinConfidence();
    }

    if (vulScore.get() < scoreValue.get()) {
      return scoreValue.set(vulScore.get());
    }

    return scoreValue;
  }

  @Override
  public String description() {
    return DESCRIPTION;
  }
}
