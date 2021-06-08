package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.Subject.cast;

import com.sap.oss.phosphor.fosstars.data.AbstractCachingDataProvider;
import com.sap.oss.phosphor.fosstars.model.Subject;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.util.Objects;

/**
 * This is a base class for data providers that would like to check the cache
 * before trying to fetch values for a project on GitHub.
 */
public abstract class GitHubCachingDataProvider extends AbstractCachingDataProvider {

  /**
   * An interface to GitHub.
   */
  protected final GitHubDataFetcher fetcher;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public GitHubCachingDataProvider(GitHubDataFetcher fetcher) {
    this.fetcher = Objects.requireNonNull(
        fetcher, "Oh no! You gave me a null instead of a GitHub fetcher!");
  }

  @Override
  public final boolean interactive() {
    return false;
  }

  @Override
  public boolean supports(Subject subject) {
    return subject instanceof GitHubProject;
  }

  @Override
  protected final ValueSet fetchValuesFor(Subject subject) throws IOException {
    return fetchValuesFor(cast(subject, GitHubProject.class));
  }

  /**
   * Fetch values for a project on GitHub.
   *
   * @param project The subject
   * @return A set of values for the project.
   * @throws IOException If something went wrong.
   */
  protected abstract ValueSet fetchValuesFor(GitHubProject project) throws IOException;
}
