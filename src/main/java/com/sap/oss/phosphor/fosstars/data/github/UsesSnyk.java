package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_SNYK;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SNYK;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * This data provider checks if an open-source project on GitHub uses Snyk, and fills out the {@link
 * OssFeatures#USES_SNYK} feature.
 *
 * <p>First, the provider checks if a repository contains a policy file for Snyk. If the policy file
 * exists, then the provider reports that the project uses Snyk. Next, the provider searches for
 * commits from Snyk in the commit history. If the commits are found, then the provider also reports
 * that the project uses Snyk.
 */
public class UsesSnyk extends AbstractDependencyScanDataProvider {

  /**
   * A file name containing Snyk policies in a repository.
   *
   * @see <a href="https://docs.snyk.io/snyk-cli/test-for-vulnerabilities/the-.snyk-file/">The .snyk
   *     file</a>
   */
  private static String SNYK_POLICY_FILE_NAME = ".snyk";

  /**
   * A location of a Snyk configuration file in a repository.
   *
   * @see <a href="https://github.com/snyk/actions/tree/master/setup">To setup Snyk actions</a>
   *
   */
  private static final String [] SNYK_CONFIGS = {
      ".github/workflows/snyk.yaml",
      ".github/workflows/snyk.yml"
  };

  /** 
    * Predicate to confirm if there is a file in open-source project with the .snyk extension.
    */
  private static final Predicate<Path> SNYK_FILE_PREDICATE =
      path -> path.getFileName().toString().endsWith(SNYK_POLICY_FILE_NAME);

  /**
   * A pattern to detect commits by Snyk.
   *
   * @see <a
   *     href="https://docs.snyk.io/integrations/git-repository-scm-integrations/github-integration#commit-signing/">Snyk
   *     commit signing</a>
   */
  private static final String SNYK_PATTERN = "snyk";

  @Override
  protected String getDependencyCheckerPattern() {
    return SNYK_PATTERN;
  }

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesSnyk(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return setOf(USES_SNYK, HAS_OPEN_PULL_REQUEST_FROM_SNYK);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Checking how the project uses Snyk ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    return ValueHashSet.from(
        USES_SNYK.value(
            hasSnykPolicy(repository)
                || hasDependencyCheckerConfig(repository, SNYK_CONFIGS)
                || hasDependencyCheckerCommits(repository)),
        HAS_OPEN_PULL_REQUEST_FROM_SNYK.value(hasOpenPullRequestFromDependencyChecker(project)));
  }

  /**
   * Checks if a repository has a policy file for Snyk.
   *
   * @param repository The repository
   * @return True if a policy file was found, false otherwise.
   */
  private boolean hasSnykPolicy(LocalRepository repository) throws IOException {
    List<Path> snykPolicyFilePaths = repository.files(SNYK_FILE_PREDICATE);
    return !snykPolicyFilePaths.isEmpty();
  }
}
