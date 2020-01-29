package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.SCANS_FOR_VULNERABLE_DEPENDENCIES;

import com.sap.sgs.phosphor.fosstars.data.UserCallback;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
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
   * @param mayTalk A flag which shows if the provider can communicate with a user or not.
   */
  public UsesSnykDependencyCheck(String where, String name, GitHub github, boolean mayTalk) {
    super(where, name, github, mayTalk);
  }

  @Override
  public Value get(UserCallback callback) {
    // TODO: implement UsesSnykDependencyCheck.get() method
    System.out.println("[+] Figuring out if the project uses Snyk ...");
    return UnknownValue.of(SCANS_FOR_VULNERABLE_DEPENDENCIES);
  }
}
