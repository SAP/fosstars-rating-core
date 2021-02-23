package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USE_REUSE;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;

/**
 * The data provider gathers info about how a project uses Reuse tool.
 * In particular, it fills out the {@link OssFeatures#USE_REUSE} feature.
 */
public class UseReuseDataProvider extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * A path to a Reuse config.
   */
  static final String REUSE_CONFIG = ".reuse/dep5";

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UseReuseDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return USE_REUSE;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out how the project uses Reuse ...");
    return USE_REUSE.value(GitHubDataFetcher.localRepositoryFor(project).hasFile(REUSE_CONFIG));
  }
}
