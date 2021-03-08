package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value for a feature that.
 * TODO add proper javadoc
 */
public class ArtifactVersionsValue extends AbstractValue<ArtifactVersions> {

  /**
   * A language.
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
    this.artifactVersions = Objects.requireNonNull(artifactVersions, "Versions can't be null!");
  }

  @Override
  public boolean isUnknown() {
    return artifactVersions == null || artifactVersions.empty();
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
    return Objects.hash(super.hashCode(), artifactVersions);
  }

  @Override
  public String toString() {
    return artifactVersions.toString();
  }
}
