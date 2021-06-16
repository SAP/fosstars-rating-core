package com.sap.oss.phosphor.fosstars.model.subject.oss;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.subject.AbstractSubject;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * A project on GitHub.
 */
public class GitHubProject extends AbstractSubject implements OpenSourceProject {

  /**
   * An organization (or user) that owns the project.
   */
  private final GitHubOrganization organization;

  /**
   * Project's name.
   */
  private final String name;

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
    this(organization, name, NO_RATING_VALUE, NO_RATING_DATE);
  }

  /**
   * Initializes a new project.
   *
   * @param organization An organization (or user) that owns the project.
   * @param name Project's name.
   * @param ratingValue A rating value for the project.
   * @param ratingValueDate When the rating value was calculated.
   */
  @JsonCreator
  public GitHubProject(
      @JsonProperty("organization") GitHubOrganization organization,
      @JsonProperty("name") String name,
      @JsonProperty("ratingValue") @Nullable RatingValue ratingValue,
      @JsonProperty("ratingValueDate") @Nullable Date ratingValueDate) {

    super(ratingValue, ratingValueDate);
    this.organization = Objects.requireNonNull(organization, "Hey! Organization can't be null!");
    this.name = Objects.requireNonNull(name, "Hey! Project's name can't be null!");
  }

  @Override
  public URL scm() {
    try {
      return new URL(format("https://github.com/%s/%s", organization.name(), name));
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("Could not create a URL!", e);
    }
  }

  @Override
  public String purl() {
    return format("pkg:github/%s/%s", organization.name(), name);
  }

  /**
   * Returns project's path.
   *
   * @return Project's path.
   */
  public String path() {
    return format("%s/%s", organization.name(), name);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!super.equals(o) || !GitHubProject.class.isAssignableFrom(o.getClass())) {
      return false;
    }
    GitHubProject that = (GitHubProject) o;
    return Objects.equals(organization, that.organization) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), organization, name);
  }

  @Override
  public String toString() {
    return scm().toString();
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
    if (!"github.com".equals(url.getHost())) {
      throw new IllegalArgumentException(format("The host name is not github.com: %s", urlString));
    }
    String[] parts = url.getPath().split("/");
    String name = parts[2];
    if (name.endsWith(".git")) {
      name = name.substring(0, name.length() - 4);
    }
    return new GitHubProject(new GitHubOrganization(parts[1]), name);
  }

  /**
   * Checks if a URL points to GitHub.
   *
   * @param url The URL to be checked.
   * @return True if the URL points to GitHub, false otherwise.
   */
  public static boolean isOnGitHub(String url) {
    return url != null
        && (url.startsWith("https://github.com/") || url.startsWith("http://github.com/"));
  }
}
