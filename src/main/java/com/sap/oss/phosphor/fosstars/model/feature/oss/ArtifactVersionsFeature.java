package com.sap.oss.phosphor.fosstars.model.feature.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersionsValue;

/**
 * This feature contains a set of versions of the artifact released by the open-source project.
 */
public class ArtifactVersionsFeature extends AbstractFeature<ArtifactVersions> {

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  @JsonCreator
  public ArtifactVersionsFeature(@JsonProperty("name") String name) {
    super(name);
  }

  @Override
  public Value<ArtifactVersions> value(ArtifactVersions artifactVersions) {
    return new ArtifactVersionsValue(this, artifactVersions);
  }

  @Override
  public Value<ArtifactVersions> parse(String string) {
    throw new UnsupportedOperationException("Unfortunately I can't parse versions yet");
  }
}
