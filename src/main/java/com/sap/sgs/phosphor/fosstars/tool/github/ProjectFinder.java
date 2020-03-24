package com.sap.sgs.phosphor.fosstars.tool.github;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ProjectFinder {

  public ProjectFinder organization(String name) {
    return this;
  }

  public ProjectFinder organization(String name, List<String> excludeList) {
    return this;
  }

  public ProjectFinder add(Organization organization) {
    return this;
  }

  public ProjectFinder config(String path) {
    return this;
  }

  public List<GitHubProject> run() {
    throw new UnsupportedOperationException("No repositories for you!");
  }

  private static class ConfigParser {

    void parse(String path) throws IOException {
      throw new UnsupportedOperationException("No parsing for you!");
    }

    List<Organization> organizations() {
      throw new UnsupportedOperationException("No organizations for you!");
    }

    Map<Organization, List<String>> exludeLists() {
      throw new UnsupportedOperationException("No exclude lists for you!");
    }

    List<GitHubProject> repositories() {
      throw new UnsupportedOperationException("No repositories for you!");
    }
  }

}
