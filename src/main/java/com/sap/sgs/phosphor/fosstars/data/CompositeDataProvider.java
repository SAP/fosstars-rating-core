package com.sap.sgs.phosphor.fosstars.data;

import com.sap.sgs.phosphor.fosstars.data.github.AbstractGitHubDataProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.kohsuke.github.GitHub;

/**
 * This is a base class for a composite data provider which combines multiple data providers.
 */
public abstract class CompositeDataProvider extends AbstractGitHubDataProvider {

  /**
   * A list of underlying data providers.
   */
  final List<DataProvider> providers = new ArrayList<>();

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   * @param providers Underlying data providers.
   */
  CompositeDataProvider(String where, String name, GitHub github, boolean mayTalk,
      DataProvider... providers) {

    super(where, name, github, mayTalk);
    if (providers != null) {
      this.providers.addAll(Arrays.asList(providers));
    }
  }

  /**
   * Add an underlying data provider.
   *
   * @param providers The data provider to be added.
   * @return This {@link CompositeDataProvider}.
   */
  public CompositeDataProvider add(DataProvider... providers) {
    Objects.requireNonNull(providers, "Provider can't be null!");
    this.providers.addAll(Arrays.asList(providers));
    return this;
  }
}
