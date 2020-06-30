package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
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
import java.util.concurrent.ScheduledExecutorService;
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
    fetcher = spy(new TestGitHubDataFetcher(github, path));
    assertEquals(fetcher.github(), github);

    // Create the second GitHub mock bean
    GitHub github2 = mock(GitHub.class);
    fetcher = spy(new TestGitHubDataFetcher(github2, path));
    assertEquals(fetcher.github(), github2);
  }

  @Test
  public void testLoadAndCleanRepository() throws IOException, InterruptedException {
    GitHubProject project = new GitHubProject("test", "project");
    Path projectDir = path.resolve(project.name());
    try (
        Repository repository = FileRepositoryBuilder.create(projectDir.resolve(".git").toFile())) {
      repository.create();

      createRepoTestFile(repository, project, "testfile");

      fetcher.addRepositoryInfoForTesting(project, projectDir);

      localRepositoryFor(project, 1);

      cleanup(project, 0);
    }
  }

  @Test
  public void testCleanAndLoadRepository() throws IOException, InterruptedException {
    GitHubProject project = new GitHubProject("test", "project");
    Path projectDir = path.resolve(project.name());
    try (
        Repository repository = FileRepositoryBuilder.create(projectDir.resolve(".git").toFile())) {
      repository.create();

      createRepoTestFile(repository, project, "testFile");

      cleanup(project, 0);

      fetcher.addRepositoryInfoForTesting(project, projectDir);

      localRepositoryFor(project, 1);
    }
  }

  @Test
  public void testMultiGithubInstanceAndAccessRepo() throws IOException {
    // Create the first project with first GitHub mock instance.
    GitHubProject firstProject = new GitHubProject("test", "project1");
    Path project1Dir = path.resolve(firstProject.name());
    GitHub github = mock(GitHub.class);
    try (Repository repository =
        FileRepositoryBuilder.create(project1Dir.resolve(".git").toFile())) {
      repository.create();

      createRepoTestFile(repository, firstProject, "testFile1");


      fetcher = spy(new TestGitHubDataFetcher(github, path));

      fetcher.addRepositoryInfoForTesting(firstProject, project1Dir);

      localRepositoryFor(firstProject, 1);
      assertEquals(fetcher.github(), github);
    }

    // Create the second project with second GitHub mock instance.
    GitHubProject secondProject = new GitHubProject("test", "project2");
    Path project2Dir = path.resolve(secondProject.name());
    GitHub github2 = mock(GitHub.class);
    try (Repository repository =
        FileRepositoryBuilder.create(project2Dir.resolve(".git").toFile())) {
      repository.create();

      createRepoTestFile(repository, secondProject, "testFile2");

      fetcher = spy(new TestGitHubDataFetcher(github2, path));

      fetcher.addRepositoryInfoForTesting(secondProject, project2Dir);

      localRepositoryFor(secondProject, 2);
      assertEquals(fetcher.github(), github2);
    }

    // Create the third project with second GitHub mock instance.
    GitHubProject thirdProject = new GitHubProject("test", "project3");

    Path project3Dir = path.resolve(thirdProject.name());
    try (Repository repository =
        FileRepositoryBuilder.create(project3Dir.resolve(".git").toFile())) {
      repository.create();
      createRepoTestFile(repository, thirdProject, "testFile3");

      fetcher.addRepositoryInfoForTesting(thirdProject, project3Dir);

      localRepositoryFor(thirdProject, 3);
      assertEquals(fetcher.github(), github2);
    }
  }

  @Test
  public void testMultiThreadReadAndUpdate() throws IOException, InterruptedException {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    GitHubProject firstProject = new GitHubProject("test", "project");
    Path project1Dir = path.resolve(firstProject.name());
    try (Repository repository =
        FileRepositoryBuilder.create(project1Dir.resolve(".git").toFile())) {
      repository.create();

      createRepoTestFile(repository, firstProject, "testFile");

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
          throw new IllegalStateException("Clean up exception");
        }
      };

      GitHubProject secondProject = new GitHubProject("test", "project2");

      Runnable loadRepoTask = () -> {
        try {
          assertTrue(latch.await(10, TimeUnit.SECONDS));
          fetcher.localRepositoryFor(secondProject);
          checkLocalRepository(secondProject, 1);
        } catch (InterruptedException | IOException e) {
          throw new IllegalStateException("Load repository exception");
        }
      };

      executorService.execute(cleanTask);
      executorService.execute(loadRepoTask);
    } finally {
      executorService.shutdown();
    }
  }

  @Test
  public void testMultiThreadLocalRepoInfoTest() throws IOException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    GitHubProject project = new GitHubProject("test", "project");
    Path projectDir = path.resolve(project.name());
    try (
        Repository repository = FileRepositoryBuilder.create(projectDir.resolve(".git").toFile())) {
      repository.create();

      createRepoTestFile(repository, project, "testFile");

      fetcher.addRepositoryInfoForTesting(project, projectDir);

      int numberOfThreads = 25;
      CountDownLatch latch = new CountDownLatch(numberOfThreads);
      for (int i = 0; i < numberOfThreads; i++) {
        executorService.submit(() -> {
          try {
            localRepositoryFor(project);
          } catch (IOException e) {
            throw new IllegalStateException("Load repository exception");
          }
          latch.countDown();
        });
      }
      latch.await();

      checkLocalRepository(project, 1);
    } finally {
      executorService.shutdown();
    }
  }

  @Test
  public void testMultiThreadCleanUpTest() throws IOException, InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    GitHubProject project = new GitHubProject("test", "project");
    Path projectDir = path.resolve(project.name());
    try (
        Repository repository = FileRepositoryBuilder.create(projectDir.resolve(".git").toFile())) {
      repository.create();

      createRepoTestFile(repository, project, "testFile");

      fetcher.addRepositoryInfoForTesting(project, projectDir);

      localRepositoryFor(project, 1);

      int numberOfThreads = 25;
      CountDownLatch latch = new CountDownLatch(numberOfThreads);
      for (int i = 0; i < numberOfThreads; i++) {
        executorService.submit(() -> {
          try {
            cleanup(project);
          } catch (IOException e) {
            throw new IllegalStateException("Clean up exception");
          }
          latch.countDown();
        });
      }
      latch.await();

      checkCleanUp(project, 0);
    } finally {
      executorService.shutdown();
    }
  }

  @Test
  public void testLocalRepositoriesInfoJson() throws IOException {
    // Create the first project with first GitHub mock instance.
    GitHubProject firstProject = new GitHubProject("test", "project1");
    Path project1Dir = path.resolve(firstProject.name());
    GitHub github = mock(GitHub.class);
    try (Repository repository =
        FileRepositoryBuilder.create(project1Dir.resolve(".git").toFile())) {
      repository.create();

      createRepoTestFile(repository, firstProject, "testFile1");

      fetcher = spy(new TestGitHubDataFetcher(github, path));

      fetcher.addRepositoryInfoForTesting(firstProject, project1Dir);

      localRepositoryFor(firstProject, 1);
    }

    // Create the second project with second GitHub mock instance.
    GitHubProject secondProject = new GitHubProject("test", "project2");
    Path project2Dir = path.resolve(secondProject.name());
    try (Repository repository =
        FileRepositoryBuilder.create(project2Dir.resolve(".git").toFile())) {
      repository.create();

      createRepoTestFile(repository, secondProject, "testFile2");

      fetcher.addRepositoryInfoForTesting(secondProject, project2Dir);

      localRepositoryFor(secondProject, 2);
    }

    cleanup(firstProject, 1);
    cleanup(secondProject, 0);
  }

  private void localRepositoryFor(GitHubProject project, int expectedSize) throws IOException {
    localRepositoryFor(project);
    checkLocalRepository(project, expectedSize);
  }

  private void localRepositoryFor(GitHubProject project) throws IOException {
    fetcher.localRepositoryFor(project);
  }

  private void checkLocalRepository(GitHubProject project, int expectedSize) throws IOException {
    Map<URL, LocalRepositoryInfo> repositories = fetcher.getLocalRespositoriesInfoForTesting();
    assertTrue(repositories.size() == expectedSize);
    LocalRepositoryInfo localRepositoryInfo = repositories.get(project.url());
    assertNotNull(localRepositoryInfo);
    assertEquals(project.url(), localRepositoryInfo.url());
  }

  private void cleanup(GitHubProject project, int expectedSize) throws IOException {
    cleanup(project);
    checkCleanUp(project, expectedSize);
  }

  private void cleanup(GitHubProject project) throws IOException {
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

  private void createRepoTestFile(Repository repository, GitHubProject project, String fileName)
      throws IOException {
    Path testFilePath = new File(repository.getDirectory().getParent(), fileName).toPath();
    String content = String.format("git clone %s", project.url());
    Files.write(testFilePath, content.getBytes());
  }
}