package com.sap.oss.phosphor.fosstars.data.github;

import com.sap.oss.phosphor.fosstars.data.AbstractDataProvider;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.util.Objects;

/**
 * Base class for data providers which get data from GitHub.
 */
public abstract class AbstractGitHubDataProvider extends AbstractDataProvider {

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

  @Override
  public boolean supports(Subject subject) {
    return subject instanceof GitHubProject;
  }

  /**
   * The method always returns false, so that all child classes can't be interactive.
   */
  @Override
  public final boolean interactive() {
    return false;
  }
}
