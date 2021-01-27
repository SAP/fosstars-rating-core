package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value for a feature that holds a set of programming languages.
 */
public class ArtifactVersionValue extends AbstractValue<String> {

  /**
   * A language.
   */
  private final String artifactVersions;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param artifactVersions The versions.
   */
  @JsonCreator
  public ArtifactVersionValue(
      @JsonProperty("feature") Feature<String> feature,
      @JsonProperty("versions") String artifactVersions) {

    super(feature);
    this.artifactVersions = Objects.requireNonNull(artifactVersions, "Version can't be null!");
  }

  @Override
  @JsonGetter("version")
  public String get() {
    return artifactVersions;
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
    return Objects.equals(artifactVersions, that.artifactVersions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), artifactVersions);
  }

  @Override
  public String toString() {
    return artifactVersions;
  }
}
//public class ArtifactVersionValue extends AbstractValue<ArtifactVersion> {
//
//  /**
//   * A language.
//   */
//  private final ArtifactVersion artifactVersions;
//
//  /**
//   * Initializes a value for a feature.
//   *
//   * @param feature The feature.
//   * @param artifactVersions The versions.
//   */
//  @JsonCreator
//  public ArtifactVersionValue(
//      @JsonProperty("feature") Feature<ArtifactVersion> feature,
//      @JsonProperty("versions") ArtifactVersion artifactVersions) {
//
//    super(feature);
//    this.artifactVersions = Objects.requireNonNull(artifactVersions, "Version can't be null!");
//  }
//
//  @Override
//  @JsonGetter("versions")
//  public ArtifactVersion get() {
//    return artifactVersions;
//  }
//
//  @Override
//  public boolean equals(Object o) {
//    if (this == o) {
//      return true;
//    }
//    if (o instanceof ArtifactVersionValue == false) {
//      return false;
//    }
//    if (!super.equals(o)) {
//      return false;
//    }
//    ArtifactVersionValue that = (ArtifactVersionValue) o;
//    return Objects.equals(artifactVersions, that.artifactVersions);
//  }
//
//  @Override
//  public int hashCode() {
//    return Objects.hash(super.hashCode(), artifactVersions);
//  }
//
//  @Override
//  public String toString() {
//    return artifactVersions.toString();
//  }
//}
