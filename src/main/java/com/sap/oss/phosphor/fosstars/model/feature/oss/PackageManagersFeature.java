package com.sap.oss.phosphor.fosstars.model.feature.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.AbstractFeature;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagers;
import com.sap.oss.phosphor.fosstars.model.value.PackageManagersValue;

/**
 * This feature contains a set of package managers that are used
 * in an open-source project.
 */
public class PackageManagersFeature extends AbstractFeature<PackageManagers> {

  /**
   * Initializes a feature.
   *
   * @param name The feature name.
   */
  @JsonCreator
  public PackageManagersFeature(@JsonProperty("name") String name) {
    super(name);
  }

  @Override
  public Value<PackageManagers> value(PackageManagers packageManagers) {
    return new PackageManagersValue(this, packageManagers);
  }

  @Override
  public Value<PackageManagers> parse(String string) {
    throw new UnsupportedOperationException("Unfortunately I can't parse package managers");
  }
}
