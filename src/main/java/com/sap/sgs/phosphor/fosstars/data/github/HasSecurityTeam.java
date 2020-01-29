package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.data.json.SecurityTeamStorage;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.tool.YesNoSkipQuestion;
import com.sap.sgs.phosphor.fosstars.tool.YesNoSkipQuestion.Answer;
import java.io.IOException;
import org.kohsuke.github.GitHub;

/**
 * This data provider tries to figure out if a project has a security team. First, it checks if a
 * project belongs to an organization which provides a security team such as Apache Software
 * Foundation. Next, it tries to ask a user if {@link UserCallback} is available.
 */
public class HasSecurityTeam extends AbstractGitHubDataProvider {

  /**
   * Where info about security teams are stored.
   */
  private final SecurityTeamStorage storage;

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   */
  public HasSecurityTeam(String where, String name, GitHub github, boolean mayTalk) throws IOException {
    super(where, name, github, mayTalk);
    storage = SecurityTeamStorage.load();
  }

  @Override
  public Value<Boolean> get(UserCallback callback) {
    System.out.println("[+] Figuring out if the project has a security team ...");

    if (storage.supported(url)) {
      return new BooleanValue(HAS_SECURITY_TEAM, true);
    }

    if (mayTalk()) {
      String question = String.format(
          "Does project %s/%s have a security team? Say yes, no, or skip, please.",
          where, name);

      Answer answer = new YesNoSkipQuestion(callback, question).ask();
      switch (answer) {
        case YES:
          return new BooleanValue(HAS_SECURITY_TEAM, true);
        case NO:
          return new BooleanValue(HAS_SECURITY_TEAM, false);
      }

      // TODO: store the answer in the SecurityTeamStorage
    }

    return UnknownValue.of(HAS_SECURITY_TEAM);
  }
}
