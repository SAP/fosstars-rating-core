package com.sap.oss.phosphor.fosstars.tool;

import static com.sap.oss.phosphor.fosstars.tool.finder.OrganizationConfig.EMPTY_EXCLUDE_LIST;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubOrganization;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.tool.finder.AbstractEntityFinder;
import com.sap.oss.phosphor.fosstars.tool.finder.Finder;
import com.sap.oss.phosphor.fosstars.tool.finder.FinderConfig;
import com.sap.oss.phosphor.fosstars.tool.finder.OrganizationConfig;
import com.sap.oss.phosphor.fosstars.tool.finder.ProjectConfig;
import java.io.IOException;
import java.util.ArrayList;
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
public class GitHubProjectFinder extends AbstractEntityFinder<GitHubProject> {

  /**
   * The default minimal number of stars.
   */
  private static final int DEFAULT_STARS = 0;

  /**
   * A page size for requests to GitHub.
   */
  private static final int PAGE_SIZE = 100;

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
    config.organizationConfigs()
        .add(new OrganizationConfig(name, EMPTY_EXCLUDE_LIST, DEFAULT_STARS));
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
    config.organizationConfigs().add(new OrganizationConfig(name, excludeList, DEFAULT_STARS));
    return this;
  }

  /**
   * Adds an organization to be scanned.
   *
   * @param organization The organization.
   * @return The same {@link GitHubProjectFinder}.
   */
  public GitHubProjectFinder add(GitHubOrganization organization) {
    config.organizationConfigs().add(
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
    config.projectConfigs().add(
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
    config.projectConfigs().add(
        new ProjectConfig(organization, name));
    return this;
  }

  /**
   * Sets a config.
   *
   * @param config The config to be used.
   * @return The same {@link GitHubProjectFinder}.
   */
  @Override
  public Finder set(FinderConfig config) {
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
  @Override
  public List<GitHubProject> run() throws IOException {
    List<GitHubProject> projects = new ArrayList<>();
    for (OrganizationConfig organizationConfig : config.organizationConfigs()) {
      projects.addAll(projectsIn(organizationConfig));
    }
    for (ProjectConfig projectConfig : config.projectConfigs()) {
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
   * The method uses {@link OrganizationConfig#excludeList()} to exclude some projects.
   *
   * @param organizationConfig The config.
   * @return A list of projects.
   * @throws IOException If something went wrong.
   */
  private List<GitHubProject> projectsIn(OrganizationConfig organizationConfig) throws IOException {
    GHOrganization organization = githubOrganization(organizationConfig.name());
    List<GitHubProject> projects = new ArrayList<>();
    for (GHRepository repository : organization.listRepositories(PAGE_SIZE).toList()) {
      if (repository.getStargazersCount() < organizationConfig.stars()) {
        continue;
      }
      String name = repository.getName();
      String path = String.format("%s/%s", organizationConfig.name(), name);
      repositories.put(path, repository);
      if (organizationConfig.excluded(name)) {
        continue;
      }
      projects.add(new GitHubProject(
          new GitHubOrganization(organizationConfig.name()), repository.getName()));
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
    checkRepository(projectConfig.organization(), projectConfig.name());
    return new GitHubProject(
        new GitHubOrganization(projectConfig.organization()), projectConfig.name());
  }
}