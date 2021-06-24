package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher.LOCAL_REPOSITORIES;
import static com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher.LOCAL_REPOSITORIES_CACHE_CAPACITY;
import static com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher.LOCAL_REPOSITORIES_INFO;
import static com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher.loadLocalRepositoriesInfo;
import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addRepositoryInfoForTesting;
import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.directoryFor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueBuilder;
import org.kohsuke.github.GHIssueSearchBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterator;
import org.kohsuke.github.PagedSearchIterable;

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
    GitHubDataFetcher.pullAfter(twoDays);
    Date threeDaysAgo = Date.from(Instant.now().minus(3, ChronoUnit.DAYS));

    Path path = Paths.get("test");

    Repository repository = mock(Repository.class);
    when(repository.getDirectory()).thenReturn(path.resolve(".git").toFile());

    LocalRepository outdatedRepository = new LocalRepository(
        new LocalRepositoryInfo(
            path, threeDaysAgo, new URL("https://scm/org/test")),
        repository);
    assertTrue(GitHubDataFetcher.shouldUpdate(outdatedRepository));

    LocalRepository freshRepository = new LocalRepository(
        new LocalRepositoryInfo(
            path, now, new URL("https://scm/org/test")),
        repository);
    assertFalse(GitHubDataFetcher.shouldUpdate(freshRepository));
  }

  @Test
  public void testUpdateGithubInstance() throws IOException {
    GitHub github = mock(GitHub.class);
    GitHubDataFetcher fetcher = new GitHubDataFetcher(github, "test token");
    assertEquals(fetcher.github(), github);

    // create the second GitHub mock bean
    GitHub anotherGithub = mock(GitHub.class);
    fetcher = new GitHubDataFetcher(anotherGithub, "test token");
    assertEquals(fetcher.github(), anotherGithub);
  }

  @Test
  public void testLoadAndCleanRepository() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    Path directory = directoryFor(project);
    try (Repository repository = FileRepositoryBuilder.create(directory.resolve(".git").toFile())) {
      repository.create();
      addRepositoryInfoForTesting(project, directory);
      testLocalRepositoryFor(project, 1);
      testCleanup(project, 0);
    }
  }

  @Test
  public void testCleanAndLoadRepository() throws IOException {
    GitHubProject project = new GitHubProject("test", "project");
    Path directory = directoryFor(project);
    try (Repository repository = FileRepositoryBuilder.create(directory.resolve(".git").toFile())) {
      repository.create();
      testCleanup(project, 0);
      addRepositoryInfoForTesting(project, directory);
      testLocalRepositoryFor(project, 1);
    }
  }

  @Test
  public void testMultiThreadReadAndUpdate() throws IOException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    GitHubProject firstProject = new GitHubProject("test", "one");
    GitHubProject secondProject = new GitHubProject("test", "two");

    Path firstDir = directoryFor(firstProject);
    try (Repository repository = FileRepositoryBuilder.create(firstDir.resolve(".git").toFile())) {
      repository.create();
    }

    Path secondDir = directoryFor(secondProject);
    try (Repository repository = FileRepositoryBuilder.create(secondDir.resolve(".git").toFile())) {
      repository.create();
    }

    try {
      addRepositoryInfoForTesting(firstProject, firstDir);

      CountDownLatch latch = new CountDownLatch(1);

      Runnable cleanTask = () -> {
        try {
          fetcher.cleanup((url, repo, total) -> true);
          checkCleanUp(firstProject, 0);
          latch.countDown();
        } catch (IOException e) {
          throw new IllegalStateException(e);
        }
      };

      Runnable loadRepoTask = () -> {
        try {
          latch.await();
          addRepositoryInfoForTesting(secondProject, secondDir);
          GitHubDataFetcher.localRepositoryFor(secondProject);
        } catch (InterruptedException | IOException e) {
          throw new IllegalStateException(e);
        }
      };

      executorService.execute(cleanTask);
      executorService.execute(loadRepoTask);
    } finally {
      executorService.awaitTermination(4, TimeUnit.SECONDS);
    }

    checkLocalRepository(secondProject, 1);
  }

  @Test
  public void testMultiThreadAccessToLocalRepoInfo() throws IOException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    GitHubProject project = new GitHubProject("test", "project");
    Path directory = directoryFor(project);

    try (Repository repository = FileRepositoryBuilder.create(directory.resolve(".git").toFile())) {
      repository.create();

      addRepositoryInfoForTesting(project, directory);

      int numberOfThreads = 25;
      CountDownLatch latch = new CountDownLatch(numberOfThreads);
      for (int i = 0; i < numberOfThreads; i++) {
        executorService.submit(() -> {
          try {
            GitHubDataFetcher.localRepositoryFor(project);
          } catch (IOException e) {
            throw new IllegalStateException(e);
          }
          latch.countDown();
        });
      }

      assertTrue(latch.await(5, TimeUnit.SECONDS));
    } finally {
      executorService.shutdown();
    }

    checkLocalRepository(project, 1);
  }

  @Test
  public void testMultiThreadCleanUpTest() throws IOException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    GitHubProject project = new GitHubProject("test", "project");
    Path directory = directoryFor(project);

    try (Repository repository = FileRepositoryBuilder.create(directory.resolve(".git").toFile())) {
      repository.create();

      addRepositoryInfoForTesting(project, directory);

      testLocalRepositoryFor(project, 1);

      int numberOfThreads = 25;
      CountDownLatch latch = new CountDownLatch(numberOfThreads);
      for (int i = 0; i < numberOfThreads; i++) {
        executorService.submit(() -> {
          try {
            runCleanupFor(project);
          } catch (IOException e) {
            throw new IllegalStateException("Clean up exception");
          }
          latch.countDown();
        });
      }

      assertTrue(latch.await(5, TimeUnit.SECONDS));
    } finally {
      executorService.shutdown();
    }

    checkCleanUp(project, 0);
  }

  @Test
  public void testWithMultipleProjects() throws IOException {
    GitHubProject firstProject = new GitHubProject("test", "one");
    Path firstDir = directoryFor(firstProject);
    try (Repository repository = FileRepositoryBuilder.create(firstDir.resolve(".git").toFile())) {
      repository.create();
    }
    addRepositoryInfoForTesting(firstProject, firstDir);
    testLocalRepositoryFor(firstProject, 1);

    GitHubProject secondProject = new GitHubProject("test", "two");
    Path secondDir = directoryFor(secondProject);
    try (Repository repository = FileRepositoryBuilder.create(secondDir.resolve(".git").toFile())) {
      repository.create();
    }
    addRepositoryInfoForTesting(secondProject, secondDir);
    testLocalRepositoryFor(secondProject, 2);

    testCleanup(firstProject, 1);
    testCleanup(secondProject, 0);
  }

  @Test
  public void testLocalRepositoriesCapacity() {
    List<GitHubProject> projects = generateProjectsForSize(LOCAL_REPOSITORIES_CACHE_CAPACITY + 10);
    projects.forEach(project -> addForTesting(project, mock(LocalRepository.class)));
    assertEquals(LOCAL_REPOSITORIES_CACHE_CAPACITY, LOCAL_REPOSITORIES.size());
    assertTrue(checkLocalRepositories(projects, LOCAL_REPOSITORIES));
  }
  
  @Test
  public void testCreateGitHubIssueExpectProject() throws IOException {
    try {
      fetcher.createGitHubIssue(null, "gugl", "hupf");
    } catch (NullPointerException npe) {
      assertEquals("Oh no! The project is null!", npe.getMessage());
    }    
  }
  
  @Test
  public void testCreateGitHubIssueExpectTitle() throws IOException {
    GitHubProject myProject = new GitHubProject("gugl", "hupf");
    try {
      fetcher.createGitHubIssue(myProject, null, "hupf");
    } catch (IllegalArgumentException iae) {
      assertEquals("Oh no! The issue title is invalid!", iae.getMessage());
    }
    try {
      fetcher.createGitHubIssue(myProject, StringUtils.EMPTY, "hupf");
    } catch (IllegalArgumentException iae) {
      assertEquals("Oh no! The issue title is invalid!", iae.getMessage());
    }
  }
  
  @Test
  public void testCreateGitHubIssueExpectBody() throws IOException {
    GitHubProject myProject = new GitHubProject("gugl", "hupf");
    try {
      fetcher.createGitHubIssue(myProject, "gugl", null);
    } catch (IllegalArgumentException iae) {
      assertEquals("Oh no! The issue body is invalid!", iae.getMessage());
    }
    try {
      fetcher.createGitHubIssue(myProject, "gugl", StringUtils.EMPTY);
    } catch (IllegalArgumentException iae) {
      assertEquals("Oh no! The issue body is invalid!", iae.getMessage());
    }
  }
  
  @Test
  public void testCreateGitHubIssue() throws IOException {
    GHRepository repository = mock(GHRepository.class);
    GHIssueBuilder issueBuilder = mock(GHIssueBuilder.class);
    GHIssue issue = mock(GHIssue.class);
    
    GitHubProject myProject = new GitHubProject("gugl", "hupf");
    when(fetcher.github().getRepository(any())).thenReturn(repository);
    when(repository.createIssue("apfel")).thenReturn(issueBuilder);
    when(issueBuilder.create()).thenReturn(issue);
    
    GHIssue createdIssue = fetcher.createGitHubIssue(myProject, "apfel", "kuchen");
    assertEquals(issue, createdIssue);
    verify(issueBuilder, times(1)).body("kuchen");    
  }
  
  @Test
  public void testGitHubIssuesForExpectProject() throws IOException {
    try {
      fetcher.gitHubIssuesFor(null, "gugl");
    } catch (NullPointerException npe) {
      assertEquals("Oh no! The project is null!", npe.getMessage());
    }    
  }
  
  @Test
  public void testGitHubIssuesForExpectQuery() throws IOException {
    GitHubProject myProject = new GitHubProject("gugl", "hupf");
    try {
      fetcher.gitHubIssuesFor(myProject, null);
    } catch (IllegalArgumentException iae) {
      assertEquals("Oh no! The search query is invalid!", iae.getMessage());
    }
    try {
      fetcher.gitHubIssuesFor(myProject, StringUtils.EMPTY);
    } catch (IllegalArgumentException iae) {
      assertEquals("Oh no! The search query is invalid!", iae.getMessage());
    }
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testGitHubIssuesForFound() throws IOException {
    GHIssueSearchBuilder issueSearchBuilder = mock(GHIssueSearchBuilder.class);
    GHIssue issue = mock(GHIssue.class);
    PagedSearchIterable<GHIssue> searchIterable = mock(PagedSearchIterable.class);
    PagedIterator<GHIssue> searchIterator = mock(PagedIterator.class);
    when(searchIterator.hasNext()).thenReturn(true, false);
    when(searchIterator.next()).thenReturn(issue);
    when(searchIterable.iterator()).thenReturn(searchIterator);
    when(issueSearchBuilder.list()).thenReturn(searchIterable);
    
    GitHubProject myProject = new GitHubProject("gugl", "hupf");
    when(issueSearchBuilder.isOpen()).thenReturn(issueSearchBuilder);
    when(issueSearchBuilder.q(any())).thenReturn(issueSearchBuilder);
    when(fetcher.github().searchIssues()).thenReturn(issueSearchBuilder);
    
    List<GHIssue> foundIssues = fetcher.gitHubIssuesFor(myProject, "apfel");
    assertNotNull(foundIssues);
    assertEquals(1, foundIssues.size());
    assertEquals(issue, foundIssues.get(0));    
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testGitHubIssuesForNotFound() throws IOException {
    GHIssueSearchBuilder issueSearchBuilder = mock(GHIssueSearchBuilder.class);
    PagedSearchIterable<GHIssue> searchIterable = mock(PagedSearchIterable.class);
    PagedIterator<GHIssue> searchIterator = mock(PagedIterator.class);
    when(searchIterator.hasNext()).thenReturn(false);
    when(searchIterable.iterator()).thenReturn(searchIterator);
    when(issueSearchBuilder.list()).thenReturn(searchIterable);
    
    GitHubProject myProject = new GitHubProject("gugl", "hupf");
    when(issueSearchBuilder.isOpen()).thenReturn(issueSearchBuilder);
    when(issueSearchBuilder.q(any())).thenReturn(issueSearchBuilder);
    when(fetcher.github().searchIssues()).thenReturn(issueSearchBuilder);
    
    List<GHIssue> foundIssues = fetcher.gitHubIssuesFor(myProject, "apfel");
    assertNotNull(foundIssues);
    assertEquals(0, foundIssues.size());   
  }

  private static List<GitHubProject> generateProjectsForSize(int n) {
    List<GitHubProject> projects = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      projects.add(new GitHubProject("test", String.format("project%s", i)));
    }
    return projects;
  }

  private static boolean checkLocalRepositories(
      List<GitHubProject> projects, Map<URL, LocalRepository> localRepositories) {

    Set<URL> projectUrls = projects.stream().map(GitHubProject::scm).collect(Collectors.toSet());
    assertEquals(projects.size(), projectUrls.size());

    return projectUrls.containsAll(localRepositories.keySet());
  }

  private void testLocalRepositoryFor(GitHubProject project, int expectedSize) throws IOException {
    GitHubDataFetcher.localRepositoryFor(project);
    checkLocalRepository(project, expectedSize);
  }

  private static void checkLocalRepository(GitHubProject project, int expectedSize)
      throws IOException {

    LocalRepositoryInfo localRepositoryInfo = localRepositoryInfoFor(project, expectedSize);
    assertNotNull(localRepositoryInfo);
    assertEquals(project.scm(), localRepositoryInfo.url());
  }

  private void testCleanup(GitHubProject project, int expectedSize) throws IOException {
    runCleanupFor(project);
    checkCleanUp(project, expectedSize);
  }

  private void runCleanupFor(GitHubProject project) throws IOException {
    fetcher.cleanup((url, repo, total) -> url.equals(project.scm()));
  }

  private static void checkCleanUp(GitHubProject project, int expectedSize) throws IOException {
    LocalRepositoryInfo cleanRepositoryInfo = localRepositoryInfoFor(project, expectedSize);
    assertNull(cleanRepositoryInfo);
    assertFalse(LOCAL_REPOSITORIES.containsKey(project.scm()));
  }

  private static LocalRepositoryInfo localRepositoryInfoFor(GitHubProject project, int expectedSize)
      throws IOException {

    loadLocalRepositoriesInfo();
    assertEquals(LOCAL_REPOSITORIES_INFO.size(), expectedSize);
    return LOCAL_REPOSITORIES_INFO.get(project.scm());
  }
}