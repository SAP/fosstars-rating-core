package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.data.AbstractDataProvider;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.util.Objects;
import org.kohsuke.github.GitHub;

/**
 * Base class for data providers which get data from GitHub.
 */
public abstract class AbstractGitHubDataProvider extends AbstractDataProvider<GitHubProject> {

  /**
   * An interface to the GitHub API.
   */
  protected final GitHub github;

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public AbstractGitHubDataProvider(GitHub github) {
    this.github = Objects.requireNonNull(
        github, "Oh no! You gave me a null instead of a GitHub instance!");
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
  protected GitHubDataFetcher gitHubDataFetcher() {
    return GitHubDataFetcher.instance();
  }
}
