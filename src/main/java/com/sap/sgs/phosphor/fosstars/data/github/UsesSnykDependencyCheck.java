package com.sap.sgs.phosphor.fosstars.data.github;


import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import org.kohsuke.github.GitHub;

/**
 * This data provider checks if an open-source project uses OWASP Dependency Check Maven plugin to
 * scan dependencies for known vulnerabilities.
 */
public class UsesSnykDependencyCheck extends CachedSingleFeatureGitHubDataProvider {

  /**
   * A feature which is filled out by the provider.
   */
  public static final Feature<Boolean> USES_SNYK
      = new BooleanFeature("If a project uses Snyk");

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public UsesSnykDependencyCheck(GitHub github) {
    super(github);
  }

  @Override
  protected Feature supportedFeature() {
    return USES_SNYK;
  }

  // TODO: implement UsesSnykDependencyCheck.fetchValueFor() method
  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) {
    logger.info("Figuring out if the project uses Snyk ...");
    return UnknownValue.of(USES_SNYK);
  }
}
