package com.sap.oss.phosphor.fosstars.model.subject.oss;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.Optional;

/**
 * NPM module artifact.
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class NpmArtifact implements Artifact {

  /**
   * NPM module identifier.
   */
  private final String identifier;

  /**
   * NPM artifact's GitHub project.
   */
  private final GitHubProject project;

  /**
   * Initializes a NPM artifact.
   *
   * @param identifier An NPM identifier.
   * @param project A {@link GitHubProject}.
   */
  @JsonCreator
  public NpmArtifact(@JsonProperty("identifier") String identifier,
      @JsonProperty("project") GitHubProject project) {
    this.identifier = Objects.requireNonNull(identifier,
        "Oh no! You gave me a null instead of an NPM identifier!");
    this.project = project;
  }

  /**
   * Returns NPM's identifier.
   *
   * @return NPM's identifier.
   */
  public String identifier() {
    return identifier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof NpmArtifact == false) {
      return false;
    }
    NpmArtifact that = (NpmArtifact) o;
    return Objects.equals(identifier, that.identifier) && Objects.equals(project, that.project);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier, project);
  }

  @Override
  public String toString() {
    return String.format("%s %s", identifier, project);
  }

  @Override
  public Optional<GitHubProject> project() {
    return Optional.ofNullable(project);
  }
}