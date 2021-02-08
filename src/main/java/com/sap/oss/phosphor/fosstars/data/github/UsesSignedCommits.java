package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SIGNED_COMMITS;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.UnknownValue;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This data provider checks if a project uses signed commits. The check would be based on
 * a density ratio compared against a percentage threshold
 * ({@link #SIGNED_COMMIT_PERCENTAGE_THRESHOLD}).
 */
public class UsesSignedCommits extends CachedSingleFeatureGitHubDataProvider<Boolean> {

  /**
   * A period of time when the provider should look for signed commits.
   */
  private static final Duration ONE_YEAR = Duration.ofDays(365);

  /**
   * The threshold in % of number of signed commits against total number of commits in a
   * year.
   */
  private static final double SIGNED_COMMIT_PERCENTAGE_THRESHOLD = 90;

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public UsesSignedCommits(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Feature<Boolean> supportedFeature() {
    return USES_SIGNED_COMMITS;
  }

  @Override
  protected Value<Boolean> fetchValueFor(GitHubProject project) throws IOException {
    logger.info("Figuring out if the project uses signed commits ...");
    return usesSignedCommits(project);
  }

  /**
   * It gathers the list of commits within one year from the current date. Then it calculates
   * density ratio. The density ratio is the percentage calculation of number of signed
   * commits against the total number of commits in the year. If the density ratio in % crosses a
   * certain percentage threshold ({@link #SIGNED_COMMIT_PERCENTAGE_THRESHOLD}), then it says that
   * the project uses signed commits. Otherwise, it says that the project does not use
   * signed commits.
   *
   * @param project The project.
   * @return {@link BooleanValue} value of the feature.
   */
  private Value<Boolean> usesSignedCommits(GitHubProject project) {
    try {
      return new BooleanValue(USES_SIGNED_COMMITS, askGithub(project));
    } catch (IOException e) {
      logger.warn("Couldn't fetch data from api.github.com!", e);
      return UnknownValue.of(USES_SIGNED_COMMITS);
    }
  }

  /**
   * Check if a project uses signed commits.
   *
   * @param project The project.
   * @return boolean True if the project uses signed commits. Otherwise, False
   * @throws IOException caused during REST call to GitHub API.
   */  
  private boolean askGithub(GitHubProject project) throws IOException {
    List<Commit> yearCommits
        = GitHubDataFetcher.localRepositoryFor(project).commitsWithin(ONE_YEAR);
    int counter = yearCommits.size();

    List<Commit> signedCommits = yearCommits.stream()
        .filter(Commit::isSigned)
        .collect(Collectors.toList());
    int signedCounter = signedCommits.size();

    return counter > 0 && signedCounter > 0
        && (signedCounter * 100 / counter) >= SIGNED_COMMIT_PERCENTAGE_THRESHOLD;
  }
}