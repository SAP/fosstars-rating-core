package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.tool.Project;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * A project on GitHub.
 */
public class GitHubProject implements Project {

  /**
   * An organization (or user) that owns the project.
   */
  private final GitHubOrganization organization;

  /**
   * Project's name.
   */
  private final String name;

  /**
   * A rating value for the project.
   */
  private RatingValue ratingValue;

  /**
   * Initializes a new project.
   *
   * @param organization An organization that owns the project.
   * @param name Project's name.
   */
  public GitHubProject(GitHubOrganization organization, String name) {
    this.organization = Objects.requireNonNull(organization, "Hey! Organization can't be null!");
    this.name = Objects.requireNonNull(name, "Hey! Project's name can't be null!");
  }

  /**
   * Returns a URL of the project.
   */
  String url() {
    return String.format("https://github.com/%s/%s", organization.name(), name);
  }

  /**
   * Returns the organization.
   */
  GitHubOrganization organization() {
    return organization;
  }

  /**
   * Returns project's name.
   */
  String name() {
    return name;
  }

  /**
   * Returns a rating value assigned to the project.
   *
   * @return An {@link Optional} with the rating value for the project.
   */
  Optional<RatingValue> ratingValue() {
    return ratingValue == null ? Optional.empty() : Optional.of(ratingValue);
  }

  /**
   * Set a rating value for the project.
   *
   * @param value The rating value.
   */
  void set(RatingValue value) {
    ratingValue = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o instanceof GitHubProject == false) {
      return false;
    }
    GitHubProject project = (GitHubProject) o;
    return Objects.equals(organization, project.organization)
        && Objects.equals(name, project.name)
        && Objects.equals(ratingValue, project.ratingValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organization, name, ratingValue);
  }

  /**
   * Makes a project from its URL.
   *
   * @param urlString The URL.
   * @return A new project.
   * @throws IOException If something went wrong.
   */
  public static GitHubProject parse(String urlString) throws IOException {
    URL url = new URL(urlString);
    String[] parts = url.getPath().split("/");
    if (parts.length != 3) {
      throw new IllegalArgumentException(
          String.format("The URL doesn't seem to be correct: %s", urlString));
    }
    return new GitHubProject(new GitHubOrganization(parts[1]), parts[2]);
  }

}
