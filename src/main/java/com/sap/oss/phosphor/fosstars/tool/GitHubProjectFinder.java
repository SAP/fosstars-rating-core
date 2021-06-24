package com.sap.oss.phosphor.fosstars.tool;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubOrganization;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * The class scans a number of organizations on GitHub and returns their projects.
 * The class also checks specific projects and add them to results of the scan if they exist.
 */
public class GitHubProjectFinder {

  /**
   * The default minimal number of stars.
   */
  private static final int DEFAULT_STARS = 0;

  /**
   * A page size for requests to GitHub.
   */
  private static final int PAGE_SIZE = 100;

  /**
   * An empty exclude list.
   */
  static final List<String> EMPTY_EXCLUDE_LIST = Collections.emptyList();

  /**
   * An interface to GitHub.
   */
  private final GitHub github;

  /**
   * A cache of GitHub organizations.
   */
  private final Map<String, GHOrganization> organizations = new HashMap<>();

  /**
   * A cache of GitHub projects.
   */
  private final Map<String, GHRepository> repositories = new HashMap<>();

  /**
   * A configuration.
   */
  private Config config = new Config();

  /**
   * Initializes a new {@link GitHubProjectFinder}.
   *
   * @param github An interface to GitHub.
   */
  public GitHubProjectFinder(GitHub github) {
    this.github = Objects.requireNonNull(github, "Hey! GitHub interface can't be null!");
  }

  /**
   * Adds an organization to be scanned.
   *
   * @param name Organization's name.
   * @return The same {@link GitHubProjectFinder}.
   */
  public GitHubProjectFinder organization(String name) {
    config.organizationConfigs.add(new OrganizationConfig(name, EMPTY_EXCLUDE_LIST, DEFAULT_STARS));
    return this;
  }

  /**
   * Adds an organization to be scanned.
   *
   * @param name Organization's name.
   * @param excludeList A list of patterns to identify projects
   *                    which should be excluded from results of the scan.
   * @return The same {@link GitHubProjectFinder}.
   */
  public GitHubProjectFinder organization(String name, List<String> excludeList) {
    config.organizationConfigs.add(new OrganizationConfig(name, excludeList, DEFAULT_STARS));
    return this;
  }


  /**
   * Adds an organization to be scanned.
   *
   * @param organization The organization.
   * @return The same {@link GitHubProjectFinder}.
   */
  public GitHubProjectFinder add(GitHubOrganization organization) {
    config.organizationConfigs.add(
        new OrganizationConfig(organization.name(), EMPTY_EXCLUDE_LIST, DEFAULT_STARS));
    return this;
  }

  /**
   * Adds a project to be checked.
   * If the project exists, it will be added to results of the scan.
   *
   * @param project The project to be added.
   * @return The same {@link GitHubProjectFinder}.
   */
  public GitHubProjectFinder add(GitHubProject project) {
    config.projectConfigs.add(
        new ProjectConfig(project.organization().name(), project.name()));
    return this;
  }

  /**
   * Adds a project to be checked.
   * If the project exists, it will be added to results of the scan.
   *
   * @param organization Organization's name.
   * @param name Project's name.
   * @return The same {@link GitHubProjectFinder}.
   */
  public GitHubProjectFinder project(String organization, String name) {
    config.projectConfigs.add(
        new ProjectConfig(organization, name));
    return this;
  }

  /**
   * Sets a config.
   *
   * @param config The config to be used.
   * @return The same {@link GitHubProjectFinder}.
   */
  public GitHubProjectFinder set(Config config) {
    this.config = Objects.requireNonNull(config, "Oh no! Config can't be null!");
    return this;
  }

  /**
   * Loads a configuration form a file.
   *
   * @param filename The file name.
   * @return The same {@link GitHubProjectFinder}.
   * @throws IOException If something went wrong.
   */
  public GitHubProjectFinder config(String filename) throws IOException {
    this.config = new ConfigParser().parse(filename);
    return this;
  }

  /**
   * Search for projects on GitHub.
   *
   * @return A list of projects.
   * @throws IOException If something went wrong.
   */
  public List<GitHubProject> run() throws IOException {
    List<GitHubProject> projects = new ArrayList<>();
    for (OrganizationConfig organizationConfig : config.organizationConfigs) {
      projects.addAll(projectsIn(organizationConfig));
    }
    for (ProjectConfig projectConfig : config.projectConfigs) {
      projects.add(projectFrom(projectConfig));
    }
    return projects;
  }

  /**
   * Creates a {@link GHOrganization}.
   *
   * @param name Organization's name.
   * @return An instance of {@link GHOrganization}.
   * @throws IOException If the organization doesn't exist or something went wrong.
   */
  private GHOrganization githubOrganization(String name) throws IOException {
    GHOrganization organization = organizations.get(name);
    if (organization != null) {
      return organization;
    }
    organization = github.getOrganization(name);
    if (organization == null) {
      throw new IOException("Could not find an organization!");
    }
    organizations.put(name, organization);
    return organization;
  }

  /**
   * Scans the organization specified by a config and returns its projects.
   * The method uses {@link OrganizationConfig#excludeList} to exclude some projects.
   *
   * @param organizationConfig The config.
   * @return A list of projects.
   * @throws IOException If something went wrong.
   */
  private List<GitHubProject> projectsIn(OrganizationConfig organizationConfig) throws IOException {
    GHOrganization organization = githubOrganization(organizationConfig.name);
    List<GitHubProject> projects = new ArrayList<>();
    for (GHRepository repository : organization.listRepositories(PAGE_SIZE).asList()) {
      if (repository.getStargazersCount() < organizationConfig.stars) {
        continue;
      }
      String name = repository.getName();
      String path = String.format("%s/%s", organizationConfig.name, name);
      repositories.put(path, repository);
      if (organizationConfig.excluded(name)) {
        continue;
      }
      projects.add(new GitHubProject(
          new GitHubOrganization(organizationConfig.name), repository.getName()));
    }
    return projects;
  }

