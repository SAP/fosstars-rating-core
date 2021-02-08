package com.sap.oss.phosphor.fosstars.model.value;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Contains version tag and release date for an artifact version.
 */
public class ArtifactVersion {

  private final String version;
  private final LocalDate releaseDate;

  private int major;
  private int minor;
  private int micro;
  private boolean validSemVer = false;

  /**
   * Initialize the ArtifactVersion based on version tag and release date.
   *
   * @param version version tag
   * @param releaseDate release date
   */
  public ArtifactVersion(String version, LocalDate releaseDate) {
    Objects.requireNonNull(version, "Version must be set");
    Objects.requireNonNull(releaseDate, "Release date must be set");

    this.version = version;
    this.releaseDate = releaseDate;
    initSemVer(version);
  }

  private void initSemVer(String version) {
    String[] versionSplit = version.split("\\.");
    if (versionSplit.length >= 3) {
      // take first three as major/minor/micro if they are numbers
      try {
        major = Integer.parseInt(versionSplit[0]);
        minor = Integer.parseInt(versionSplit[1]);
        micro = Integer.parseInt(versionSplit[2]);
        validSemVer = true;
      } catch (NumberFormatException e) {
        // FIXME: replace sout
        System.out.printf("Unable to parse %s as a semantic version%n", version);
      }
    }
  }

  public int getMajor() {
    return major;
  }

  public int getMinor() {
    return minor;
  }

  public int getMicro() {
    return micro;
  }

  public boolean isValidSemanticVersion() {
    return validSemVer;
  }

  public String getVersion() {
    return version;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  @Override
  public String toString() {
    return "{"
        + "version='" + version + '\''
        + ", releaseDate=" + releaseDate
        + '}';
  }
}
