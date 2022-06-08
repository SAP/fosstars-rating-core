package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.AbstractScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * <p>This is a security scoring function for open-source artifact versions.
 * The scoring function is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link ArtifactVersionSecurityScore}</li>
 *   <li>{@link OssSecurityScore}</li>
 * </ul>
 */
public class OssArtifactSecurityScore extends AbstractScore {

  /**
   * A description of the score.
   */
  private static final String DESCRIPTION
      = "The security score for open source artifact versions shows how safe it is to use a "
      + "specific version of an open source component. Besides information about the artifact "
      + "version, the scoring functions takes into account the security score of the project "
      + "to which the artifact belongs to.";


  private final ArtifactVersionSecurityScore artifactVersionSecurityScore;
  private final OssSecurityScore ossSecurityScore;

  /**
   * Initialize the score with default values.
   */
  public OssArtifactSecurityScore() {
    this(new ArtifactVersionSecurityScore(), new OssSecurityScore());
  }

  /**
   * Initializes a new security score for open-source artifacts.
   *
   * @param artifactVersionSecurityScore artifact version and releases based score
   * @param ossSecurityScore project specific security score
   */
  @JsonCreator
  public OssArtifactSecurityScore(@JsonProperty("artifactVersionSecurityScore")
      ArtifactVersionSecurityScore artifactVersionSecurityScore,
      @JsonProperty("ossSecurityScore") OssSecurityScore ossSecurityScore) {

    super("Security score for an artifact of an open-source project", DESCRIPTION);
    this.artifactVersionSecurityScore = Objects.requireNonNull(
        artifactVersionSecurityScore, "Hey! ArtifactVersionSecurityScore can't be null!");
    this.ossSecurityScore = Objects.requireNonNull(
        ossSecurityScore, "Hey! OssSecurityScore can't be null!");
  }

  @Override
  public Set<Feature<?>> features() {
    return Collections.emptySet();
  }

  @Override
  public Set<Score> subScores() {
    return setOf(artifactVersionSecurityScore, ossSecurityScore);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    ScoreValue artifactVersionSecurityScore = calculateIfNecessary(
        this.artifactVersionSecurityScore, new ValueHashSet(values));
    ScoreValue projectSecurityScore = calculateIfNecessary(
        this.ossSecurityScore, new ValueHashSet(values));

    ScoreValue scoreValue =
        scoreValue(Score.MIN, artifactVersionSecurityScore, projectSecurityScore);

    if (artifactVersionSecurityScore.isUnknown() || projectSecurityScore.isUnknown()) {
      return scoreValue.withMinConfidence().makeUnknown();
    }

    final double updatedScore;
    if (artifactVersionSecurityScore.get() >= 9.0) {
      updatedScore = 0.11 * artifactVersionSecurityScore.get() * projectSecurityScore.get();
    } else {
      updatedScore = 0.1 * (0.9 + artifactVersionSecurityScore.get()) * projectSecurityScore.get();
    }
    return scoreValue.set(Math.min(Score.MAX, updatedScore));
  }

  /**
   * Get the ArtifactVersionSecurityScore used as sub-score.
   *
   * @return the ArtifactVersionSecurityScore used as sub-score
   */
  @JsonGetter("artifactVersionSecurityScore")
  public ArtifactVersionSecurityScore artifactVersionSecurityScore() {
    return artifactVersionSecurityScore;
  }

  /**
   * Get the OssSecurityScore used as sub-score.
   *
   * @return the OssSecurityScore used as sub-score
   */
  @JsonGetter("ossSecurityScore")
  public OssSecurityScore ossSecurityScore() {
    return ossSecurityScore;
  }
}
