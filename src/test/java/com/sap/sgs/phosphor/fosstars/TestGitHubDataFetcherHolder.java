package com.sap.sgs.phosphor.fosstars;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.sap.sgs.phosphor.fosstars.data.github.GitHubDataFetcher;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
   * A base directory for {@link GitHubDataFetcher}.
   */
  private Path path;

  /**
   * An instance of {@link GitHubDataFetcher} for tests.
   */
  protected GitHubDataFetcher fetcher;

  /**
   * Initializes a fetcher for each test case.
   *
   * @throws IOException If the initialization failed.
   */
  @Before
  public void init() throws IOException {
    GitHub github = mock(GitHub.class);
    path = Files.createTempDirectory(getClass().getName());
    fetcher = spy(new GitHubDataFetcher(github, path));
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

}
