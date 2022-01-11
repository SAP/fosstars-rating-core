package com.sap.oss.phosphor.fosstars.tool.finder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * A configuration for the {@link Finder}.
 */
public class FinderConfig {

  /**
   * A list of configurations for GitHub organizations to be scanned.
   */
  final List<OrganizationConfig> organizationConfigs;

  /**
   * A list of configurations for GitHub projects to be scanned.
   */
  final List<ProjectConfig> projectConfigs;

  /**
   * A list of configurations for Maven artifacts to be scanned.
   */
  final List<MavenArtifactConfig> mavenArtifactConfigs;

  /**
   * Initializes an empty configuration.
   */
  FinderConfig() {
    this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  /**
   * Initializes a configuration.
   *
   * @param organizationConfigs A list of configurations for GitHub organizations to be scanned.
   * @param projectConfigs A list of configurations for GitHub projects to be scanned.
   * @param mavenArtifactConfigs A list of configurations for Maven artifacts to be scanned.
   */
  @JsonCreator
  FinderConfig(
      @JsonProperty("organizations") List<OrganizationConfig> organizationConfigs,
      @JsonProperty("repositories") List<ProjectConfig> projectConfigs,
      @JsonProperty("artifacts") List<MavenArtifactConfig> mavenArtifactConfigs) {

    this.organizationConfigs
        = organizationConfigs != null ? organizationConfigs : new ArrayList<>();
    this.projectConfigs
        = projectConfigs != null ? projectConfigs : new ArrayList<>();
    this.mavenArtifactConfigs
        = mavenArtifactConfigs != null ? mavenArtifactConfigs : new ArrayList<>();
  }

  public List<OrganizationConfig> organizationConfigs() {
    return organizationConfigs;
  }

  public List<ProjectConfig> projectConfigs() {
    return projectConfigs;
  }

  public List<MavenArtifactConfig> mavenArtifactConfigs() {
    return mavenArtifactConfigs;
  }
}
