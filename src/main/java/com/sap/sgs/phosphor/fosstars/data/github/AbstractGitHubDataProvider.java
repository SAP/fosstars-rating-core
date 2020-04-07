package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.data.AbstractDataProvider;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.util.Date;
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
   * Returns a date for tomorrow.
   */
  static Date tomorrow() {
    return new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L);
  }

}
