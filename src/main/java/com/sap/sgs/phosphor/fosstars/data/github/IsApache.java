package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.IS_APACHE;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import org.kohsuke.github.GitHub;

/**
 * The data provider tries to figure out if an open-source project belongs to the Apache Software
 * Foundation.
 */
public class IsApache extends CachedSingleFeatureGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public IsApache(GitHub github) {
    super(github);
  }

  @Override
  protected Feature supportedFeature() {
    return IS_APACHE;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project belongs to the Apache Software Foundation ...");
    return IS_APACHE.value("apache".equalsIgnoreCase(project.organization().name()));
  }

}
