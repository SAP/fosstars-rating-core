package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * The scores check how old the latest release artifact version of the open-source project is.
 * The score uses {@link OssFeatures#RELEASED_ARTIFACT_VERSIONS} feature.
 */
public class ArtifactLatestReleaseAgeScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  public ArtifactLatestReleaseAgeScore() {
    super("How old the latest released artifact is", RELEASED_ARTIFACT_VERSIONS);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<ArtifactVersions> artifactVersions = find(RELEASED_ARTIFACT_VERSIONS, values);

    ScoreValue scoreValue = scoreValue(Score.MIN, artifactVersions);

    if (artifactVersions.isUnknown()) {
      logger.info("No release information was found (unknown artifact versions value).");
      return scoreValue.makeUnknown().withMinConfidence();
    }
    Collection<ArtifactVersion> sortedByReleaseDate =
        ArtifactVersions.sortByReleaseDate(artifactVersions);

    if (sortedByReleaseDate.isEmpty()) {
      logger.info("No release information was found (empty artifact versions value).");
      return scoreValue.makeUnknown().withMinConfidence();
    }

    ArtifactVersion latestArtifact = sortedByReleaseDate.iterator().next();
    LocalDateTime oneMonthBack = LocalDateTime.now().minusMonths(1);
    LocalDateTime sixMonthBack = LocalDateTime.now().minusMonths(6);
    LocalDateTime oneYearBack = LocalDateTime.now().minusYears(1);

    // check age of latest release
    if (latestArtifact.releaseDate().isAfter(oneMonthBack)) {
      return scoreValue.set(Score.MAX);
    } else if (latestArtifact.releaseDate().isAfter(sixMonthBack)) {
      return scoreValue.set(5.0);
    } else if (latestArtifact.releaseDate().isAfter(oneYearBack)) {
      return scoreValue.set(2.0);
    }

    // otherwise, return the minimal score
    return scoreValue.set(Score.MIN);
  }
}
