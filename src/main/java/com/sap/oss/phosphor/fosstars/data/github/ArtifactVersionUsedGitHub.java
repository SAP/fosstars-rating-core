package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;

import com.sap.oss.phosphor.fosstars.data.AbstractCachingDataProvider;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersions;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * This data provider collects version information from system environment.
 * FIXME: somehow misuse of GitHubDataProvider, should be see {@link ArtifactVersionUsed}
 */
public class ArtifactVersionUsedGitHub extends CachedSingleFeatureGitHubDataProvider<String> {

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public ArtifactVersionUsedGitHub(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Value<String> fetchValueFor(GitHubProject project) throws IOException {
    String version = getSetVersion();
    logger.info("Found version: {}", version);
    return ARTIFACT_VERSION.value(version == null ? "" : version);
  }

  private String getSetVersion() {
    String setVersion = System.getenv("FEATURE_VALUE_ARTIFACT_VERSION");
    if (setVersion == null) {
      setVersion = System.getProperty("feature-value.artifact-version");
    }
    return setVersion;
  }

  @Override
  protected Feature<String> supportedFeature() {
    return ARTIFACT_VERSION;
  }
}
