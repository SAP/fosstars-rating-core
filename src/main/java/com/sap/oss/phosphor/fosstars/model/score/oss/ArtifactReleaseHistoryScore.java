package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static java.time.temporal.ChronoUnit.DAYS;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.Iterator;

/**
 * The scoring function assesses how an open source project releases a new version.
 * The more often the higher the score.
 * The scoring function uses {@link OssFeatures#RELEASED_ARTIFACT_VERSIONS}.
 */
public class ArtifactReleaseHistoryScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  public ArtifactReleaseHistoryScore() {
    super("How frequent an open source project releases new versions",
        OssFeatures.RELEASED_ARTIFACT_VERSIONS);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<ArtifactVersions> artifactVersions = find(RELEASED_ARTIFACT_VERSIONS, values);

    if (artifactVersions.isUnknown()) {
      return scoreValue(0.0, artifactVersions)
          .makeUnknown()
          .explain("No versions are found. Hence, no release history score can be calculated")
          .withMinConfidence();
    }

    if (artifactVersions.get().size() <= 1) {
      return scoreValue(0.0, artifactVersions)
          .explain("Only one version is given. Hence, no release history score can be calculated")
          .withMinConfidence();
    }

    ScoreValue scoreValue = scoreValue(7.0, artifactVersions);

    Collection<ArtifactVersion> sortedByReleaseDate =
        ArtifactVersions.sortByReleaseDate(artifactVersions);

    // check release frequency over time
    Collection<VersionInfo> versionInfo = createVersionInfo(sortedByReleaseDate);
    VersionStats stats = calculateVersionStats(versionInfo);

    if (stats.averageDaysBetweenReleases < 10) {
      scoreValue.increase(3);
    } else if (stats.averageDaysBetweenReleases < 30) {
      scoreValue.increase(2);
    } else if (stats.averageDaysBetweenReleases < 60) {
      scoreValue.increase(1);
    } else if (stats.averageDaysBetweenReleases < 180) {
      scoreValue.decrease(1);
    } else if (stats.averageDaysBetweenReleases < 270) {
      scoreValue.decrease(2);
    } else if (stats.averageDaysBetweenReleases < 360) {
      scoreValue.decrease(3);
    }

    if (stats.releaseCycleTrend < 0) {
      scoreValue.decrease(-1 * stats.releaseCycleTrend);
    } else {
      scoreValue.increase(stats.releaseCycleTrend);
    }

    return scoreValue;
  }

  /**
   * Calculate the VersionStatistics based on given version infos.
   *
   * @param versionInfos used to calculate statistics
   * @return calculated version statistics
   */
  static VersionStats calculateVersionStats(Collection<VersionInfo> versionInfos) {
    IntSummaryStatistics stats = versionInfos.stream()
        .filter(v -> v.daysDiffToVersionBefore >= 0)
        .mapToInt(v -> v.daysDiffToVersionBefore)
        .summaryStatistics();

    int releaseCycleTrend = 0;
    int lastDays = -1;
    for (VersionInfo versionInfo : versionInfos) {
      if (versionInfo.daysDiffToVersionBefore >= 0) {
        if (lastDays < 0) {
          lastDays = versionInfo.daysDiffToVersionBefore;
        } else {
          if (lastDays > versionInfo.daysDiffToVersionBefore) {
            releaseCycleTrend--;
          } else if (lastDays < versionInfo.daysDiffToVersionBefore) {
            releaseCycleTrend++;
          }
        }
      }
    }

    double usedReleaseCycleTrend = (double) releaseCycleTrend / stats.getCount();
    return new VersionStats(usedReleaseCycleTrend, stats.getAverage());
  }

  /**
   * Create the version info based on list of sorted artifact versions.
   *
   * @param artifactVersions A sorted collection of artifact versions.
   * @return Version info.
   */
  static Collection<VersionInfo> createVersionInfo(Collection<ArtifactVersion> artifactVersions) {
    Collection<VersionInfo> versionInfo = new ArrayList<>();
    ArtifactVersion previousArtifact = null;
    Iterator<ArtifactVersion> iterator = artifactVersions.iterator();
    while (iterator.hasNext()) {
      int daysDiff = -1;
      ArtifactVersion nextArtifact = previousArtifact;
      if (previousArtifact == null) {
        nextArtifact = iterator.next();
      }
      if (iterator.hasNext()) {
        previousArtifact = iterator.next();
        daysDiff = (int) DAYS.between(previousArtifact.releaseDate(), nextArtifact.releaseDate());
      }
      versionInfo.add(new VersionInfo(daysDiff, nextArtifact));
    }
    if (artifactVersions.size() != versionInfo.size()) {
      versionInfo.add(new VersionInfo(-1, previousArtifact));
    }

    return versionInfo;
  }

  static class VersionStats {
    final double releaseCycleTrend;
    final double averageDaysBetweenReleases;

    public VersionStats(double releaseCycleTrend, double averageDaysBetweenReleases) {
      this.releaseCycleTrend = releaseCycleTrend;
      this.averageDaysBetweenReleases = averageDaysBetweenReleases;
    }

    @Override
    public String toString() {
      return "VersionStats{"
          + "releaseCycleTrend=" + releaseCycleTrend
          + ", averageDaysBetweenReleases=" + averageDaysBetweenReleases
          + '}';
    }
  }

  static class VersionInfo {
    final int daysDiffToVersionBefore;
    final ArtifactVersion version;

    public VersionInfo(int daysDiffToVersionBefore, ArtifactVersion version) {
      this.daysDiffToVersionBefore = daysDiffToVersionBefore;
      this.version = version;
    }
  }
}
