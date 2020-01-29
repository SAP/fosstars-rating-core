package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.PROJECT_START_DATE;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.DateValue;
import java.io.IOException;
import java.util.Date;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider estimates a date when a project was created by the date when the repository
 * was created.
 */
public class ProjectStarted extends FirstCommit {

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   */
  public ProjectStarted(String where, String name, GitHub github, boolean mayTalk) {
    super(where, name, github, mayTalk);
  }

  @Override
  public Value<Date> get(UserCallback callback) throws IOException {
    System.out.println("[+] Figuring out when the project started ...");

    Date firstCommitDate = super.get(callback).get();

    GHRepository repository = github.getRepository(path);
    Date repositoryCreated = repository.getCreatedAt();

    if (firstCommitDate.before(repositoryCreated)) {
      return new DateValue(PROJECT_START_DATE, firstCommitDate);
    }

    return new DateValue(PROJECT_START_DATE, repositoryCreated);
  }
}
