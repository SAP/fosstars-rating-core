package com.sap.oss.phosphor.fosstars.tool.finder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * A configuration for a GitHub project to be scanned.
 */
public class ProjectConfig {

  /**
   * An organization.
   */
  final String organization;

  /**
   * Project's name.
   */
  final String name;

  /**
   * Initializes a new configuration.
   *
   * @param organization An organization.
   * @param name Project's name;
   */
  @JsonCreator
  public ProjectConfig(
      @JsonProperty("organization") String organization,
      @JsonProperty("name") String name) {

    Objects.requireNonNull(organization, "Hey! Organization can't be null!");
    Objects.requireNonNull(name, "Hey! Name can't be null!");
    this.organization = organization;
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof ProjectConfig == false) {
      return false;
    }
    ProjectConfig that = (ProjectConfig) o;
    return Objects.equals(organization, that.organization)
        && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organization, name);
  }

  public String organization() {
    return organization;
  }

  public String name() {
    return name;
  }
}
