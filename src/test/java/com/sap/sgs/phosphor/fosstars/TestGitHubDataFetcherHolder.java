package com.sap.sgs.phosphor.fosstars;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.sap.sgs.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.sgs.phosphor.fosstars.data.github.LocalRepository;
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
import org.eclipse.jgit.lib.Repository;
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

    FileUtils.deleteDirectory(path.toFile());
    assertFalse(Files.exists(path));
  }

  public static class TestGitHubDateFetcher extends GitHubDataFetcher {

    public TestGitHubDateFetcher(GitHub github, Path base) throws IOException {
      super(github, base);
    }

    @Override
    protected LocalRepository clone(GitHubProject project, Path base) {
      return spy(new LocalRepository(
          base.resolve(project.organization().name()).resolve(project.name()),
          mock(Repository.class)));
    }

    @Override
    protected Map<URL, LocalRepository> loadLocalRepositories() {
      return new HashMap<>();
    }

    @Override
    protected void storeLocalRepositories() {
      // do nothing
    }
  }

}
