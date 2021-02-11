package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.IteratorUtils;

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
  private static final String CODEQL_ANALYZE_STEP_TASK = "github/codeql-action/analyze";

  /**
   * Initializes a data provider.
   *
   * @param fetcher An interface to GitHub.
   */
  public CodeqlDataProvider(GitHubDataFetcher fetcher) {
    super(fetcher);
  }

  @Override
  protected Set<Feature<?>> supportedFeatures() {
    return setOf(RUNS_CODEQL_SCANS, USES_CODEQL_CHECKS);
  }

  @Override
  protected ValueSet fetchValuesFor(GitHubProject project) throws IOException {
    logger.info("Figuring out how the project uses CodeQL ...");

    LocalRepository repository = GitHubDataFetcher.localRepositoryFor(project);

    Value<Boolean> runsCodeqlScans = RUNS_CODEQL_SCANS.value(false);
    Value<Boolean> usesCodeqlChecks = USES_CODEQL_CHECKS.value(false);

    // ideally, we're looking for a GitHub action that runs CodeQL scan on pull requests
    // but if we just find an action that runs CodeQL scans, that's also fine
    for (Path configPath : findGitHubActionsIn(repository)) {
      InputStream content = Files.newInputStream(configPath);
      Map<String, Object> githubAction = Yaml.readMap(content);
      if (triggersCodeqlScan(githubAction)) {
        runsCodeqlScans = RUNS_CODEQL_SCANS.value(true);
        if (runsOnPullRequests(githubAction)) {
          usesCodeqlChecks = USES_CODEQL_CHECKS.value(true);
          break;
        }
      }
    }

    return ValueHashSet.from(usesCodeqlChecks, runsCodeqlScans);
  }

  /**
   * Looks for GitHub actions in a repository.
   *
   * @param repository The repository to be checked.
   * @return A list of paths to GitHub Action configs.
   * @throws IOException If something went wrong.
   */
  private static List<Path> findGitHubActionsIn(LocalRepository repository) throws IOException {
    Path path = Paths.get(GITHUB_ACTIONS_DIRECTORY);

    if (!repository.hasDirectory(path)) {
      return Collections.emptyList();
    }

    return repository.files(path, CodeqlDataProvider::isGitHubActionConfig);
  }

  /**
   * Checks if a GitHub action triggers a CodeQL scan.
   *
   * @param githubAction A config for the action.
   * @return True if the action triggers a CodeQL scan, false otherwise.
   */
  private static boolean triggersCodeqlScan(Map<?, ?> githubAction) {
    return Optional.ofNullable(githubAction.get("jobs"))
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(jobs -> jobs.get("analyze"))
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(jobs -> jobs.get("steps"))
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .map(CodeqlDataProvider::hasCodeqlAnalyzeStep)
        .orElse(false);
  }

  /**
   * Checks if a collection of steps from a GitHub action contains a step that triggers
   * a CodeQL scan.
   *
   * @param steps The steps to be checked.
   * @return True if the steps contain a step that triggers a CodeQL scan, false otherwise.
   */
  private static boolean hasCodeqlAnalyzeStep(Iterable<?> steps) {
    return IteratorUtils.toList(steps.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(step -> step.get("uses"))
        .filter(String.class::isInstance)
        .map(String.class::cast)
        .anyMatch(uses -> uses.startsWith(CODEQL_ANALYZE_STEP_TASK));
  }

  /**
   * Checks if a GitHub action runs on pull requests.
   *
   * @param githubAction A config of the action.
   * @return True if the action runs on pull requests, false otherwise.
   */
  private static boolean runsOnPullRequests(Map<?, ?> githubAction) {
    return Optional.ofNullable(githubAction.get("on"))
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(on -> on.containsKey("pull_request"))
        .orElse(false);
  }

  /**
   * Checks if a file is a config for a GitHub action.
   *
   * @param path A path to the file.
   * @return True if a file looks like a config for a GitHub action, false otherwise.
   */
  private static boolean isGitHubActionConfig(Path path) {
    return GITHUB_ACTIONS_CONFIG_EXTENSIONS
        .stream().anyMatch(ext -> path.getFileName().toString().endsWith(ext));
  }
}
