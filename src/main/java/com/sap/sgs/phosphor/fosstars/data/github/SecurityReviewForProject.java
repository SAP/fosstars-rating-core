package com.sap.sgs.phosphor.fosstars.data.github;

import com.sap.sgs.phosphor.fosstars.data.json.SecurityReviewStorage;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviewsDoneValue;
import java.io.IOException;
import java.util.Objects;
import org.kohsuke.github.GitHub;

/**
 * This data provider provides info about security reviews for an open-source project.
 *
 * TODO: This class doesn't talk to GitHub. Instead, it uses a local storage
 *       which contains info about known security teams.
 *       SecurityReviewForProject may be converted to a data provider.
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
   * @throws IOException If the information about security reviews can't be loaded.
   */
  public SecurityReviewForProject(String where, String name, GitHub github)
      throws IOException {

    super(where, name, github);
    storage = SecurityReviewStorage.load();
  }

  @Override
  public SecurityReviewForProject update(ValueSet values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    System.out.println("[+] Figuring out if any security review has been done for the project ...");
    values.update(new SecurityReviewsDoneValue(storage.get(url)));
    return this;
  }
}
