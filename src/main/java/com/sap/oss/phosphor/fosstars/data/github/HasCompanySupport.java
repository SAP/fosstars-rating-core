package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SUPPORTED_BY_COMPANY;

import com.sap.oss.phosphor.fosstars.data.json.CompanySupportStorage;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;

/**
 * This data provider check if an open-source project is supported by a company.
 */
public class HasCompanySupport extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * Where the info about open-source projects is stored.
   */
  private final CompanySupportStorage company;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   * @throws IOException If the info about open-source projects can't be loaded.
   */
  public HasCompanySupport(GitHubDataFetcher fetcher) throws IOException {
    super(fetcher);
    company = CompanySupportStorage.load();
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return SUPPORTED_BY_COMPANY;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project is supported by a company ...");
    return SUPPORTED_BY_COMPANY.value(company.supports(project.scm()));
  }
}
