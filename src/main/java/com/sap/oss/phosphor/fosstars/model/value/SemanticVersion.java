package com.sap.oss.phosphor.fosstars.model.value;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class represents a semantic version (see: https://semver.org/).
 */
public class SemanticVersion {

  private static final Pattern SEMANTIC_VERSION_PATTERN =
      Pattern.compile("(\\d*).(\\d*).(\\d*)(.*)");

  private final int major;
  private final int minor;
  private final int micro;

  private SemanticVersion(int major, int minor, int micro) {
    if (major < 0 || minor < 0 || micro < 0) {
      throw new IllegalArgumentException(
          "The version parts of a semantic version must be positive integers");
    }
    this.major = major;
    this.minor = minor;
    this.micro = micro;
  }

  /**
   * Create a SemanticVersion based on given version information.
   * @param major the major version (positive integer)
   * @param minor the major version (positive integer)
   * @param micro the major version (positive integer)
   * @return the SemanticVersion
   */
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
    if (version == null) {
      return Optional.empty();
    }
    Matcher matcher = SEMANTIC_VERSION_PATTERN.matcher(version);
    if (matcher.matches()) {
      // take first three as major/minor/micro if they are numbers
      try {
        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int micro = Integer.parseInt(matcher.group(3));
        return Optional.of(new SemanticVersion(major, minor, micro));
      } catch (NumberFormatException ignored) {
        // no special handling required
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
  public static boolean isSemanticVersion(String version) {
    return parse(version).isPresent();
  }

  /**
   * Get the major version.
   * @return the major version
   */
  public int getMajor() {
    return major;
  }

  /**
   * Get the minor version.
   * @return the minor version
   */
  public int getMinor() {
    return minor;
  }

  /**
   * Get the micro version.
   * @return the micro version
   */
  public int getMicro() {
    return micro;
  }

  /**
   * Check if this version is in range of given versions (including).
   *
   * @param startVersion start version (including)
   * @param endVersion end version (including)
   * @return true if in range, otherwise false
   */
  public boolean isInRange(SemanticVersion startVersion, SemanticVersion endVersion) {
    return checkStartVersion(startVersion) && checkEndVersion(endVersion);
  }

  private boolean checkStartVersion(SemanticVersion startVersion) {
    if (this.major > startVersion.major) {
      return true;
    }
    if (this.major == startVersion.major) {
      if (this.minor > startVersion.minor) {
        return true;
      } else if (this.minor == startVersion.minor) {
        if (this.micro >= startVersion.micro) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean checkEndVersion(SemanticVersion endVersion) {
    if (this.major < endVersion.major) {
      return true;
    }
    if (this.major == endVersion.major) {
      if (this.minor < endVersion.minor) {
        return true;
      } else if (this.minor == endVersion.minor) {
        if (this.micro <= endVersion.micro) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SemanticVersion that = (SemanticVersion) o;
    return major == that.major && minor == that.minor && micro == that.micro;
  }

  @Override
  public int hashCode() {
    return Objects.hash(major, minor, micro);
  }

  @Override
  public String toString() {
    return String.format("%s.%s.%s", major, minor, micro);
  }
}
