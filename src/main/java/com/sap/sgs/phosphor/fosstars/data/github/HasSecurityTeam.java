package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.data.json.SecurityTeamStorage;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.tool.YesNoSkipQuestion;
import com.sap.sgs.phosphor.fosstars.tool.YesNoSkipQuestion.Answer;
import java.io.IOException;
import java.util.Objects;
import org.kohsuke.github.GitHub;

/**
 * This data provider tries to figure out if a project has a security team. First, it checks if a
 * project belongs to an organization which provides a security team such as Apache Software
 * Foundation. Next, it tries to ask a user if {@link UserCallback} is available.
 * TODO: This class doesn't talk to GitHub. Instead, it uses a local storage
 *       which contains info about known security teams.
 *       SecurityTeamStorage may be converted to a data provider.
 */
public class HasSecurityTeam extends AbstractGitHubDataProvider {

  /**
   * Where info about security teams are stored.
   */
  private final SecurityTeamStorage storage;

  /**
   * Initializes a data provider.
   *
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   */
  public HasSecurityTeam(String where, String name, GitHub github) throws IOException {
    super(where, name, github);
    storage = SecurityTeamStorage.load();
  }

  @Override
  public HasSecurityTeam update(ValueSet values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    logger.info("Figuring out if the project has a security team ...");

    values.update(UnknownValue.of(HAS_SECURITY_TEAM));


    if (storage.supported(url)) {
      values.update(new BooleanValue(HAS_SECURITY_TEAM, true));
    } else if (callback.canTalk()) {
      String question = String.format(
          "Does project %s/%s have a security team? Say yes, no, or skip, please.",
          where, name);

      Answer answer = new YesNoSkipQuestion(callback, question).ask();
      switch (answer) {
        case YES:
          values.update(new BooleanValue(HAS_SECURITY_TEAM, true));
          break;
        case NO:
          values.update(new BooleanValue(HAS_SECURITY_TEAM, false));
          break;
        default:
          throw new IllegalArgumentException(
              String.format("Hey! You gave me an unexpected answer: %s", answer));
      }

      // TODO: store the answer in the SecurityTeamStorage
    }

    return this;
  }
}
