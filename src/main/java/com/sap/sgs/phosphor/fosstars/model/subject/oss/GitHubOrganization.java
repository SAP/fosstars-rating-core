package com.sap.sgs.phosphor.fosstars.model.subject.oss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.subject.AbstractSubject;
import java.util.Objects;

/**
 * An organization (or a user) on GitHub.
 */
public class GitHubOrganization extends AbstractSubject {

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
    if (!super.equals(o)) {
      return false;
    }
    GitHubOrganization that = (GitHubOrganization) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name);
  }
}
