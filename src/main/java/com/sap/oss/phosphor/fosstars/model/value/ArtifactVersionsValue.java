package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value for a feature that holds versions for an artifact.
 */
public class ArtifactVersionsValue extends AbstractKnownValue<ArtifactVersions> {

  /**
   * Versions of an artifact.
   */
  private final ArtifactVersions artifactVersions;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param artifactVersions The versions.
   */
  @JsonCreator
  public ArtifactVersionsValue(
      @JsonProperty("feature") Feature<ArtifactVersions> feature,
      @JsonProperty("versions") ArtifactVersions artifactVersions) {

    super(feature);
    if (artifactVersions == null) {
      this.artifactVersions = ArtifactVersions.ofNothing();
    } else {
      this.artifactVersions = artifactVersions;
    }
  }

  @Override
  @JsonGetter("versions")
  public ArtifactVersions get() {
    return artifactVersions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ArtifactVersionsValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ArtifactVersionsValue that = (ArtifactVersionsValue) o;
    return Objects.equals(artifactVersions, that.artifactVersions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(artifactVersions);
  }

  @Override
  public String toString() {
    return artifactVersions.toString();
  }
}
