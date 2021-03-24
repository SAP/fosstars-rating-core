package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value for a feature that holds a version of an artifact.
 */
public class ArtifactVersionValue extends AbstractValue<String> {

  /**
   * Version of an artifact.
   */
  private final String artifactVersion;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param artifactVersion The versions.
   */
  @JsonCreator
  public ArtifactVersionValue(
      @JsonProperty("feature") Feature<String> feature,
      @JsonProperty("version") String artifactVersion) {

    super(feature);
    this.artifactVersion = artifactVersion;
  }

  @Override
  @JsonGetter("version")
  public String get() {
    return artifactVersion;
  }

  @Override
  public boolean isUnknown() {
    return artifactVersion == null || artifactVersion.isEmpty();
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
    return Objects.hash(super.hashCode(), artifactVersion);
  }

  @Override
  public String toString() {
    return artifactVersion;
  }
}