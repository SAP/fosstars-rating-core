package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubOrganization;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * This data provider check if an open-source project has a security policy which describes how
 * vulnerabilities should be reported and fixed.
 */
public class HasSecurityPolicy extends CachedSingleFeatureGitHubDataProvider<Boolean> {

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
  protected Feature<Boolean> supportedFeature() {
    return HAS_SECURITY_POLICY;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project has a security policy ...");
    return HAS_SECURITY_POLICY.value(
        hasSecurityPolicy(project) || hasSecurityPolicy(project.organization()))
        .explainIf(false, "Neither the project nor organization has a security policy");
  }

  /**
   * Check if a project has a security policy.
   *
   * @param project The project.
   * @return True if the project has a security policy, false otherwise.
   * @throws IOException If something went wrong.
   */
  private boolean hasSecurityPolicy(GitHubProject project) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    for (String path : POLICY_LOCATIONS) {
      if (isPolicy(repository, path)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Check if an organization has a security policy.
   *
   * @param organization The organization.
   * @return True if the organization has a security policy, false otherwise.
   * @throws IOException If something went wrong.
   */
  private boolean hasSecurityPolicy(GitHubOrganization organization) throws IOException {
    try (CloseableHttpClient client = httpClient()) {
      String url = String.format(
          "https://github.com/%s/.github/security/policy", organization.name());
      HttpGet httpGetRequest = new HttpGet(url);
      try (CloseableHttpResponse httpResponse = client.execute(httpGetRequest)) {
        return httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
      }
    }
  }

  /**
   * Creates an HTTP client.
   *
   * @return An HTTP client.
   */
  CloseableHttpClient httpClient() {
    return HttpClients.createDefault();
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