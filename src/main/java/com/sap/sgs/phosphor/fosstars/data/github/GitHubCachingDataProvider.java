package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.data.AbstractCachingDataProvider;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubDataFetcher;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.util.Objects;
import org.kohsuke.github.GitHub;

/**
 * This is a base class for data providers that would like to check the cache
 * before trying to fetch values for a project on GitHub.
 */
public abstract class GitHubCachingDataProvider extends AbstractCachingDataProvider<GitHubProject> {

  /**
   * An interface to the GitHub API.
   */
  protected final GitHub github;

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public GitHubCachingDataProvider(GitHub github) {
    this.github = Objects.requireNonNull(
        github, "Oh no! You gave me a null instead of a GitHub instance!");
  }

  @Override
  public final boolean interactive() {
    return false;
  }

  /**
   * Returns an instance of {@link GitHubDataFetcher}.
   */
  protected GitHubDataFetcher gitHubDataFetcher() {
    return GitHubDataFetcher.instance();
  }

}
