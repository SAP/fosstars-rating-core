package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;

/**
 * This data provider check if an open-source project has a security policy which describes how
 * vulnerabilities should be reported and fixed.
 */
public class HasSecurityPolicy extends CachedSingleFeatureGitHubDataProvider {

  /**
   * A minimal number of characters in a security policy to consider it valid.
   */
  private static final int ACCEPTABLE_POLICY_SIZE = 50;

  /**
   * A list of well-known location of a security policy.
   *
   * @see <a href="https://help.github.com/en/github/managing-security-vulnerabilities/adding-a-security-policy-to-your-repository">
   * GitHub: Adding a security policy to your repository</a>
   */
  private static final String[] POLICY_LOCATIONS = {
      "SECURITY.md",
      "docs/SECURITY.md",
      ".github/SECURITY.md",
      ".github/SECURITY.rst"
  };

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public HasSecurityPolicy(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature supportedFeature() {
    return HAS_SECURITY_POLICY;
  }

  @Override
  protected Value fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project has a security policy ...");
    return hasSecurityPolicy(project);
  }

  /**
   * Check if a project has a security policy.
   * @param project The project.
   * @return A value of {@link OssFeatures#HAS_SECURITY_POLICY} feature.
   * @throws IOException If something went wrong.
   */
  private Value<Boolean> hasSecurityPolicy(GitHubProject project) throws IOException {
    LocalRepository repository = fetcher.localRepositoryFor(project);
    for (String path : POLICY_LOCATIONS) {
      if (isPolicy(repository, path)) {
        return HAS_SECURITY_POLICY.value(true);
      }
    }

    return HAS_SECURITY_POLICY.value(false);
  }

  /**
   * Check if a file exists in a repository and its content more than
   * {@link #ACCEPTABLE_POLICY_SIZE}.
   *
   * @param repository The repository.
   * @param path A path to the file
   * @return True if the file exists in the repository and it's big enough, false otherwise.
   */
  private static boolean isPolicy(LocalRepository repository, String path) throws IOException {
    return repository.file(path)
        .filter(content -> content.length() > ACCEPTABLE_POLICY_SIZE)
        .isPresent();
  }
}