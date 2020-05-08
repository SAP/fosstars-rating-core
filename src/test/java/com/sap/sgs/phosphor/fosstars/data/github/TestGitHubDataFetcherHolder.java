package com.sap.sgs.phosphor.fosstars.data.github;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
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
  private Path path;

  /**
   * An instance of {@link GitHubDataFetcher} for tests.
   */
  protected TestGitHubDateFetcher fetcher;

  /**
   * Initializes a fetcher for each test case.
   *
   * @throws IOException If the initialization failed.
   */
  @Before
  public void init() throws IOException {
    GitHub github = mock(GitHub.class);
    path = Files.createTempDirectory(getClass().getName());
    fetcher = spy(new TestGitHubDateFetcher(github, path));
  }

  /**
   * Does a clean up after each test case.
   *
   * @throws IOException If the cleanup failed.
   */
  @After
  public void cleanup() throws IOException {
    List<Path> deletedPaths = new ArrayList<>();
    fetcher.cleanup(((url, repository, total) -> {
      deletedPaths.add(repository.path());
      return true;
    }));

    for (Path deletedPath : deletedPaths) {
      assertFalse(Files.exists(deletedPath));
    }

    FileUtils.forceDeleteOnExit(path.toFile());
  }

  public static class TestGitHubDateFetcher extends GitHubDataFetcher {

    public TestGitHubDateFetcher(GitHub github, Path base) throws IOException {
      super(github, base);
    }

    public void addForTesting(GitHubProject project, LocalRepository repository) {
      localRepositories.put(project, repository);
    }

    @Override
    protected void clone(GitHubProject project, Path base) {
      // do nothing
    }

    @Override
    protected Map<URL, LocalRepositoryInfo> loadLocalRepositoriesInfo() {
      return new HashMap<>();
    }

    @Override
    protected void storeLocalRepositoriesInfo() {
      // do nothing
    }
  }

}
