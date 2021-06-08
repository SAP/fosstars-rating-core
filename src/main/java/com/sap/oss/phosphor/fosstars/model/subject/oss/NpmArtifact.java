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
 * NPM package artifact.
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class NpmArtifact extends AbstractSubject implements Artifact {

  /**
   * NPM package identifier.
   */
  private final String identifier;

  /**
   * NPM package version.
   */
  private final String version;

  /**
   * NPM package's GitHub project.
   */
  private final GitHubProject project;

  /**
   * Initializes a NPM artifact.
   *
   * @param identifier An NPM package identifier.
   * @param version The NPM package version. It may be null.
   * @param project A {@link GitHubProject}. It may be null.
   */
  @JsonCreator
  public NpmArtifact(
      @JsonProperty("identifier") String identifier,
      @JsonProperty("version") @Nullable String version,
      @JsonProperty("project") @Nullable GitHubProject project) {

    this.identifier = Objects.requireNonNull(identifier,
        "Oh no! You gave me a null instead of an NPM identifier!");

    this.version = version;
    this.project = project;
  }

  /**
   * Returns NPM package's identifier.
   *
   * @return NPM package's identifier.
   */
  public String identifier() {
    return identifier;
  }

  /**
   * Returns NPM package's version.
   *
   * @return NPM package's version.
   */
  public Optional<String> version() {
    return Optional.ofNullable(version);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!super.equals(o) || !NpmArtifact.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    NpmArtifact that = (NpmArtifact) o;
    return Objects.equals(identifier, that.identifier)
        && Objects.equals(version, that.version)
        && Objects.equals(project, that.project);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier, version, project);
  }

  @Override
  public Optional<GitHubProject> project() {
    return Optional.ofNullable(project);
  }

  @Override
  public String purl() {
    if (version == null) {
      return format("pkg:npm/%s", identifier);
    }

    return format("pkg:npm/%s@%s", identifier, version);
  }
}