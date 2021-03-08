package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.AbstractScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>This is a security score for open-source artifacts.
 * The score is based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link OssArtifactVersionScore}</li>
 *   <li>{@link OssSecurityScore}</li>
 * </ul>
 */
public class OssArtifactSecurityScore extends AbstractScore {

  /**
   * A description of the score.
   */
  private static final String DESCRIPTION
      = "The security score for open-source artifacts evaluates how the artifact score of the given"
      + "version is and merge it with the security score of the project the artifact belongs. "
      + "If the artifact score for the given version is low, "
      + "the security score for open-source artifacts will be very low.";


  private final OssArtifactVersionScore ossArtifactVersionScore;
  private final OssSecurityScore ossSecurityScore;

  /**
   * Initialize the score with default values.
   */
  public OssArtifactSecurityScore() {
    this(new OssArtifactVersionScore(), new OssSecurityScore());
  }

  /**
   * Initializes a new security score for open-source artifacts.
   *
   * @param ossArtifactVersionScore artifact version and releases based score
   * @param ossSecurityScore project specific security score
   */
  @JsonCreator
  public OssArtifactSecurityScore(
      @JsonProperty("ossArtifactVersionScore") OssArtifactVersionScore ossArtifactVersionScore,
      @JsonProperty("ossSecurityScore") OssSecurityScore ossSecurityScore) {

    super("Security score for an artifact of an open-source project", DESCRIPTION);
    this.ossArtifactVersionScore = ossArtifactVersionScore;
    this.ossSecurityScore = ossSecurityScore;
  }

  @Override
  public Set<Feature<?>> features() {
    return Collections.emptySet();
  }

  @Override
  public Set<Score> subScores() {
    return setOf(ossArtifactVersionScore, ossSecurityScore);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    ScoreValue artifactVersionScore = calculateIfNecessary(
        new OssArtifactVersionScore(), new ValueHashSet(values));
    ScoreValue projectSecurityScore = calculateIfNecessary(
        new OssSecurityScore(), new ValueHashSet(values));

    ScoreValue scoreValue = scoreValue(Score.MIN, artifactVersionScore, projectSecurityScore);

    if (artifactVersionScore.isUnknown()) {
      return scoreValue.withMinConfidence().makeUnknown();
    }

    if (artifactVersionScore.get() >= 9.0) {
      double updatedScore = 0.11 * artifactVersionScore.get() * projectSecurityScore.get();
      return scoreValue.set(updatedScore);
    } else {
      double updatedScore = 0.1 * (1 + artifactVersionScore.get()) * projectSecurityScore.get();
      return scoreValue.set(updatedScore);
    }
  }
}
