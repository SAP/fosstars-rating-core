package com.sap.oss.phosphor.fosstars.model.subject.oss;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.subject.AbstractSubject;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Maven artifact.
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class MavenArtifact extends AbstractSubject implements Artifact {

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
   * @param version The version of the artifact. It may be null.
   * @param project A {@link GitHubProject}. It may be null.
   */
  @JsonCreator
  public MavenArtifact(
      @JsonProperty("group") String group,
      @JsonProperty("artifact") String artifact,
      @JsonProperty("version") @Nullable String version,
      @JsonProperty("project") @Nullable GitHubProject project) {

    this.group = Objects.requireNonNull(group,
        "Oh no! You gave me a null instead of a Maven group identifier!");
    this.artifact = Objects.requireNonNull(artifact,
        "Oh no! You gave me a null instead of a Maven artifact identifier!");

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
    if (!super.equals(o) || !MavenArtifact.class.isAssignableFrom(o.getClass())) {
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
  public Optional<GitHubProject> project() {
    return Optional.ofNullable(project);
  }

  @Override
  public String purl() {
    if (version == null) {
      return format("pkg:maven/%s/%s", group, artifact);
    }

    return format("pkg:maven/%s/%s@%s", group, artifact, version);
  }
}