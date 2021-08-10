package com.sap.oss.phosphor.fosstars.maven;

import static java.lang.String.format;

import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Maven GAV coordinates.
 */
public class GAV {

  /**
   * No version.
   */
  public static final String NO_VERSION = null;

  /**
   * Group ID.
   */
  private final String group;

  /**
   * Artifact ID.
   */
  private final String artifact;

  /**
   * Version.
   */
  private final String version;

  /**
   * Create new GAV coordinates.
   *
   * @param group Group ID.
   * @param artifact Artifact ID.
   * @param version Version. May be null.
   */
  public GAV(String group, String artifact, @Nullable String version) {
    Objects.requireNonNull(group, "Oh no! Group can't be null!");
    Objects.requireNonNull(artifact, "Oh no! Artifact can't be null!");

    this.group = group;
    this.artifact = artifact;
    this.version = version;
  }

  /**
   * Return the group ID.
   *
   * @return The group ID.
   */
  public String group() {
    return group;
  }

  /**
   * Return the artifact ID.
   *
   * @return The artifact ID.
   */
  public String artifact() {
    return artifact;
  }

  /**
   * Return the version if available.
   *
   * @return The version.
   */
  public Optional<String> version() {
    return Optional.ofNullable(version);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || !GAV.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    GAV gav = (GAV) o;
    return Objects.equals(group, gav.group)
        && Objects.equals(artifact, gav.artifact) && Objects.equals(version, gav.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(group, artifact, version);
  }

  @Override
  public String toString() {
    if (version == null) {
      return format("%s:%s", group, artifact);
    }

    return format("%s:%s:%s", group, artifact, version);
  }

  /**
   * Parse a string with GAV coordinates. The coordinates have to have group and artifact IDs,
   * but version is optional.
   *
   * @param gav The string with GAV coordinates.
   * @return An instance of {@link GAV}.
   * @throws IllegalArgumentException If the string doesn't have valid GAV coordinates.
   */
  public static GAV parse(String gav) {
    Objects.requireNonNull(gav, "Oh no! GAV is null");

    String[] parts = gav.trim().split(":");

    if (parts.length < 2 || parts.length > 3) {
      throw new IllegalArgumentException("Oh no! The string doesn't look like GAV!");
    }

    return new GAV(parts[0], parts[1], parts.length > 2 ? parts[2] : null);
  }
}
