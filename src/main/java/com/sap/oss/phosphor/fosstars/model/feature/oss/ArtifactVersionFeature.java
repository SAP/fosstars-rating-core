package com.sap.oss.phosphor.fosstars.model.feature.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersionValue;

/**
 * This feature holds a version of an artifact for which the security rating is calculated.
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