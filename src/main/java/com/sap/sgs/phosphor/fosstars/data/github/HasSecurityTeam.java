package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.data.json.SecurityTeamStorage;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
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
  private final SecurityTeamStorage securityTeam;

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public HasSecurityTeam(GitHub github) throws IOException {
    super(github);
    securityTeam = SecurityTeamStorage.load();
  }

  @Override
  protected HasSecurityTeam doUpdate(GitHubProject project, ValueSet values) {
    logger.info("Figuring out if the project has a security team ...");

    if (securityTeam.existsFor(project.url())) {
      values.update(HAS_SECURITY_TEAM.value(true));
    } else {
      values.update(HAS_SECURITY_TEAM.unknown());
    }

    return this;
  }
}
