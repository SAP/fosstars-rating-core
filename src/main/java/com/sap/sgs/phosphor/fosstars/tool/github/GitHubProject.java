package com.sap.sgs.phosphor.fosstars.tool.github;

import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import com.sap.sgs.phosphor.fosstars.tool.Project;
import java.io.IOException;
import java.net.URL;

public class GitHubProject implements Project {

  private final Organization organization;
  private final String name;
  private RatingValue ratingValue;

  public GitHubProject(Organization organization, String name) {
    this.organization = organization;
    this.name = name;
  }

  String url() {
    return String.format("https://github.com/%s/%s", organization.name(), name);
  }

  Organization organization() {
    return organization;
  }

  String name() {
    return name;
  }

  RatingValue ratingValue() {
    return ratingValue;
  }

  void set(RatingValue value) {
    ratingValue = value;
  }

  public static GitHubProject parse(String urlString) throws IOException {
    URL url = new URL(urlString);
    String[] parts = url.getPath().split("/");
    if (parts.length != 3) {
      throw new IllegalArgumentException(
          String.format("The URL doesn't seem to be correct: %s", urlString));
    }
    return new GitHubProject(new Organization(parts[1]), parts[2]);
  }

}
