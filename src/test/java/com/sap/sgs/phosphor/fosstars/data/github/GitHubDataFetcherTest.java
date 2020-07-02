package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.GitHubDataFetcher.LOCAL_REPOSITORIES;
import static com.sap.sgs.phosphor.fosstars.data.github.GitHubDataFetcher.LOCAL_REPOSITORIES_CACHE_CAPACITY;
import static com.sap.sgs.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.directoryFor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

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

  @Test
  public void testUpdateGithubInstance() throws IOException {
    GitHub github = mock(GitHub.class);
    GitHubDataFetcher fetcher = new GitHubDataFetcher(github);
    assertEquals(fetcher.github(), github);

    // Create the second GitHub mock bean
    GitHub github2 = mock(GitHub.class);
    fetcher = new GitHubDataFetcher(github2);
    assertEquals(fetcher.github(), github2);
  }

  @Test
  public void testLoadAndCleanRepository() throws IOException, InterruptedException {
    GitHubProject project = new GitHubProject("test", "project");
    Path projectDir = directoryFor(project);
    try (
        Repository repository = FileRepositoryBuilder.create(projectDir.resolve(".git").toFile())) {
      repository.create();

      fetcher.addRepositoryInfoForTesting(project, projectDir);
      testLocalRepositoryFor(project, 1);
      testCleanup(project, 0);
    }
  }

  @Test
  public void testCleanAndLoadRepository() throws IOException, InterruptedException {
    GitHubProject project = new GitHubProject("test", "project");
    Path projectDir = directoryFor(project);
    try (
        Repository repository = FileRepositoryBuilder.create(projectDir.resolve(".git").toFile())) {
      repository.create();

      testCleanup(project, 0);
      fetcher.addRepositoryInfoForTesting(project, projectDir);
      testLocalRepositoryFor(project, 1);
    }
  }

  @Test
  public void testMultiThreadReadAndUpdate() throws IOException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    GitHubProject firstProject = new GitHubProject("test", "project");
    GitHubProject secondProject = new GitHubProject("test", "project2");
    Path project1Dir = directoryFor(firstProject);
    Path project2Dir = directoryFor(secondProject);

    try {
      try (Repository repository =
          FileRepositoryBuilder.create(project1Dir.resolve(".git").toFile())) {
        repository.create();
      }
      fetcher.addRepositoryInfoForTesting(firstProject, project1Dir);

      CountDownLatch latch = new CountDownLatch(1);
      Runnable cleanTask = () -> {
        try {
          fetcher.cleanup((url, repo, total) -> {
            latch.countDown();
            return true;
          });
          checkCleanUp(firstProject, 0);
        } catch (IOException e) {
          throw new IllegalStateException(e);
        }
      };

      Runnable loadRepoTask = () -> {
        try {
          latch.await();
          try (Repository repository =
              FileRepositoryBuilder.create(project2Dir.resolve(".git").toFile())) {
            repository.create();
          }
          fetcher.addRepositoryInfoForTesting(secondProject, project2Dir);
          fetcher.localRepositoryFor(secondProject);
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
  public void testMultiThreadLocalRepoInfoTest() throws IOException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    GitHubProject project = new GitHubProject("test", "project");
    Path projectDir = directoryFor(project);

    try (
        Repository repository = FileRepositoryBuilder.create(projectDir.resolve(".git").toFile())) {
      repository.create();
      fetcher.addRepositoryInfoForTesting(project, projectDir);

      int numberOfThreads = 25;
      CountDownLatch latch = new CountDownLatch(numberOfThreads);
      for (int i = 0; i < numberOfThreads; i++) {
        executorService.submit(() -> {
          try {
            fetcher.localRepositoryFor(project);
          } catch (IOException e) {
            throw new IllegalStateException(e);
          }
          latch.countDown();
        });
      }
      assertTrue(latch.await(5, TimeUnit.SECONDS));

      checkLocalRepository(project, 1);
    } finally {
      executorService.shutdown();
    }
  }

  @Test
  public void testMultiThreadCleanUpTest() throws IOException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    GitHubProject project = new GitHubProject("test", "project");
    Path projectDir = directoryFor(project);

    try (
        Repository repository = FileRepositoryBuilder.create(projectDir.resolve(".git").toFile())) {
      repository.create();
      fetcher.addRepositoryInfoForTesting(project, projectDir);
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

      checkCleanUp(project, 0);
    } finally {
      executorService.shutdown();
    }
  }

  @Test
  public void testMultipleInstanceFetcher() throws IOException {
    // Create the first project with first GitHub mock instance.
    GitHubProject firstProject = new GitHubProject("test", "project1");
    Path project1Dir = directoryFor(firstProject);
    GitHub github = mock(GitHub.class);
    try (Repository repository =
        FileRepositoryBuilder.create(project1Dir.resolve(".git").toFile())) {
      repository.create();

      fetcher = new TestGitHubDataFetcher(github);
      fetcher.addRepositoryInfoForTesting(firstProject, project1Dir);
      testLocalRepositoryFor(firstProject, 1);
    }

    // Create the second project with second GitHub mock instance.
    GitHubProject secondProject = new GitHubProject("test", "project2");
    Path project2Dir = directoryFor(secondProject);
    try (Repository repository =
        FileRepositoryBuilder.create(project2Dir.resolve(".git").toFile())) {
      repository.create();

      fetcher = new TestGitHubDataFetcher(github);
      fetcher.addRepositoryInfoForTesting(secondProject, project2Dir);
      testLocalRepositoryFor(secondProject, 2);
    }
  }

  @Test
  public void testLocalRepsitoriesCapacity() throws IOException {
    GitHub github = mock(GitHub.class);
    fetcher = new TestGitHubDataFetcher(github);
    for (int i = 0; i < 110; i++) {
      fetcher.addForTesting(mock(GitHubProject.class), mock(LocalRepository.class));
    }
    assertEquals(LOCAL_REPOSITORIES_CACHE_CAPACITY, LOCAL_REPOSITORIES.size());
  }

  private void testLocalRepositoryFor(GitHubProject project, int expectedSize) throws IOException {
    fetcher.localRepositoryFor(project);
    checkLocalRepository(project, expectedSize);
  }

  private void checkLocalRepository(GitHubProject project, int expectedSize) throws IOException {
    Map<URL, LocalRepositoryInfo> repositories = fetcher.getLocalRespositoriesInfoForTesting();
    assertTrue(repositories.size() == expectedSize);
    LocalRepositoryInfo localRepositoryInfo = repositories.get(project.url());
    assertNotNull(localRepositoryInfo);
    assertEquals(project.url(), localRepositoryInfo.url());
  }

  private void testCleanup(GitHubProject project, int expectedSize) throws IOException {
    runCleanupFor(project);
    checkCleanUp(project, expectedSize);
  }

  private void runCleanupFor(GitHubProject project) throws IOException {
    fetcher.cleanup((url, repo, total) -> {
      return url.equals(project.url());
    });
  }

  private void checkCleanUp(GitHubProject project, int expectedSize) throws IOException {
    Map<URL, LocalRepositoryInfo> localRepositories = fetcher.getLocalRespositoriesInfoForTesting();
    assertTrue(localRepositories.size() == expectedSize);
    LocalRepositoryInfo cleanRepositoryInfo = localRepositories.get(project.url());
    assertNull(cleanRepositoryInfo);
  }
}