package com.sap.sgs.phosphor.fosstars.tool.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * An organization (or a user) on GitHub.
 */
public class GitHubOrganization {

  /**
   * A name.
   */
  private final String name;

  /**
   * Initializes an organization.
   *
   * @param name A name of the organization.
   */
  @JsonCreator
  public GitHubOrganization(@JsonProperty("name") String name) {
    this.name = Objects.requireNonNull(name, "Hey! Organization's name can't be null!");
  }

  /**
   * Returns organization's name.
   *
   * @return A name of the organization.
   */
  @JsonGetter("name")
  public String name() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof GitHubOrganization == false) {
      return false;
    }
    GitHubOrganization that = (GitHubOrganization) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
