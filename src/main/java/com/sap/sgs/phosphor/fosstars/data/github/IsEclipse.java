package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;

/**
 * The data provider tries to figure out if an open-source project belongs to the Eclipse Software
 * Foundation.
 */
public class IsEclipse extends CachedSingleFeatureGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public IsEclipse(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature supportedFeature() {
    return IS_ECLIPSE;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project belongs to the Eclipse Software Foundation ...");
    return IS_ECLIPSE.value("eclipse".equalsIgnoreCase(project.organization().name()));
  }
}
