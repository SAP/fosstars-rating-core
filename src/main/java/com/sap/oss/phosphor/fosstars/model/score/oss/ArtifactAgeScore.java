package com.sap.oss.phosphor.fosstars.model.score.oss;

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
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The scores check how old the latest available artifact version of the open-source project is.
 * <ul>
 *   <li>{@link OssFeatures#RELEASED_ARTIFACT_VERSIONS}</li>
 * </ul>
 */
public class ArtifactAgeScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  public ArtifactAgeScore() {
    super("How old the released artifacts are",
        OssFeatures.RELEASED_ARTIFACT_VERSIONS);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<ArtifactVersions> artifactVersions = find(RELEASED_ARTIFACT_VERSIONS, values);

    ScoreValue scoreValue = scoreValue(Score.MIN, artifactVersions);

    if (artifactVersions.isUnknown()) {
      logger.info("No release information was found (unknown artifact versions value).");
      return scoreValue.makeUnknown().withMinConfidence();
    }
    Collection<ArtifactVersion> sortedByReleaseDate = sortByReleaseDate(artifactVersions);

    if (sortedByReleaseDate.isEmpty()) {
      logger.info("No release information was found (empty artifact versions value).");
      return scoreValue.makeUnknown().withMinConfidence();
    }

    ArtifactVersion latestVersion = sortedByReleaseDate.iterator().next();
    LocalDate oneMonthBack = LocalDate.now().minusMonths(1);
    LocalDate sixMonthBack = LocalDate.now().minusMonths(6);
    LocalDate oneYearBack = LocalDate.now().minusYears(1);

    // check age of latest release
    if (latestVersion.getReleaseDate().isAfter(oneMonthBack)) {
      return scoreValue.set(Score.MAX);
    } else if (latestVersion.getReleaseDate().isAfter(sixMonthBack)) {
      return scoreValue.set(5.0);
    } else if (latestVersion.getReleaseDate().isAfter(oneYearBack)) {
      return scoreValue.set(2.0);
    }

    // otherwise, return the minimal score
    return scoreValue.set(Score.MIN);
  }

  private static final Comparator<ArtifactVersion> RELEASE_DATE_COMPARISON =
      (a, b) -> b.getReleaseDate().compareTo(a.getReleaseDate());

  private Collection<ArtifactVersion> sortByReleaseDate(Value<ArtifactVersions> artifactVersions) {
    ArtifactVersions versions = artifactVersions.get();
    SortedSet<ArtifactVersion> sortedArtifacts = new TreeSet<>(RELEASE_DATE_COMPARISON);
    for (ArtifactVersion artifactVersion : versions) {
      sortedArtifacts.add(artifactVersion);
    }
    return sortedArtifacts;
  }
}
