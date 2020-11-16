package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.kohsuke.github.GHCheckRun;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHException;

/**
 * The data provider gathers info about how a project uses CodeQL for static analysis.
 * In particular, it tires to fill out the following features:
 * <ul>
 *   <li>{@link OssFeatures#RUNS_CODEQL_SCANS}</li>
 *   <li>{@link OssFeatures#USES_CODEQL_CHECKS}</li>
 * </ul>
 *
 * @see LgtmDataProvider
 */
public class CodeqlDataProvider extends GitHubCachingDataProvider {

  /**
   * The number of latest commits to be checked.
   */
  private static final int COMMITS_TO_BE_CHECKED = 20;

  /**
   * A directory where GitHub Actions configs are stored.
   */
  private static final String GITHUB_ACTIONS_DIRECTORY = ".github/workflows";

  /**
   * A list of extensions of GitHub Actions configs.
   */
  private static final List<String> GITHUB_ACTIONS_CONFIG_EXTENSIONS
      = Arrays.asList(".yaml", ".yml");

  /**
   * A step in a GitHub action that triggers analysis with CodeQL.
   */
  private static final String CODEQL_ANALYZE_STEP_CALL = "uses: github/codeql-action/analyze";

  /**
   * A pattern to check if a check in a pull request is for CodeQL scan (lowercase).
   */
  private static final String CODEQL_CHECK_PATTERN = "codeql";

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public CodeqlDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Set<Feature> supportedFeatures() {
    return setOf(RUNS_CODEQL_SCANS, USES_CODEQL_CHECKS);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out how the project uses CodeQL ...");
    return ValueHashSet.from(usesCodeqlChecks(project), runsCodeqlScans(project));
  }

  /**
   * Checks if a project uses CodeQL checks for pull requests.
   *
   * @param project The project to be checked.
   * @return A value of the {@link OssFeatures#USES_CODEQL_CHECKS} feature.
   * @throws IOException If something went wrong.
   */
  private Value<Boolean> usesCodeqlChecks(GitHubProject project) throws IOException {
    for (GHCommit commit : fetcher.githubCommitsFor(project, COMMITS_TO_BE_CHECKED)) {
      if (hasCodeqlChecks(commit)) {
        return USES_CODEQL_CHECKS.value(true);
      }
    }

    return USES_CODEQL_CHECKS.value(false);
  }

  /**
   * Checks if a project runs CodeQL checks on pull requests.
   *
   * @param project The project to be checked.
   * @return A value of the {@link OssFeatures#RUNS_CODEQL_SCANS} feature.
   * @throws IOException If something went wrong.
   */
  private Value<Boolean> runsCodeqlScans(GitHubProject project) throws IOException {
    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);
    for (Path path : repository.files(CodeqlDataProvider::isGitHubActionConfig)) {
      Optional<InputStream> content = repository.read(path);
      if (!content.isPresent()) {
        continue;
      }

      try (BufferedReader reader = new BufferedReader(new InputStreamReader(content.get()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.trim().startsWith(CODEQL_ANALYZE_STEP_CALL)) {
            return RUNS_CODEQL_SCANS.value(true);
          }
        }
      }
    }

    return RUNS_CODEQL_SCANS.value(false);
  }

  /**
   * Checks if a file is a config for a GitHub action.
   *
   * @param path A path to the file.
   * @return True if a file looks like a config for a GitHub action, false otherwise.
   */
  private static boolean isGitHubActionConfig(Path path) {
    return path.startsWith(GITHUB_ACTIONS_DIRECTORY)
        && GITHUB_ACTIONS_CONFIG_EXTENSIONS.stream()
              .anyMatch(ext -> path.getFileName().endsWith(ext));
  }

  /**
   * Checks if a commit has CodeQL checks.
   *
   * @param commit The commit to be checked.
   * @return True if the commit has CodeQL checks, false otherwise.
   * @throws IOException If something went wrong.
   */
  private boolean hasCodeqlChecks(GHCommit commit) throws IOException {
    try {
      for (GHCheckRun checkRun : commit.getCheckRuns()) {
        if (checkRun.getName().toLowerCase().contains(CODEQL_CHECK_PATTERN)) {
          return true;
        }
      }
    } catch (GHException e) {
      logger.warn("Oops! Something went wrong: {}", e.getMessage());
      logger.debug("Here is what happened: ", e);
    }

    return false;
  }
}
