package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value for a feature that holds a version for an artifact.
 */
public class ArtifactVersionValue extends AbstractKnownValue<ArtifactVersion> {

  /**
   * Version of an artifact.
   */
  private final ArtifactVersion artifactVersion;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param artifactVersion The version.
   */
  @JsonCreator
  public ArtifactVersionValue(
      @JsonProperty("feature") Feature<ArtifactVersion> feature,
      @JsonProperty("version") ArtifactVersion artifactVersion) {

    super(feature);

    this.artifactVersion =
        Objects.requireNonNull(artifactVersion, "Hey! Artifact version can't be null!");
  }

  @Override
  @JsonGetter("version")
  public ArtifactVersion get() {
    return artifactVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ArtifactVersionValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ArtifactVersionValue that = (ArtifactVersionValue) o;
    return Objects.equals(artifactVersion, that.artifactVersion);
  }

  @Override
  public int hashCode() {
    return artifactVersion.hashCode();
  }

  @Override
  public String toString() {
    return artifactVersion.toString();
  }
}
