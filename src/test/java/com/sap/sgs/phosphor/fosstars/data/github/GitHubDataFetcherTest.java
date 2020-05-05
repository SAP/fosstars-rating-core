package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.TestGitHubDataFetcherHolder;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterable;

public class GitHubDataFetcherTest extends TestGitHubDataFetcherHolder  {

  @Test
  public void testCommitsOrder() throws IOException {
    List<GHCommit> commits = new ArrayList<>();

    GHCommit firstCommit = mock(GHCommit.class);
    when(firstCommit.getCommitDate())
        .thenReturn(Date.from(Instant.now().minus(300, ChronoUnit.DAYS)));
    commits.add(firstCommit);

    for (int i = 10; i < 100; i++) {
      GHCommit commit = mock(GHCommit.class);
      when(commit.getCommitDate())
          .thenReturn(Date.from(Instant.now().minus(i, ChronoUnit.DAYS)));
      commits.add(commit);
    }

    GHCommit latestCommit = mock(GHCommit.class);
    when(latestCommit.getCommitDate())
        .thenReturn(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)));
    commits.add(latestCommit);

    Collections.shuffle(commits);

    PagedIterable<GHCommit> pagedIterable = mock(PagedIterable.class);
    when(pagedIterable.toList()).thenReturn(commits);

    GHRepository repository = mock(GHRepository.class);
    when(repository.listCommits()).thenReturn(pagedIterable);

    GitHubProject project = new GitHubProject("first", "project");
    when(fetcher.github().getRepository(any())).thenReturn(repository);

    List<GHCommit> fetchedCommits = fetcher.commitsFor(project);

    assertTrue(fetcher.firstCommitFor(project).isPresent());
    assertEquals(
        firstCommit.getCommitDate(),
        fetcher.firstCommitFor(project).get().getCommitDate());

    // make sure that all commits are sorted
    Date previous = null;
    for (GHCommit commit : fetchedCommits) {
      if (previous == null) {
        previous = commit.getCommitDate();
      } else {
        Date current = commit.getCommitDate();
        assertTrue(current.before(previous));
        previous = current;
      }
    }

    assertEquals(
        latestCommit.getCommitDate(),
        fetchedCommits.get(0).getCommitDate());
    assertEquals(
        firstCommit.getCommitDate(),
        fetchedCommits.get(fetchedCommits.size() - 1).getCommitDate());
  }

  @Test
  public void testCommitsCache() throws IOException {
    List<GHCommit> commits = new ArrayList<>();
    for (int i = 10; i < 100; i++) {
      GHCommit commit = mock(GHCommit.class);
      when(commit.getCommitDate())
          .thenReturn(Date.from(Instant.now().minus(i, ChronoUnit.DAYS)));
      commits.add(commit);
    }

    PagedIterable<GHCommit> pagedIterable = mock(PagedIterable.class);
    when(pagedIterable.toList()).thenReturn(commits);

    GHRepository repository = mock(GHRepository.class);
    when(repository.listCommits()).thenReturn(pagedIterable);

    GitHubProject project = new GitHubProject("first", "project");
    when(fetcher.github().getRepository(any())).thenReturn(repository);

    List<GHCommit> fetchedCommits = fetcher.commitsFor(project);
    assertEquals(1, fetcher.commitsCache().size());
    assertEquals(commits.size(), fetchedCommits.size());

    // fill out the cache for commits
    int i = 0;
    while (fetcher.commitsCache().size() < fetcher.commitsCache().maxSize()) {
      project = new GitHubProject(
          String.format("org%d", i), String.format("project%d", i));
      fetcher.commitsFor(project);
      i++;
    }

    assertEquals(fetcher.commitsCache().maxSize(), fetcher.commitsCache().size());
    assertNotNull(fetcher.repositoryFor(project));

    // try to add one more
    i++;
    GitHubProject latestProject = new GitHubProject(
        String.format("org%d", i), String.format("project%d", i));
    fetcher.repositoryFor(latestProject);
    assertEquals(fetcher.commitsCache().maxSize(), fetcher.commitsCache().size());

    assertNotNull(fetcher.commitsFor(latestProject));
  }

  @Test
  public void testCommitsAfter() throws IOException {
    List<GHCommit> commits = new ArrayList<>();
    for (int i = 10; i < 100; i++) {
      GHCommit commit = mock(GHCommit.class);
      when(commit.getCommitDate())
          .thenReturn(Date.from(Instant.now().minus(i, ChronoUnit.DAYS)));
      commits.add(commit);
    }

    PagedIterable<GHCommit> pagedIterable = mock(PagedIterable.class);
    when(pagedIterable.toList()).thenReturn(commits);

    GHRepository repository = mock(GHRepository.class);
    when(repository.listCommits()).thenReturn(pagedIterable);

    GitHubProject project = new GitHubProject("first", "project");
    when(fetcher.github().getRepository(any())).thenReturn(repository);

    Date date = Date.from(Instant.now().minus(50, ChronoUnit.DAYS));
    List<GHCommit> fetchedCommits = fetcher.commitsAfter(date, project);
    assertEquals(1, fetcher.commitsCache().size());
    for (GHCommit fetchedCommit : fetchedCommits) {
      assertTrue(fetchedCommit.getCommitDate().after(date));
    }
  }

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
}