package com.sap.oss.phosphor.fosstars.tool.finder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * A configuration for a Maven artifact identifier to be scanned.
 */
public class MavenArtifactConfig {

  /**
   * A group of an artifact.
   */
  final String group;

  /**
   * An artifact.
   */
  final String artifact;

  /**
   * Initializes a new configuration.
   *
   * @param group Of an artifact.
   * @param artifact An artifact.
   */
  @JsonCreator
  public MavenArtifactConfig(
      @JsonProperty("group") String group,
      @JsonProperty("artifact") String artifact) {
    Objects.requireNonNull(group, "Hey! Group can't be null!");
    Objects.requireNonNull(artifact, "Hey! Artifact can't be null!");
    this.group = group;
    this.artifact = artifact;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof MavenArtifactConfig == false) {
      return false;
    }
    MavenArtifactConfig that = (MavenArtifactConfig) o;
    return Objects.equals(group, that.group)
        && Objects.equals(artifact, that.artifact);
  }

  @Override
  public int hashCode() {
    return Objects.hash(group, artifact);
  }

  public String group() {
    return group;
  }

  public String artifact() {
    return artifact;
  }
}
