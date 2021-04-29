package com.sap.oss.phosphor.fosstars.model.subject.oss;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Optional;

/**
 * Maven artifact.
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class MavenArtifact implements Artifact {

  /**
   * Maven artifact's group id.
   */
  private final String group;

  /**
   * Maven artifact's artifact id.
   */
  private final String artifact;


  /**
   * Maven artifact's version.
   */
  private final String version;

  /**
   * Maven artifact's GitHub project.
   */
  private final GitHubProject project;

  /**
   * Initializes a Maven artifact.
   *
   * @param group A group id of the artifact.
   * @param artifact An artifact id of the artifact.
   * @param version The version of the artifact.
   * @param project A {@link GitHubProject}.
   */
  @JsonCreator
  public MavenArtifact(@JsonProperty("group") String group,
      @JsonProperty("artifact") String artifact,
      @JsonProperty("version") String version,
      @JsonProperty("project") GitHubProject project) {
    this.group = Objects.requireNonNull(group,
        "Oh no! You gave me a null instead of a Maven group from GA coordinate!");
    this.artifact = Objects.requireNonNull(artifact,
        "Oh no! You gave me a null instead of a Maven artifact from GA coordinate!");
    this.version = version;
    this.project = project;
  }

  /**
   * Returns group id of the artifact.
   *
   * @return Group id.
   */
  public String group() {
    return group;
  }

  /**
   * Returns artifact id.
   *
   * @return Artifact id.
   */
  public String artifact() {
    return artifact;
  }

  /**
   * Returns artifact version.
   *
   * @return Artifact version.
   */
  public Optional<String> version() {
    return Optional.ofNullable(version);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof MavenArtifact == false) {
      return false;
    }
    MavenArtifact that = (MavenArtifact) o;
    return Objects.equals(group, that.group)
        && Objects.equals(artifact, that.artifact)
        && Objects.equals(version, that.version)
        && Objects.equals(project, that.project);
  }

  @Override
  public int hashCode() {
    return Objects.hash(group, artifact, version, project);
  }

  @Override
  public String toString() {
    return String.format("%s:%s:%s %s", group, artifact, version, project);
  }

  @Override
  public Optional<GitHubProject> project() {
    return Optional.ofNullable(project);
  }
}