package com.sap.oss.phosphor.fosstars.model.value;

import java.util.Objects;
import java.util.Optional;

public class SemanticVersion {

  private final int major;
  private final int minor;
  private final int micro;

  private SemanticVersion(int major, int minor, int micro) {
    this.major = major;
    this.minor = minor;
    this.micro = micro;
  }

  public static SemanticVersion create(int major, int minor, int micro) {
    return new SemanticVersion(major, minor, micro);
  }

  /**
   * Parse given version to a Semantic Version.
   * If version is null or can not be parsed an empty optional is returned.
   *
   * @param version version to parse
   * @return the parsed SemanticVersion or an empty optional
   */
  public static Optional<SemanticVersion> parse(String version) {
    // Objects.requireNonNull(version, "Given version to parse must not be null");
    if (version == null) {
      return Optional.empty();
    }
    String[] versionSplit = version.split("\\.");
    if (versionSplit.length >= 3) {
      // take first three as major/minor/micro if they are numbers
      try {
        int major = Integer.parseInt(versionSplit[0]);
        int minor = Integer.parseInt(versionSplit[1]);
        int micro = Integer.parseInt(versionSplit[2]);
        return Optional.of(new SemanticVersion(major, minor, micro));
      } catch (NumberFormatException ignored) {
        // not special handling required
      }
    }
    return Optional.empty();
  }

  /**
   * Check if given version is a semantic compatible version.
   *
   * @param version version to check
   * @return true if version follows the semantic version
   */
  public static boolean isSemVer(String version) {
    return parse(version).isPresent();
  }

  /**
   * Check if this version is in range of given versions (including).
   *
   * @param startVersion start version (including)
   * @param endVersion end version (including)
   * @return true if in range, otherwise false
   */
  public boolean isInRange(SemanticVersion startVersion, SemanticVersion endVersion) {
    if (this.major >= startVersion.major
        && this.minor >= startVersion.minor
        && this.micro >= startVersion.micro) {

      if (this.major <= endVersion.major
          && this.minor <= endVersion.minor
          && this.micro <= endVersion.micro) {

        return true;
      }
    }

    return false;
  }
}
