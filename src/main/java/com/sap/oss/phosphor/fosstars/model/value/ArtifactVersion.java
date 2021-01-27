package com.sap.oss.phosphor.fosstars.model.value;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Contains version tag and release date for an artifact version.
 */
public class ArtifactVersion implements Comparable<ArtifactVersion> {

  private final String version;
  private final LocalDate releaseDate;

  /**
   * Initialize the ArtifactVersion based on version tag and release date.
   *  @param version version tag
   * @param releaseDate release date
   */
  public ArtifactVersion(String version, LocalDate releaseDate) {
    Objects.requireNonNull(version, "Version must be set");
    Objects.requireNonNull(releaseDate, "Release date must be set");

    this.version = version;
    this.releaseDate = releaseDate;
  }

  public String getVersion() {
    return version;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  @Override
  public String toString() {
    return "ArtifactVersion{"
        + "version='" + version + '\''
        + ", releaseDate=" + releaseDate
        + '}';
  }

  @Override
  public int compareTo(ArtifactVersion artifactVersion) {
    return artifactVersion.getReleaseDate().compareTo(releaseDate);
  }
}
