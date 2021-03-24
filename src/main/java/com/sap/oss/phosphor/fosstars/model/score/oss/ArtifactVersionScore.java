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
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

/**
 * The scores check how old the given artifact version is
 * and if it is the latest available artifact version.
 * <ul>
 *   <li>{@link OssFeatures#RELEASED_ARTIFACT_VERSIONS}</li>
 *   <li>{@link OssFeatures#ARTIFACT_VERSION}</li>
 * </ul>
 */
public class ArtifactVersionScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  public ArtifactVersionScore() {
    super("How old the given version is and is it the latest available artifact version",
        RELEASED_ARTIFACT_VERSIONS,
        ARTIFACT_VERSION);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<ArtifactVersions> artifactVersionsValue = find(RELEASED_ARTIFACT_VERSIONS, values);
    Value<String> versionValue = find(ARTIFACT_VERSION, values);

    ScoreValue scoreValue = scoreValue(Score.MIN, artifactVersionsValue, versionValue);
    if (versionValue.isUnknown() || artifactVersionsValue.isUnknown()) {
      return scoreValue.withMinConfidence().makeUnknown();
    }

    ArtifactVersions artifactVersions = artifactVersionsValue.get();
    Collection<ArtifactVersion> sortedByReleaseDate = artifactVersions.getSortByReleaseDate();
    Optional<ArtifactVersion> mappedVersion =
        artifactVersions.getArtifactVersion(versionValue.get());

    if (mappedVersion.isPresent()) {
      ArtifactVersion latestVersion = sortedByReleaseDate.iterator().next();
      ArtifactVersion checkVersion = mappedVersion.get();

      LocalDate oneMonthBack = LocalDate.now().minusMonths(1);
      LocalDate sixMonthBack = LocalDate.now().minusMonths(6);
      LocalDate oneYearBack = LocalDate.now().minusYears(1);

      // check if used version is latest version
      if (latestVersion.equals(checkVersion)) {
        scoreValue.set(Score.MAX);
      } else {
        scoreValue.set(8.0);
      }

      if (checkVersion.getReleaseDate().isBefore(oneYearBack)) {
        return scoreValue.decrease(8);
      } else if (checkVersion.getReleaseDate().isBefore(sixMonthBack)) {
        return scoreValue.decrease(4);
      } else if (checkVersion.getReleaseDate().isBefore(oneMonthBack)) {
        return scoreValue.decrease(1);
      }
      return scoreValue;
    }


    // otherwise, return the minimal score
    return scoreValue.set(Score.MIN);
  }
}
