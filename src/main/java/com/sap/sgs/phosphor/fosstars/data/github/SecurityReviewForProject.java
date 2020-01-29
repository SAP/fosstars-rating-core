package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.data.json.SecurityReviewStorage;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviews;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviewsDoneValue;
import java.io.IOException;
import org.kohsuke.github.GitHub;

/**
 * This data provider provides info about security reviews for an open-source project.
 */
public class SecurityReviewForProject extends AbstractGitHubDataProvider {

  /**
   * Where the info about security review is stored.
   */
  private final SecurityReviewStorage storage;

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   * @throws IOException If the information about security reviews can't be loaded.
   */
  public SecurityReviewForProject(String where, String name, GitHub github, boolean mayTalk)
      throws IOException {

    super(where, name, github, mayTalk);
    storage = SecurityReviewStorage.load();
  }

  @Override
  public Value<SecurityReviews> get(UserCallback callback) {
    System.out.println("[+] Figuring out if any security review has been done for the project ...");
    return new SecurityReviewsDoneValue(storage.get(url));
  }
}
