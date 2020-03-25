package com.sap.sgs.phosphor.fosstars.tool.github;

import static com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.EMPTY_EXCLUDE_LIST;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.Config;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.ConfigParser;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.OrganizationConfig;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectFinder.ProjectConfig;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

public class GitHubProjectFinderTest {

  @Test
  public void parseValidConfig() throws IOException {
    ConfigParser parser = new ConfigParser();
    try (InputStream is = getClass().getResourceAsStream("ValidProjectFinderConfig.yml")) {
      Config config = parser.parse(is);
      assertNotNull(config);
      assertNotNull(config.organizationConfigs);
      assertEquals(3, config.organizationConfigs.size());
      assertThat(
          config.organizationConfigs,
          hasItem(
              new OrganizationConfig("apache", Arrays.asList("incubator", "incubating"))));
      assertThat(config.organizationConfigs,
          hasItem(
              new OrganizationConfig("eclipse", Collections.singletonList("incubator"))));
      assertThat(config.organizationConfigs,
          hasItem(
              new OrganizationConfig("spring-projects", EMPTY_EXCLUDE_LIST)));
      assertNotNull(config.projectConfigs);
      assertEquals(2, config.projectConfigs.size());
      assertThat(
          config.projectConfigs,
          hasItem(new ProjectConfig("FasterXML", "jackson-databind")));
      assertThat(
          config.projectConfigs,
          hasItem(new ProjectConfig("FasterXML", "jackson-dataformat-xml")));
    }
  }

  @Test
  public void browseOrganization() throws IOException {
    final String apache = "apache";
    final String eclipse = "eclipse";

    final GHRepository projectX = mockRepository("project-x");
    final GHRepository projectY = mockRepository("project-y");
    final GHRepository projectZ = mockRepository("project-z-incubator");
    final GHRepository extraProject = mockRepository("extra");

    List<GHRepository> repositories = new ArrayList<>();
    repositories.add(projectX);
    repositories.add(projectY);
    repositories.add(projectZ);

    PagedIterable<GHRepository> pagedIterable = mock(PagedIterable.class);
    when(pagedIterable.asList()).thenReturn(repositories);

    GHOrganization organization = mock(GHOrganization.class);
    when(organization.listRepositories(anyInt())).thenReturn(pagedIterable);

    GitHub github = mock(GitHub.class);
    when(github.getOrganization(apache)).thenReturn(organization);
    when(github.getRepository("eclipse/extra")).thenReturn(extraProject);

    GitHubProjectFinder finder = new GitHubProjectFinder(github);
    finder.project(eclipse, "extra");
    finder.organization(apache, Arrays.asList("incubator", "incubating"));
    List<GitHubProject> projects = finder.run();

    assertEquals(3, projects.size());
    assertThat(
        projects,
        hasItem(new GitHubProject(new GitHubOrganization(apache), "project-x")));
    assertThat(
        projects,
        hasItem(new GitHubProject(new GitHubOrganization(apache), "project-y")));
    assertThat(
        projects,
        hasItem(new GitHubProject(new GitHubOrganization(eclipse), "extra")));
  }

  private static GHRepository mockRepository(String name) {
    GHRepository repository = mock(GHRepository.class);
    when(repository.getName()).thenReturn(name);
    return repository;
  }

}