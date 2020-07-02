package com.sap.sgs.phosphor.fosstars.tool.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.tool.Project;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * A project on GitHub.
 */
public class GitHubProject implements Project {

  /**
   * Shows that there is no rating value assigned to a project.
   */
  private static final RatingValue NO_RATING_VALUE = null;

  /**
   * Shows that the date is unknown.
   */
  private static final Date NO_DATE = null;

  /**
   * An organization (or user) that owns the project.
   */
  private final GitHubOrganization organization;

  /**
   * Project's name.
   */
  private final String name;

  /**
   * A URL to the project's SCM.
   */
  private final URL url;

  /**
   * A rating value for the project.
   */
  private RatingValue ratingValue;

  /**
   * When the rating value was calculated.
   */
  private Date ratingValueDate;

  /**
   * Initializes a project.
   *
   * @param owner An owner of the project.
   * @param name A name of the project.
   */
  public GitHubProject(String owner, String name) {
    this(new GitHubOrganization(owner), name);
  }

  /**
   * Initializes a new project.
   *
   * @param organization An organization that owns the project.
   * @param name Project's name.
   */
  public GitHubProject(GitHubOrganization organization, String name) {
    this(organization, name, makeUrl(organization, name), NO_RATING_VALUE, NO_DATE);
  }

  /**
   * Initializes a new project.
   *
   * @param organization An organization (or user) that owns the project.
   * @param name Project's name.
   * @param url A URL to the project's SCM.
   * @param ratingValue A rating value for the project.
   * @param ratingValueDate When the rating value was calculated.
   */
  @JsonCreator
  public GitHubProject(
      @JsonProperty("organization") GitHubOrganization organization,
      @JsonProperty("name") String name,
      @JsonProperty("url") URL url,
      @JsonProperty("ratingValue") RatingValue ratingValue,
      @JsonProperty("ratingValueDate") Date ratingValueDate) {

    this.organization = Objects.requireNonNull(organization, "Hey! Organization can't be null!");
    this.name = Objects.requireNonNull(name, "Hey! Project's name can't be null!");
    this.url = Objects.requireNonNull(url, "Hey! URL can't be null!");
    this.ratingValue = ratingValue;
    this.ratingValueDate = ratingValueDate;
  }

  @Override
  @JsonGetter("url")
  public URL url() {
    return url;
  }

  /**
   * Returns project's path.
   *
   * @return Project's path.
   */
  public String path() {
    return String.format("%s/%s", organization.name(), name);
  }

  /**
   * Returns the organization.
   *
   * @return The organization.
   */
  @JsonGetter("organization")
  public GitHubOrganization organization() {
    return organization;
  }

  /**
   * Returns project's name.
   *
   * @return Project's name.
   */
  @JsonGetter("name")
  public String name() {
    return name;
  }

  /**
   * Returns the rating value. The method is used for serialization.
   *
   * @return The rating value.
   */
  @JsonGetter("ratingValue")
  private RatingValue getRatingValue() {
    return ratingValue;
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
    ratingValueDate = new Date();
  }

  /**
   * Returns a date when the rating value was calculated.
   *
   * @return The date.
   */
  @JsonGetter("ratingValueDate")
  public Date ratingValueDate() {
    return ratingValueDate;
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
        && Objects.equals(name, project.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(organization, name);
  }

  @Override
  public String toString() {
    return url.toString();
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
    String name = parts[2];
    if (name.endsWith(".git")) {
      name = name.substring(0, name.length() - 4);
    }
    return new GitHubProject(new GitHubOrganization(parts[1]), name);
  }

  /**
   * Constructs a URL of a project.
   *
   * @param organization An organization which owns the project.
   * @param name The project's name.
   * @return A URL of the project.
   */
  private static URL makeUrl(GitHubOrganization organization, String name) {
    try {
      return new URL(String.format("https://github.com/%s/%s", organization.name(), name));
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Could not create a URL!", e);
    }
  }

}
