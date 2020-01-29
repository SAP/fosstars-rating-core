package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.IntegerValue;
import java.io.IOException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider returns a number of stars for a project.
 */
public class NumberOfStars extends AbstractGitHubDataProvider {

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   */
  public NumberOfStars(String where, String name, GitHub github, boolean mayTalk) {
    super(where, name, github, mayTalk);
  }

  @Override
  public Value<Integer> get(UserCallback callback) throws IOException {
    System.out.println("[+] Counting how many stars the project has ...");

    GHRepository repository = github.getRepository(path);
    int stars = repository.getStargazersCount();
    return new IntegerValue(NUMBER_OF_GITHUB_STARS, stars);
  }
}
