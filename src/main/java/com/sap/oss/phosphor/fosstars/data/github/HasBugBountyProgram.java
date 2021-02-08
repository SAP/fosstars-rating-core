package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_BUG_BOUNTY_PROGRAM;

import com.sap.oss.phosphor.fosstars.data.json.BugBountyProgramStorage;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;

/**
 * This data provider tries to figure out if a project has a bug bounty program.
 */
public class HasBugBountyProgram extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * Where info about bug bounty programs are stored.
   */
  private final BugBountyProgramStorage bugBounties;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If something went wrong.
   */
  public HasBugBountyProgram(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
    bugBounties = BugBountyProgramStorage.load();
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return HAS_BUG_BOUNTY_PROGRAM;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project has a bug bounty program ...");
    return HAS_BUG_BOUNTY_PROGRAM.value(bugBounties.existsFor(project));
  }
}