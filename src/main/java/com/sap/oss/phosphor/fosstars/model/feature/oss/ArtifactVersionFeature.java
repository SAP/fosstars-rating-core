package com.sap.oss.phosphor.fosstars.model.feature.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersionValue;

/**
 * This feature contains a version of an artifact released by the open-source project.
 */
public class ArtifactVersionFeature extends AbstractFeature<ArtifactVersion> {

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
  public Value<ArtifactVersion> value(ArtifactVersion artifactVersions) {
    return new ArtifactVersionValue(this, artifactVersions);
  }

  @Override
  public Value<ArtifactVersion> parse(String string) {
    throw new UnsupportedOperationException("Unfortunately I can't parse versions yet");
  }
}
