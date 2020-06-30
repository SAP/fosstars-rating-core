package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.kohsuke.github.GitHub;

/**
 * The class creates an instance of {@link GitHubDataFetcher} for tests.
 */
public class TestGitHubDataFetcherHolder {

  /**
   * A base directory for {@link GitHubDataFetcher}.
   */
  protected Path path;

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
    path = Files.createTempDirectory(getClass().getName());
    fetcher = spy(new TestGitHubDataFetcher(github, path));
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

    FileUtils.forceDeleteOnExit(path.toFile());
  }

  public static class TestGitHubDataFetcher extends GitHubDataFetcher {

    /**
     * Test class constructor.
     */
    public TestGitHubDataFetcher(GitHub github, Path base) throws IOException {
      super(github, base);
    }

    /**
     * Adds {@link GitHubProject} and its associated {@link LocalRepository} details to cache.
     * @param project The {@link GitHubProject}.
     * @param repository The {@link LocalRepository}.
     */
    public void addForTesting(GitHubProject project, LocalRepository repository) {
      LOCAL_REPOSITORIES.put(project, repository);
    }

    /**
     * Returns the current list of local repositories stored in the base folder.
     *
     * @return Map of local repositories available.
     * @throws IOException if something goes wrong.
     */
    public Map<URL, LocalRepositoryInfo> getLocalRespositoriesInfoForTesting() throws IOException {
      return LOCAL_REPOSITORIES_INFO;
    }
    
    /**
     * Add new project to be considered while loading repository.
     *  
     * @param project The {@link GitHubProject}.
     * @param projectDir The local {@link Path} of for the {@link GitHubProject}.
     */
    public void addRepositoryInfoForTesting(GitHubProject project, Path projectDir) {
      LOCAL_REPOSITORIES.remove(project);
      LOCAL_REPOSITORIES_INFO.put(project.url(),
          new LocalRepositoryInfo(projectDir, Date.from(Instant.now()), project.url()));
    }

    @Override
    protected void clone(GitHubProject project, Path base) {
      // do nothing
    }
  }
}