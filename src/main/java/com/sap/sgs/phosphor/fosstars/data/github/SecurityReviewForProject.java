package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS_DONE;

import com.sap.sgs.phosphor.fosstars.data.json.SecurityReviewStorage;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviewsDoneValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import org.kohsuke.github.GitHub;

/**
 * This data provider provides info about security reviews for an open-source project.
 * TODO: This class doesn't talk to GitHub. Instead, it uses a local storage
 *       which contains info about known security teams.
 *       SecurityReviewForProject may be converted to a data provider.
 */
public class SecurityReviewForProject extends CachedSingleFeatureGitHubDataProvider {

  /**
   * Where the info about security review is stored.
   */
  private final SecurityReviewStorage storage;

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   * @throws IOException If the information about security reviews can't be loaded.
   */
  public SecurityReviewForProject(GitHub github) throws IOException {
    super(github);
    storage = SecurityReviewStorage.load();
  }

  @Override
  protected Feature supportedFeature() {
    return SECURITY_REVIEWS_DONE;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if any security review has been done for the project ...");
    return new SecurityReviewsDoneValue(storage.get(project.url()));
  }
}
