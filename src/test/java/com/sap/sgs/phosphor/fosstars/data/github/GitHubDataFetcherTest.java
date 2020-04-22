package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import org.junit.Test;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class GitHubDataFetcherTest {

  @Test
  public void repositoryCache() throws IOException {
    GitHub github = mock(GitHub.class);

    GitHubDataFetcher fetcher = GitHubDataFetcher.instance();
    fetcher.repositoryCache().clear();

    GHRepository repository = mock(GHRepository.class);
    GitHubProject firstProject = new GitHubProject("first", "project");
    when(github.getRepository(any())).thenReturn(repository);

    GHRepository fetchedRepository = fetcher.repositoryFor(firstProject, github);
    assertEquals(1, fetcher.repositoryCache().size());
    assertEquals(repository, fetchedRepository);

    // fill out the cache for repositories
    int i = 0;
    while (fetcher.repositoryCache().size() < fetcher.repositoryCache().maxSize()) {
      GitHubProject project = new GitHubProject(
          String.format("org%d", i), String.format("project%d", i));
      fetcher.repositoryFor(project, github);
      i++;
    }

    assertEquals(fetcher.repositoryCache().maxSize(), fetcher.repositoryCache().size());
    assertNotNull(fetcher.repositoryFor(firstProject, github));

    // try to add one more
    i++;
    GitHubProject latestProject = new GitHubProject(
        String.format("org%d", i), String.format("project%d", i));
    fetcher.repositoryFor(latestProject, github);
    assertEquals(fetcher.repositoryCache().maxSize(), fetcher.repositoryCache().size());

    assertNotNull(fetcher.repositoryFor(latestProject, github));
  }
}