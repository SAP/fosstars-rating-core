package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_ECLIPSE;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;

/**
 * The data provider tries to figure out if an open-source project belongs to the Eclipse Software
 * Foundation.
 */
public class IsEclipse extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  private static final String[] KNOWN_ECLIPSE_ORGANISATIONS = { "eclipse", "eclipse-ee4j" };

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public IsEclipse(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return IS_ECLIPSE;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project belongs to the Eclipse Software Foundation ...");
    for (String organisation : KNOWN_ECLIPSE_ORGANISATIONS) {
      if (organisation.equalsIgnoreCase(project.organization().name())) {
        return IS_ECLIPSE.value(true);
      }
    }
    return IS_ECLIPSE.value(false);
  }
}
