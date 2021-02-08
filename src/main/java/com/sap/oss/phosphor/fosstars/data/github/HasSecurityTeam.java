package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_TEAM;

import com.sap.oss.phosphor.fosstars.data.UserCallback;
import com.sap.oss.phosphor.fosstars.data.json.SecurityTeamStorage;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;

/**
 * This data provider tries to figure out if a project has a security team. First, it checks if a
 * project belongs to an organization which provides a security team such as Apache Software
 * Foundation. Next, it tries to ask a user if {@link UserCallback} is available.
 */
public class HasSecurityTeam extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * Where info about security teams are stored.
   */
  private final SecurityTeamStorage securityTeam;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If something went wrong.
   */
  public HasSecurityTeam(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
    securityTeam = SecurityTeamStorage.load();
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return HAS_SECURITY_TEAM;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project has a security team ...");
    return HAS_SECURITY_TEAM.value(securityTeam.existsFor(project.scm()));
  }
}