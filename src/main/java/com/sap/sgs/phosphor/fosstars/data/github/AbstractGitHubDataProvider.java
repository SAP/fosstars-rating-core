package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.data.DataProvider;
import com.sap.sgs.phosphor.fosstars.tool.github.ValueCache;
import java.util.Date;
import java.util.Objects;
import org.kohsuke.github.GitHub;

/**
 * Base class for data providers which get data from GitHub.
 */
public abstract class AbstractGitHubDataProvider implements DataProvider {

  /**
   * A GitHub organization of user name.
   */
  protected final String where;

  /**
   * A name of a repository.
   */
  protected final String name;

  /**
   * An interface to the GitHub API.
   */
  protected final GitHub github;

  /**
   * A URL to the GitHub repository.
   */
  protected final String url;

  /**
   * A path to the GitHub repository.
   */
  protected final String path;

  /**
   * A flag which shows if the provider can communicate with a user or not.
   */
  private final boolean mayTalk;

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   */
  public AbstractGitHubDataProvider(String where, String name, GitHub github, boolean mayTalk) {
    Objects.requireNonNull(where,
        "Oh no! You gave me a null instead of an organization or user name!");
    Objects.requireNonNull(name, "Oh no! You gave me a null instead of a project name!");
    Objects.requireNonNull(name, "Oh no! You gave me a null instead of a GitHub instance!");
    this.where = where;
    this.name = name;
    this.github = github;
    this.mayTalk = mayTalk;
    this.url = String.format("https://github.com/%s/%s", where, name);
    this.path = String.format("%s/%s", where, name);
  }

  @Override
  public final boolean mayTalk() {
    return mayTalk;
  }

  ValueCache cache() {
    return ValueCache.shared();
  }

  Date tomorrow() {
    return new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L);
  }

}
