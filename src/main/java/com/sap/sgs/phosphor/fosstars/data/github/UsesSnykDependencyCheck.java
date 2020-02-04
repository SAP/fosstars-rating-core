package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;

import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import java.util.Objects;
import org.kohsuke.github.GitHub;

/**
 * This data provider checks if an open-source project uses OWASP Dependency Check Maven plugin to
 * scan dependencies for known vulnerabilities. If it does, the provider sets {@link
 * com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures#SCANS_FOR_VULNERABLE_DEPENDENCIES} to
 * true.
 */
public class UsesSnykDependencyCheck extends AbstractGitHubDataProvider {

  /**
   * @param where A GitHub organization of user name.
   * @param name A name of a repository.
   * @param github An interface to the GitHub API.
   */
  public UsesSnykDependencyCheck(String where, String name, GitHub github) {
    super(where, name, github);
  }

  // TODO: implement UsesSnykDependencyCheck.update() method
  @Override
  public UsesSnykDependencyCheck update(ValueSet values) {
    Objects.requireNonNull(values, "Hey! Values can't be null!");
    System.out.println("[+] Figuring out if the project uses Snyk ...");

    values.update(UnknownValue.of(SCANS_FOR_VULNERABLE_DEPENDENCIES));

    return this;
  }
}
