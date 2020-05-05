package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.data.AbstractDataProvider;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.util.Objects;

/**
 * Base class for data providers which get data from GitHub.
 */
public abstract class AbstractGitHubDataProvider extends AbstractDataProvider<GitHubProject> {

  /**
   * An interface to GitHub.
   */
  protected final GitHubDataFetcher fetcher;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public AbstractGitHubDataProvider(GitHubDataFetcher fetcher) {
    this.fetcher = Objects.requireNonNull(
        fetcher, "Oh no! You gave me a null instead of a GitHub fetcher!");
  }

  /**
   * The method always returns false, so that all child classes can't be interactive.
   */
  @Override
  public final boolean interactive() {
    return false;
  }

  /**
   * Returns an instance of {@link GitHubDataFetcher}.
   */
  // TODO: this method looks redundant
  protected GitHubDataFetcher gitHubDataFetcher() {
    return fetcher;
  }
}
