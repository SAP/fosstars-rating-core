package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
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
import java.util.Optional;

/**
 * The scoring function checks if the given artifact version is up-to-date.
 * The scoring functions uses the following features:
 * <ul>
 *   <li>{@link OssFeatures#RELEASED_ARTIFACT_VERSIONS}</li>
 *   <li>{@link OssFeatures#ARTIFACT_VERSION}</li>
 * </ul>
 */
public class ArtifactVersionUpToDateScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  public ArtifactVersionUpToDateScore() {
    super("How up-to-date the given version is",
        RELEASED_ARTIFACT_VERSIONS,
        ARTIFACT_VERSION);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<ArtifactVersions> artifactVersionsValue = find(RELEASED_ARTIFACT_VERSIONS, values);
    Value<ArtifactVersion> versionValue = find(ARTIFACT_VERSION, values);

    ScoreValue scoreValue = scoreValue(Score.MIN, artifactVersionsValue, versionValue);
    if (versionValue.isUnknown() || artifactVersionsValue.isUnknown()) {
      return scoreValue.withMinConfidence().makeUnknown();
    }

    ArtifactVersions artifactVersions = artifactVersionsValue.get();
    Collection<ArtifactVersion> sortedByReleaseDate = artifactVersions.sortByReleaseDate();
    Optional<ArtifactVersion> mappedVersion =
        artifactVersions.get(versionValue.get().version());

    if (mappedVersion.isPresent()) {
      ArtifactVersion latestVersion = sortedByReleaseDate.iterator().next();
      ArtifactVersion checkVersion = mappedVersion.get();

      LocalDateTime oneMonthBack = LocalDateTime.now().minusMonths(1);
      LocalDateTime sixMonthBack = LocalDateTime.now().minusMonths(6);
      LocalDateTime oneYearBack = LocalDateTime.now().minusYears(1);

      // check if used version is latest version
      if (latestVersion.equals(checkVersion)) {
        scoreValue.set(Score.MAX);
      } else {
        scoreValue.set(8.0);
      }

      if (checkVersion.releaseDate().isBefore(oneYearBack)) {
        return scoreValue.decrease(8);
      } else if (checkVersion.releaseDate().isBefore(sixMonthBack)) {
        return scoreValue.decrease(4);
      } else if (checkVersion.releaseDate().isBefore(oneMonthBack)) {
        return scoreValue.decrease(1);
      }
      return scoreValue;
    }

    // otherwise, return minimal score
    return scoreValue.withMinConfidence()
        .explain(
            String.format("Given version %s was not found in released artifact versions.",
                versionValue.get()))
        .makeUnknown();
  }
}