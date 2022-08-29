package com.sap.oss.phosphor.fosstars.data;

import com.sap.oss.phosphor.fosstars.data.github.GitHubCachingDataProvider;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.data.github.LocalRepository;
import com.sap.oss.phosphor.fosstars.model.Feature;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This is a base class for data providers that would like to check if the project uses a Static
 * Analysis Scan Tool (SAST) and also determines how the project uses it.
 */
public abstract class AbstractStaticScanToolsDataProvider extends
    GitHubCachingDataProvider implements StaticAnalysisScanTool {

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
   * Set of supported features.
   */
  private final Set<Feature<?>> supportedFeatures;

  /**
   * Abstract data provider.
   *
   * @param fetcher An interface to GitHub.
   * @param supportedFeatures A set of supported features.
   */
  public AbstractStaticScanToolsDataProvider(GitHubDataFetcher fetcher,
      Set<Feature<?>> supportedFeatures) {
    super(fetcher);
    this.supportedFeatures = supportedFeatures;
  }

  @Override
  public Set<Feature<?>> supportedFeatures() {
    return supportedFeatures;
  }

  /**
   * Looks for GitHub actions in a repository.
   *
   * @param repository The repository to be checked.
   * @return A list of paths to GitHub Action configs.
   * @throws IOException If something went wrong.
   */
  protected static List<Path> findGitHubActionsIn(LocalRepository repository) throws IOException {
    Path path = Paths.get(GITHUB_ACTIONS_DIRECTORY);

    if (!repository.hasDirectory(path)) {
      return Collections.emptyList();
    }

    return repository.files(path, AbstractStaticScanToolsDataProvider::isGitHubActionConfig);
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

  @Override
  public boolean runsOnPullRequests(Map<?, ?> githubAction) {
    return runsOnPullRequestDefinedAsArray(githubAction)
        || runsOnPullRequestDefinedAsMap(githubAction);
  }

  /**
   * Checks if a GitHub action runs on pull requests is defined as Array.
   *
   * @param githubAction A config of the action.
   * @return True if the action runs on pull requests, false otherwise.
   */
  private boolean runsOnPullRequestArrayType(Map<?, ?> githubAction) {
    return Optional.ofNullable(githubAction.get("on"))
        .filter(ArrayList.class::isInstance)
        .map(ArrayList.class::cast)
        .map(on -> on.contains("pull_request"))
        .orElse(false);
  }

  /**
   * Checks if a GitHub action runs on pull requests is defined as Map.
   *
   * @param githubAction A config of the action.
   * @return True if the action runs on pull requests, false otherwise.
   */
  private boolean runsOnPullRequestMapType(Map<?, ?> githubAction) {
    return Optional.ofNullable(githubAction.get("on"))
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(on -> on.containsKey("pull_request"))
        .orElse(false);
  }
}