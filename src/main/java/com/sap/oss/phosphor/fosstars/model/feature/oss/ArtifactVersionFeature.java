package com.sap.oss.phosphor.fosstars.model.feature.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersionValue;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersionsValue;

/**
 * This feature contains .... TODO
 */
public class ArtifactVersionFeature extends AbstractFeature<String> {

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  @JsonCreator
  public ArtifactVersionFeature(@JsonProperty("name") String name) {
    super(name);
  }

  @Override
  public Value<String> value(String artifactVersions) {
    return new ArtifactVersionValue(this, artifactVersions);
  }

  @Override
  public Value<String> parse(String string) {
    return new ArtifactVersionValue(this, string);
  }
}
//public class ArtifactVersionFeature extends AbstractFeature<ArtifactVersion> {
//
//  /**
//   * Initializes a feature.
//   *
//   * @param name The feature name.
//   */
//  @JsonCreator
//  public ArtifactVersionFeature(@JsonProperty("name") String name) {
//    super(name);
//  }
//
//  @Override
//  public Value<ArtifactVersion> value(ArtifactVersion artifactVersions) {
//    return new ArtifactVersionValue(this, artifactVersions);
//  }
//
//  @Override
//  public Value<ArtifactVersion> parse(String string) {
//    throw new UnsupportedOperationException("Unfortunately I can't parse versions yet");
//  }
//}
