package com.sap.oss.phosphor.fosstars.model.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.util.Objects;

/**
 * A value for a feature that holds a set of package managers.
 */
public class PackageManagersValue extends AbstractKnownValue<PackageManagers> {

  /**
   * A set of package managers.
   */
  private final PackageManagers packageManagers;

  /**
   * Initializes a value for a feature.
   *
   * @param feature The feature.
   * @param packageManagers Package managers.
   */
  @JsonCreator
  public PackageManagersValue(
      @JsonProperty("feature") Feature<PackageManagers> feature,
      @JsonProperty("packageManagers") PackageManagers packageManagers) {

    super(feature);
    this.packageManagers = Objects.requireNonNull(
        packageManagers, "Package managers can't be null!");
  }

  @Override
  @JsonGetter("packageManagers")
  public PackageManagers get() {
    return packageManagers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof PackageManagersValue == false) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    PackageManagersValue that = (PackageManagersValue) o;
    return Objects.equals(packageManagers, that.packageManagers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), packageManagers);
  }

  @Override
  public String toString() {
    return packageManagers.toString();
  }
}