  /**
   * Create a {@link GHRepository}.
   *
   * @param path A path to the repository
   *             For example, apache/nifi is a path for https://github.com/apache/nifi repository.
   * @return An instance of {@link GHRepository}.
   * @throws IOException If the repository doesn't exist or something went wrong.
   */
  private GHRepository githubRepository(String path) throws IOException {
    GHRepository repository = repositories.get(path);
    if (repository != null) {
      return repository;
    }
    repository = github.getRepository(path);
    if (repository == null) {
      throw new IOException("Could not find a repository!");
    }
    repositories.put(path, repository);
    return repository;
  }

  /**
   * Checks if a repository exists.
   *
   * @param organization Organization's name.
   * @param name Project's name.
   * @throws IOException If the project doesn't exist or something went wrong.
   */
  private void checkRepository(String organization, String name) throws IOException {
    String path = String.format("%s/%s", organization, name);
    githubRepository(path);
  }

  /**
   * Make a project from a configuration.
   *
   * @param projectConfig The configuration.
   * @return The project if it exists.
   * @throws IOException If the project doesn't exist or something went wrong.
   */
  private GitHubProject projectFrom(ProjectConfig projectConfig) throws IOException {
    checkRepository(projectConfig.organization, projectConfig.name);
    return new GitHubProject(
        new GitHubOrganization(projectConfig.organization), projectConfig.name);
  }

  /**
   * A parser of configurations for {@link GitHubProjectFinder} which are stored in YAML.
   */
  static class ConfigParser {

    /**
     * Parse a configuration stored in a file.
     *
     * @param filename The file name.
     * @return A loaded configuration.
     * @throws IOException If something went wrong.
     */
    Config parse(String filename) throws IOException {
      try (InputStream is = Files.newInputStream(Paths.get(filename))) {
        return parse(is);
      }
    }

    /**
     * Parse a configuration from an input stream.
     *
     * @param is The input stream.
     * @return A loaded configuration.
     * @throws IOException If something went wrong.
     */
    Config parse(InputStream is) throws IOException {
      Objects.requireNonNull(is, "Input stream can't be null!");
      return Yaml.read(is, Config.class);
    }
  }

  /**
   * A configuration for a GitHub organization to be scanned.
   */
  static class OrganizationConfig {

    /**
     * Organization's name.
     */
    final String name;

    /**
     * A list of patterns that show which projects should be excluded.
     */
    final List<String> excludeList;

    /**
     * A minimal number of stars for a project.
     */
    final int stars;

    /**
     * Initializes a new config.
     *
     * @param name Organization's name.
     * @param excludeList A list of patterns that show which projects should be excluded.
     * @param stars A minimal number of stars for a project.
     */
    @JsonCreator
    OrganizationConfig(
        @JsonProperty("name") String name,
        @JsonProperty("exclude") List<String> excludeList,
        @JsonProperty(value = "stars", defaultValue = "0") int stars) {

      Objects.requireNonNull(name, "Hey! Name can't be null!");
      if (stars < 0) {
        throw new IllegalArgumentException("Hey! Stars can't be negative!");
      }

      this.name = name;
      this.excludeList = excludeList != null ? excludeList : EMPTY_EXCLUDE_LIST;
      this.stars = stars;
    }

    /**
     * Checks if a specified project should be excluded.
     *
     * @param name The project's name.
     * @return True if the project should be excluded, false otherwise.
     */
    boolean excluded(String name) {
      Objects.requireNonNull(name, "Hey! Name can't be null!");
      for (String string : excludeList) {
        if (name.contains(string)) {
          return true;
        }
      }
      return false;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o instanceof OrganizationConfig == false) {
        return false;
      }
      OrganizationConfig that = (OrganizationConfig) o;
      return stars == that.stars
          && Objects.equals(name, that.name)
          && Objects.equals(excludeList, that.excludeList);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, excludeList, stars);
    }
  }

  /**
   * A configuration for a GitHub project to be scanned.
   */
  static class ProjectConfig {

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
    ProjectConfig(
        @JsonProperty("organization") String organization,
        @JsonProperty("name") String name) {

      this.organization = Objects.requireNonNull(organization, "Hey! Organization can't be null!");
      this.name = Objects.requireNonNull(name, "Hey! Name can't be null!");
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
  }

  /**
   * A configuration for a the {@link GitHubProjectFinder}.
   */
  static class Config {

    /**
     * A list of configurations for GitHub organizations to be scanned.
     */
    final List<OrganizationConfig> organizationConfigs;

    /**
     * A list of configurations for GitHub projects to be scanned.
     */
    final List<ProjectConfig> projectConfigs;

    /**
     * Initializes an empty configuration.
     */
    Config() {
      this(new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Initializes a configuration.
     *
     * @param organizationConfigs A list of configurations for GitHub organizations to be scanned.
     * @param projectConfigs A list of configurations for GitHub projects to be scanned.
     */
    @JsonCreator
    Config(
        @JsonProperty("organizations") List<OrganizationConfig> organizationConfigs,
        @JsonProperty("repositories") List<ProjectConfig> projectConfigs) {

      this.organizationConfigs
          = organizationConfigs != null ? organizationConfigs : new ArrayList<>();
      this.projectConfigs
          = projectConfigs != null ? projectConfigs : new ArrayList<>();
    }
  }

}
