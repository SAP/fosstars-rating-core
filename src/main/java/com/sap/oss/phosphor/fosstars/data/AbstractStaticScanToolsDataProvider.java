package com.sap.oss.phosphor.fosstars.data;

import static com.sap.oss.phosphor.fosstars.github.GitHubVisitor.Location.GITHUB_ACTION;
import static com.sap.oss.phosphor.fosstars.github.GitHubVisitor.Location.INI_CONFIG;
import static com.sap.oss.phosphor.fosstars.github.GitHubVisitor.Location.PRE_COMMIT_HOOK;

import com.sap.oss.phosphor.fosstars.data.github.GitHubCachingDataProvider;
import com.sap.oss.phosphor.fosstars.data.github.GitHubDataFetcher;
import com.sap.oss.phosphor.fosstars.data.github.LocalRepository;
import com.sap.oss.phosphor.fosstars.github.AbstractGitHubVisitor;
import com.sap.oss.phosphor.fosstars.github.GitHubVisitor;
import com.sap.oss.phosphor.fosstars.github.GitHubVisitor.Location;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.collections4.IteratorUtils;

/**
 * This is a base class for data providers that would like to check if the project uses a Static
 * Analysis Scan Tool (SAST) and also determines how the project uses it.
 */
public abstract class AbstractStaticScanToolsDataProvider extends
    GitHubCachingDataProvider {

  /**
   * A directory where GitHub Actions configs are stored.
   */
  private static final String GITHUB_ACTIONS_DIRECTORY = ".github/workflows";

  /**
   * A pre-commit hook standard config file path.
   *
   * @see <a href=https://pre-commit.com/#2-add-a-pre-commit-configuration>Pre-commit hook config
   *      file</a>
   */
  private static final String PRE_COMMIT_HOOK_CONFIG = ".pre-commit-config.yaml";

  /**
   * A list of extensions of GitHub Actions configs.
   */
  private static final List<String> GITHUB_ACTIONS_CONFIG_EXTENSIONS =
      Arrays.asList(".yaml", ".yml");

  /**
   * A list of extensions of GitHub Actions configs.
   */
  private static final String GITHUB_INI_CONFIG_EXTENSION = ".ini";

  /**
   * A Bi-Predicate to evaluate each context of Map type extracted from the GitHub Action jobs.
   */
  private static final BiPredicate<Map<?, ?>, Map<String, Predicate<String>>> MATCH_MAP_PREDICATE =
      (context, matchers) -> {
        for (Map.Entry<String, Predicate<String>> entry : matchers.entrySet()) {
          final String key = entry.getKey();
          if (context.containsKey(key)
              && entry.getValue().test(context.get(key).toString())) {
            return true;
          }
        }
        return false;
      };

  /**
   * A Bi-Predicate to evaluate each content of String type extracted from the GitHub Action jobs.
   */
  private static final BiPredicate<String, Map<String, Predicate<String>>> MATCH_STRING_PREDICATE =
      (content, matchers) -> {
        for (Predicate<String> entry : matchers.values()) {
          if (entry.test(content)) {
            return true;
          }
        }
        return false;
      };

  /**
   * The list of Pre-defined stops to run the predicate or search for the key pattern in pre-commit
   * hook config file.
   */
  private static final List<String> PRE_COMMIT_HOOK_CONTEXT =
      Arrays.asList("entry", "additional_dependencies", "repo", "rev");

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
   * Browse a POM file with a specified visitor.
   *
   * @param repository The {@link LocalRepository}.
   * @param matchers map of {@link Predicate}s to parse for a specific content and match the
   *        predicate.
   * @param configMatchers map of {@link Predicate}s to parse for a specific content and match the
   *        specific configs with the given predicate.
   * @param visitor The visitor.
   * @param <T> A type of the visitor.
   * @return The passed visitor.
   * @throws IOException if something went wrong.
   */
  protected static <T extends GitHubVisitor> T browse(LocalRepository repository,
      Map<String, Predicate<String>> matchers, Map<String, Predicate<String>> configMatchers,
      T visitor) throws IOException {
    Objects.requireNonNull(repository, "Oh no! Repository is null!");
    Objects.requireNonNull(matchers, "Oh no! Predicate is null!");
    Objects.requireNonNull(visitor, "On no! Visitor is null!");

    visitor.visitGitHubAction(repository, matchers, configMatchers,
        in(EnumSet.noneOf(Location.class), GITHUB_ACTION));
    visitor.visitPreCommitHook(repository, matchers,
        in(EnumSet.noneOf(Location.class), PRE_COMMIT_HOOK));
    visitor.visitIniConfig(repository, matchers, in(EnumSet.noneOf(Location.class), INI_CONFIG));
    // visitor.visitSourceCode(repository, predicate, in(EnumSet.noneOf(Location.class), TYPE_PY));
    return visitor;
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
   * Looks for GitHub ini config files in a repository.
   *
   * @param repository The repository to be checked.
   * @return A list of paths to GitHub Action ini configs.
   * @throws IOException If something went wrong.
   */
  public static List<Path> findIniConfigsIn(LocalRepository repository) throws IOException {
    return repository
        .files(path -> path.getFileName().toString().endsWith(GITHUB_INI_CONFIG_EXTENSION));
  }

  /**
   * Browse through the GitHub actions and find matches with the given predicates.
   *
   * @param githubAction GitHub Actions.
   * @param matchers predicates to match through the given action.
   * @return Optional<{@link Map}> step if one of the predicate finds matches.
   *         Optional.empty otherwise.
   */
  private static Optional<Map> scanGitHubAction(Map<?, ?> githubAction,
      Map<String, Predicate<String>> matchers) {
    if (matchers.isEmpty()) {
      return Optional.empty();
    }

    return Optional.ofNullable(githubAction.get("jobs"))
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(jobs -> jobs.values())
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .map(jobs -> scanJobs(jobs, matchers))
        .orElse(Optional.empty());
  }

  /**
   * Search for the given predicate matches in the given list of action jobs.
   *
   * @param jobs Iterable list of GitHub action jobs.
   * @param matchers predicates to match through the given action.
   * @return Optional<{@link Map}> step if one of the predicate finds matches.
   *         Optional.empty otherwise.
   */
  private static Optional<Map> scanJobs(Iterable<?> jobs, Map<String, Predicate<String>> matchers) {
    return IteratorUtils.toList(jobs.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(job -> job.get("steps"))
        .filter(List.class::isInstance)
        .map(List.class::cast)
        .flatMap(List<?>::stream)
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .filter(step -> MATCH_MAP_PREDICATE.test(step, matchers))
        .findAny();
  }

  /**
   * Checks if a GitHub action runs on pull requests.
   *
   * @param githubAction A config of the action.
   * @return True if the action runs on pull requests, false otherwise.
   */
  private static boolean runsOnPullRequests(Map<?, ?> githubAction) {
    return runsOnPullRequestArrayType(githubAction)
        || runsOnPullRequestMapType(githubAction);
  }

  /**
   * Checks if a GitHub action runs on pull requests is defined as Array.
   *
   * @param githubAction A config of the action.
   * @return True if the action runs on pull requests, false otherwise.
   */
  private static boolean runsOnPullRequestArrayType(Map<?, ?> githubAction) {
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
  private static boolean runsOnPullRequestMapType(Map<?, ?> githubAction) {
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

  /**
   * Prepare the map of Pre-Commit Hook config contexts.
   *
   * @param matcher The predicate matcher to match against each contexts.
   * @return Map of Context and the associated Predicates.
   */
  protected static Map<String, Predicate<String>> preCommitHookContextMap(
      Predicate<String> matcher) {
    Map<String, Predicate<String>> map = new HashMap<>();
    for (String context : PRE_COMMIT_HOOK_CONTEXT) {
      map.put(context, matcher);
    }
    return map;
  }

  /**
   * Checks if a Static Analysis Tool is configured as a Pre-Commit Hook.
   *
   * @param config The Pre-Commit Config file.
   * @param matchers predicates to match through the given config.
   * @return True if one of the predicate finds matches. False otherwise.
   */
  private static boolean hasPreCommitHook(Map<?, ?> config,
      Map<String, Predicate<String>> matchers) {
    if (matchers.isEmpty()) {
      return false;
    }

    return Optional.ofNullable(config.get("repos"))
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .map(repos -> scanPreCommitRepos(repos, matchers) || scanPreCommitHooks(repos, matchers))
        .orElse(false);
  }

  /*
   * Search for the given predicate matches in the given list of repos.
   *
   * @param repos Iterable list of GitHub repositories config.
   * @param matchers predicates to match through the given action.
   * @return True if one of the predicate finds matches. False otherwise.
   */
  private static boolean scanPreCommitRepos(Iterable<?> repos,
      Map<String, Predicate<String>> matchers) {
    return IteratorUtils.toList(repos.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .anyMatch(repo -> MATCH_MAP_PREDICATE.test(repo, matchers));
  }

  /**
   * Search for the given predicate matches in the given list of repo hooks.
   *
   * @param repos Iterable list of GitHub repositories hooks config.
   * @param matchers predicates to match through the given action.
   * @return True if one of the predicate finds matches. False otherwise.
   */
  private static boolean scanPreCommitHooks(Iterable<?> repos,
      Map<String, Predicate<String>> matchers) {
    return IteratorUtils.toList(repos.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .map(repo -> repo.get("hooks"))
        .filter(Iterable.class::isInstance)
        .map(Iterable.class::cast)
        .anyMatch(hooks -> scanPreCommitHook(hooks, matchers));
  }

  /**
   * Search for the given predicate matches in the given list of repo hook.
   *
   * @param hooks Iterable list of GitHub hooks config.
   * @param matchers predicates to match through the given action.
   * @return True if one of the predicate finds matches. False otherwise.
   */
  private static boolean scanPreCommitHook(Iterable<?> hooks,
      Map<String, Predicate<String>> matchers) {
    return IteratorUtils.toList(hooks.iterator()).stream()
        .filter(Map.class::isInstance)
        .map(Map.class::cast)
        .anyMatch(hook -> MATCH_MAP_PREDICATE.test(hook, matchers));
  }

  /**
   * Search for the given predicate matches in the given ini config file.
   *
   * @param lines Iterable list lines from the ini config.
   * @param matchers predicates to match through the given action.
   * @return True if one of the predicate finds matches. False otherwise.
   */
  private static boolean hasIniConfig(Stream<String> lines,
      Map<String, Predicate<String>> matchers) {
    if (matchers.isEmpty()) {
      return false;
    }

    return lines.anyMatch(line -> MATCH_STRING_PREDICATE.test(line, matchers));
  }

  /**
   * Builds a new set of locations.
   *
   * @param locations A set of locations to be added to the resulting set.
   * @param rest An array of locations to be added to the resulting set.
   * @return A set of locations that contains all passed locations.
   */
  public static EnumSet<Location> in(
      EnumSet<Location> locations, Location... rest) {

    EnumSet<Location> set = EnumSet.copyOf(locations);
    set.addAll(Arrays.asList(rest));
    return set;
  }

  /**
   * Creates a visitor for searching {@link GitHubProject}.
   * 
   * @return {@link Visitor}.
   */
  protected static Visitor withVisitor() {
    return new Visitor();
  }

  /**
   * A visitor for searching a specific config predicate in a {@link GitHubProject}.
   */
  public static class Visitor extends AbstractGitHubVisitor {

    /**
     * A visitor for searching {@link GitHubProject} if the config to run a specific check tool
     * exists.
     */
    public boolean runCheck = false;

    /**
     * A visitor for searching {@link GitHubProject} if the config to use a specific check tool
     * exists.
     */
    public boolean usesCheck = false;
    
    /**
     * A visitor for searching {@link GitHubProject} if the config to use a specific check tool
     * has specific rules.
     */
    public boolean hasRules = false;

    @Override
    public void visitPreCommitHook(LocalRepository repository,
        Map<String, Predicate<String>> matchers, Set<Location> locations) throws IOException {
      Optional<InputStream> content = repository.read(PRE_COMMIT_HOOK_CONFIG);
      if (!content.isPresent()) {
        return;
      }

      Map<String, Object> config = Yaml.readMap(content.get());
      if (hasPreCommitHook(config, matchers)) {
        runCheck = true;
        usesCheck = true;
      }
    }

    @Override
    public void visitIniConfig(LocalRepository repository, Map<String, Predicate<String>> matchers,
        Set<Location> locations) throws IOException {
      for (Path configPath : findIniConfigsIn(repository)) {
        try (Stream<String> lines = Files.lines(configPath)) {
          if (hasIniConfig(lines, matchers)) {
            runCheck = true;
            break;
          }
        }
      }
    }

    @Override
    public void visitGitHubAction(LocalRepository repository,
        Map<String, Predicate<String>> matchers, Map<String, Predicate<String>> configMatchers,
        Set<Location> locations) throws IOException {
      for (Path configPath : findGitHubActionsIn(repository)) {
        try (InputStream content = Files.newInputStream(configPath)) {
          Map<String, Object> githubAction = Yaml.readMap(content);
          Optional<Map> step = scanGitHubAction(githubAction, matchers);
          if (step.isPresent() && !step.get().keySet().isEmpty()) {
            runCheck = true;
            if (runsOnPullRequests(githubAction)) {
              usesCheck = true;
            }
            if (MATCH_MAP_PREDICATE.test(step.get(), configMatchers)) {
              hasRules = true;
              break;
            }
          }
        }
      }
    }
  }
}