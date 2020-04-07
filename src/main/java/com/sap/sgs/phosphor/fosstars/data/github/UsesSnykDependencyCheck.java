package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;

import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import org.kohsuke.github.GitHub;

/**
 * This data provider checks if an open-source project uses OWASP Dependency Check Maven plugin to
 * scan dependencies for known vulnerabilities. If it does, the provider sets {@link
 * com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#SCANS_FOR_VULNERABLE_DEPENDENCIES}
 * to true.
 */
public class UsesSnykDependencyCheck extends AbstractGitHubDataProvider {

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public UsesSnykDependencyCheck(GitHub github) {
    super(github);
  }

  // TODO: implement UsesSnykDependencyCheck.update() method
  @Override
  protected UsesSnykDependencyCheck doUpdate(GitHubProject project, ValueSet values) {
    logger.info("Figuring out if the project uses Snyk ...");
    // TODO: check the cache
    values.update(UnknownValue.of(SCANS_FOR_VULNERABLE_DEPENDENCIES));
    return this;
  }
}
