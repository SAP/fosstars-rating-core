package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.util.Optional;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * This data provider check if an open-source project has a security policy which describes how
 * vulnerabilities should be reported and fixed.
 */
public class HasSecurityPolicy extends AbstractGitHubDataProvider {

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
      ".github/SECURITY.md"
  };

  /**
   * Initializes a data provider.
   *
   * @param github An interface to the GitHub API.
   */
  public HasSecurityPolicy(GitHub github) {
    super(github);
  }

  @Override
  protected HasSecurityPolicy doUpdate(GitHubProject project, ValueSet values) throws IOException {
    logger.info("Figuring out if the project has a security policy ...");
    values.update(hasSecurityPolicy(project));
    return this;
  }

  private Value<Boolean> hasSecurityPolicy(GitHubProject project) throws IOException {
    Optional<Value> something = cache.get(project, HAS_SECURITY_POLICY);
    if (something.isPresent()) {
      return something.get();
    }

    GHRepository repository = github.getRepository(project.path());

    boolean found = false;
    for (String path : POLICY_LOCATIONS) {
      if (exists(repository, path)) {
        found = true;
        break;
      }
    }

    Value<Boolean> value = HAS_SECURITY_POLICY.value(found);
    cache.put(project, value);

    return value;
  }

  /**
   * Check if a file exists in a repository and its content more than
   * {@link #ACCEPTABLE_POLICY_SIZE}.
   *
   * @param repository The repository.
   * @param path A path to the file
   * @return True if the file exists in the repository and it's big enough, false otherwise.
   */
  private static boolean exists(GHRepository repository, String path) {
    try {
      GHContent content = repository.getFileContent(path);
      if (content == null) {
        return false;
      }
      if (!content.isFile()) {
        return false;
      }

      return content.getSize() > ACCEPTABLE_POLICY_SIZE;
    } catch (IOException e) {
      return false;
    }
  }
}
