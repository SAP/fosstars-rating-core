package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;

/**
 * The data provider tries to figure out if an open-source project belongs to the Apache Software
 * Foundation.
 */
public class IsApache extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public IsApache(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return IS_APACHE;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project belongs to the Apache Software Foundation ...");
    return IS_APACHE.value("apache".equalsIgnoreCase(project.organization().name()));
  }

}
