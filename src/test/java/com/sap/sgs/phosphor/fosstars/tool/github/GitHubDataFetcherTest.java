package com.sap.sgs.phosphor.fosstars.tool.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import org.junit.Test;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class GitHubDataFetcherTest {

  @Test
  public void repositoryCache() throws IOException {
    GitHub github = mock(GitHub.class);

    GitHubDataFetcher gitHubDataFetcher = gitHubDataFetcher();
    gitHubDataFetcher.repositoryCache.clear();

    GHRepository expectedRepo = mock(GHRepository.class);
    GitHubProject project = mock(GitHubProject.class);
    when(github.getRepository(any())).thenReturn(expectedRepo);

    GHRepository actualRepo1 = gitHubDataFetcher.repositoryFor(project, github);
    assertNotNull(actualRepo1);
    assertTrue(gitHubDataFetcher.repositoryCache.size() == 1);
    assertEquals(expectedRepo, actualRepo1);
    
    GHRepository actualRepo2 = gitHubDataFetcher.repositoryFor(project, github);
    assertNotNull(actualRepo2);
    assertTrue(gitHubDataFetcher.repositoryCache.size() == 1);
    assertEquals(expectedRepo, actualRepo2);

    GitHubProject project3 = mock(GitHubProject.class);
    GHRepository actualRepo3 = gitHubDataFetcher.repositoryFor(project3, github);
    assertNotNull(actualRepo3);
    assertTrue(gitHubDataFetcher.repositoryCache.size() == 2);

    GitHubProject project4 = mock(GitHubProject.class);
    GHRepository actualRepo4 = gitHubDataFetcher.repositoryFor(project4, github);
    assertNotNull(actualRepo4);
    assertTrue(gitHubDataFetcher.repositoryCache.size() == 3);

    GitHubProject project5 = mock(GitHubProject.class);
    GHRepository actualRepo5 = gitHubDataFetcher.repositoryFor(project5, github);
    assertNotNull(actualRepo5);
    assertTrue(gitHubDataFetcher.repositoryCache.size() == 4);

    GitHubProject project6 = mock(GitHubProject.class);
    GHRepository actualRepo6 = gitHubDataFetcher.repositoryFor(project6, github);
    assertNotNull(actualRepo6);
    assertTrue(gitHubDataFetcher.repositoryCache.size() == 5);

    GitHubProject project7 = mock(GitHubProject.class);
    GHRepository actualRepo7 = gitHubDataFetcher.repositoryFor(project7, github);
    assertNotNull(actualRepo7);
    assertTrue(gitHubDataFetcher.repositoryCache.size() == 5);
  }

  private GitHubDataFetcher gitHubDataFetcher() {
    return GitHubDataFetcher.instance();
  }
}