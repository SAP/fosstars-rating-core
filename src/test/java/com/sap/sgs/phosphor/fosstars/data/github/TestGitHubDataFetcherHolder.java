package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.GitHubDataFetcher.DEFAULT_DIRECTORY;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
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
import org.kohsuke.github.GitHub;

/**
 * The class creates an instance of {@link GitHubDataFetcher} for tests.
 */
public class TestGitHubDataFetcherHolder {

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
    GitHub github = mock(GitHub.class);
    fetcher = spy(new TestGitHubDataFetcher(github));
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

    FileUtils.forceDeleteOnExit(DEFAULT_DIRECTORY.toFile());
  }

  public static class TestGitHubDataFetcher extends GitHubDataFetcher {

    /**
     * Test class constructor.
     */
    public TestGitHubDataFetcher(GitHub github) throws IOException {
      super(github);
    }

    /**
     * Adds {@link GitHubProject} and its associated {@link LocalRepository} details to cache.
     * @param project The {@link GitHubProject}.
     * @param repository The {@link LocalRepository}.
     */
    static void addForTesting(GitHubProject project, LocalRepository repository) {
      LOCAL_REPOSITORIES.put(project, repository);
    }
    
    /**
     * Add new project to be considered while loading repository.
     *  
     * @param project The {@link GitHubProject}.
     * @param projectDir The local {@link Path} for the {@link GitHubProject}.
     */
    static void addRepositoryInfoForTesting(GitHubProject project, Path projectDir) {
      LOCAL_REPOSITORIES.remove(project);
      LOCAL_REPOSITORIES_INFO.put(project.url(),
          new LocalRepositoryInfo(projectDir, Date.from(Instant.now()), project.url()));
    }

    @Override
    protected void clone(GitHubProject project, Path base) {
      // do nothing
    }

    /**
     * Returns a resolved valid directory path of the project.
     *  
     * @param project of type {@link GitHubProject}.
     * @return path of type {@link Path}. 
     */
    static Path directoryFor(GitHubProject project) {
      return DEFAULT_DIRECTORY.resolve(project.name());
    }
  }
}