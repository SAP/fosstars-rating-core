package com.sap.sgs.phosphor.fosstars.tool.github;

import java.util.Objects;

/**
 * An organization (or a user) on GitHub.
 */
class GitHubOrganization {

  /**
   * A name.
   */
  private final String name;

  /**
   * Initializes an organization.
   *
   * @param name A name of the organization.
   */
  GitHubOrganization(String name) {
    this.name = Objects.requireNonNull(name, "Hey! Organization's name can't be null!");
  }

  /**
   * Returns organization's name.
   */
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
