package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.eclipse.jgit.lib.Repository;
import org.junit.Test;
import org.kohsuke.github.GHRepository;

public class GitHubDataFetcherTest extends TestGitHubDataFetcherHolder  {

  @Test
  public void testRepositoryCache() throws IOException {
    GHRepository repository = mock(GHRepository.class);
    GitHubProject firstProject = new GitHubProject("first", "project");
    when(fetcher.github().getRepository(any())).thenReturn(repository);

    GHRepository fetchedRepository = fetcher.repositoryFor(firstProject);
    assertEquals(1, fetcher.repositoryCache().size());
    assertEquals(repository, fetchedRepository);

    // fill out the cache for repositories
    int i = 0;
    while (fetcher.repositoryCache().size() < fetcher.repositoryCache().maxSize()) {
      GitHubProject project = new GitHubProject(
          String.format("org%d", i), String.format("project%d", i));
      fetcher.repositoryFor(project);
      i++;
    }

    assertEquals(fetcher.repositoryCache().maxSize(), fetcher.repositoryCache().size());
    assertNotNull(fetcher.repositoryFor(firstProject));

    // try to add one more
    i++;
    GitHubProject latestProject = new GitHubProject(
        String.format("org%d", i), String.format("project%d", i));
    fetcher.repositoryFor(latestProject);
    assertEquals(fetcher.repositoryCache().maxSize(), fetcher.repositoryCache().size());

    assertNotNull(fetcher.repositoryFor(latestProject));
  }

  @Test
  public void testShouldUpdate() throws IOException {
    Date now = Date.from(Instant.now());
    Duration twoDays = Duration.ofDays(2);
    fetcher.pullAfter(twoDays);
    Date threeDaysAgo = Date.from(Instant.now().minus(3, ChronoUnit.DAYS));

    Path path = Paths.get("test");

    Repository repository = mock(Repository.class);
    when(repository.getDirectory()).thenReturn(path.resolve(".git").toFile());

    LocalRepository outdatedRepository = new LocalRepository(
        new LocalRepositoryInfo(
            path, threeDaysAgo, new URL("https://scm/org/test")),
        repository);
    assertTrue(fetcher.shouldUpdate(outdatedRepository));

    LocalRepository freshRepository = new LocalRepository(
        new LocalRepositoryInfo(
            path, now, new URL("https://scm/org/test")),
        repository);
    assertFalse(fetcher.shouldUpdate(freshRepository));
  }
}