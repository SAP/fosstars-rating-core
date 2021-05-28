package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher.REPOSITORIES_BASE_PATH;
import static com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher.REPOSITORIES_BASE_PATH_PROPERTY;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * The class creates an instance of {@link GitHubDataFetcher} for tests.
 */
public class TestGitHubDataFetcherHolder {

  static {
    System.setProperty(REPOSITORIES_BASE_PATH_PROPERTY, ".fosstars/test_repositories");
  }

  /**
   * An instance of {@link GitHubDataFetcher} for tests.
   */
  protected TestGitHubDataFetcher fetcher;

  /**
   * Initializes a fetcher for each test case.
   *
   * @throws IOException If the initialization failed.
   */
  @Before
  public void init() throws IOException {
    fetcher = spy(new TestGitHubDataFetcher(mock(GitHub.class)));
  }

  /**
   * Does a clean up after each test case.
   *
   * @throws IOException If the cleanup failed.
   */
  @After
  public void cleanup() throws IOException {
    List<Path> deletedPaths = new ArrayList<>();
    fetcher.cleanup((url, repository, total) -> {
      deletedPaths.add(repository.path());
      return true;
    });

    for (Path deletedPath : deletedPaths) {
      assertFalse(Files.exists(deletedPath));
    }

    FileUtils.forceDeleteOnExit(REPOSITORIES_BASE_PATH.toFile());
  }

  public static class TestGitHubDataFetcher extends GitHubDataFetcher {

    /**
     * Test class constructor.
     */
    public TestGitHubDataFetcher(GitHub github) throws IOException {
      super(github, "test token");
    }

    /**
     * Adds {@link GitHubProject} and its {@link GHRepository repository on Github} to the cache.
     *
     * @param project The {@link GitHubProject}.
     * @param repository The {@link GHRepository repository on GitHub}.
     */
    void addForTesting(GitHubProject project, GHRepository repository) {
      repositoryCache().put(project, repository);
    }

    /**
     * Adds {@link GitHubProject} and its associated {@link LocalRepository} details to cache.
     *
     * @param project The {@link GitHubProject}.
     * @param repository The {@link LocalRepository}.
     */
    public static void addForTesting(GitHubProject project, LocalRepository repository) {
      LOCAL_REPOSITORIES.put(project.scm(), repository);
    }
    
    /**
     * Add a new project to be considered while loading repository.
     *  
     * @param project The {@link GitHubProject}.
     * @param projectDir The local {@link Path} for the {@link GitHubProject}.
     */
    static void addRepositoryInfoForTesting(GitHubProject project, Path projectDir) {
      LOCAL_REPOSITORIES.remove(project.scm());
      LOCAL_REPOSITORIES_INFO.put(project.scm(),
          new LocalRepositoryInfo(projectDir, Date.from(Instant.now()), project.scm()));
    }

    /**
     * Returns a resolved valid directory path of the project.
     *  
     * @param project of type {@link GitHubProject}.
     * @return path of type {@link Path}. 
     */
    static Path directoryFor(GitHubProject project) {
      return REPOSITORIES_BASE_PATH.resolve(project.name());
    }
  }
}