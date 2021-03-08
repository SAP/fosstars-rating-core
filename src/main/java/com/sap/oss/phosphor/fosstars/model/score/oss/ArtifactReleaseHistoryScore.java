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
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The scores assesses how well maintained an open source library is (in a specific version).
 * <ul>
 *   <li>{@link OssFeatures#RELEASED_ARTIFACT_VERSIONS}</li>
 * </ul>
 */
public class ArtifactReleaseHistoryScore extends FeatureBasedScore {

  /**
   * Initializes a new score.
   */
  public ArtifactReleaseHistoryScore() {
    // FIXME: name is only a working name and require change
    super("How the artifact releases history is",
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

    Collection<ArtifactVersion> sortedByReleaseDate = sortByReleaseDate(artifactVersions);

    // check release frequency over time
    Collection<VersionInfo> versionInfos = createVersionInfos(sortedByReleaseDate);
    VersionStats stats = createVersionStats(versionInfos);

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

    // FIXME (mibo): only for demo reason, remove before merge
    System.out.println(stats);
    //
    return scoreValue;
  }

  private VersionStats createVersionStats(Collection<VersionInfo> versionInfos) {
    //
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

  private Collection<VersionInfo> createVersionInfos(Collection<ArtifactVersion> artifactVersions) {
    Collection<VersionInfo> versionInfos = new ArrayList<>();
    ArtifactVersion beforeVersion = null;
    Iterator<ArtifactVersion> iterator = artifactVersions.iterator();
    while (iterator.hasNext()) {
      boolean latestVersion = false;
      int daysDiff = -1;
      ArtifactVersion next = beforeVersion;
      if (beforeVersion == null) {
        latestVersion = true;
        next = iterator.next();
      }
      if (iterator.hasNext()) {
        beforeVersion = iterator.next();
        daysDiff = (int) DAYS.between(beforeVersion.getReleaseDate(), next.getReleaseDate());
      }
      versionInfos.add(new VersionInfo(latestVersion, daysDiff, next));
    }
    if (artifactVersions.size() != versionInfos.size()) {
      // add oldest version
      versionInfos.add(new VersionInfo(false, -1, beforeVersion));
    }

    return versionInfos;
  }

  private static class VersionStats {
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

  private static class VersionInfo {
    final boolean latestVersion;
    final int daysDiffToVersionBefore;
    final ArtifactVersion version;

    public VersionInfo(boolean latestVersion, int daysDiffToVersionBefore,
        ArtifactVersion version) {
      this.latestVersion = latestVersion;
      this.daysDiffToVersionBefore = daysDiffToVersionBefore;
      this.version = version;
    }
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
