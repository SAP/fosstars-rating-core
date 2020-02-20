package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;

import com.sap.sgs.phosphor.fosstars.data.DataProvider;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import java.io.IOException;
import java.util.Objects;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider returns a number of watchers for a project.
 */
public class NumberOfWatchers extends AbstractGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   */
  public NumberOfWatchers(String where, String name, GitHub github) {
    super(where, name, github);
  }

  @Override
  public DataProvider update(ValueSet values) throws IOException {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    System.out.println("[+] Counting how many watchers the project has ...");
    GHRepository repository = github.getRepository(path);
    int watchers = repository.getSubscribersCount();
    values.update(new IntegerValue(NUMBER_OF_WATCHERS_ON_GITHUB, watchers));
    return this;
  }
}
