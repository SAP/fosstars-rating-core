package com.sap.oss.phosphor.fosstars.data.github;

import static java.lang.String.format;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.HttpException;

/**
 * <p>Helper class for GitHub data providers which pulls the data from a GitHub repository.
 * Also, the class caches the fetched data for a certain amount of time.</p>
 * <p>The class is thread-safe.</p>
 */
public class GitHubDataFetcher {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(GitHubDataFetcher.class);

  /**
   * A type reference for serializing {@link #LOCAL_REPOSITORIES_INFO}.
   */
  private static final TypeReference<HashMap<URL, LocalRepositoryInfo>> LOCAL_REPOSITORIES_TYPE_REF
      = new TypeReference<HashMap<URL, LocalRepositoryInfo>>() {};

  /**
   * Defines how often new updates should be pulled to a local repository by default.
   */
  private static final Duration DEFAULT_PULL_INTERVAL = Duration.ofDays(1);

  /**
   * A system property that contains a path to base directory for local repositories.
   */
  static final String REPOSITORIES_BASE_PATH_PROPERTY = "fosstars.github.fetcher.repositories.base";

  /**
   * The default base directory.
   */
  static final Path REPOSITORIES_BASE_PATH = Paths.get(
      System.getProperty(REPOSITORIES_BASE_PATH_PROPERTY, ".fosstars/repositories"));

  /**
   * The default file for info about local repositories.
   */
  private static final Path LOCAL_REPOSITORIES_INFO_FILE
      = REPOSITORIES_BASE_PATH.resolve("local_repositories_info.json");

  /**
   * Maximum size of the cache for local repositories.
   */
  static final int LOCAL_REPOSITORIES_CACHE_CAPACITY = 100;

  /**
   * This flag doesn't allow exceeding the maximum cache size.
   */
  private static final boolean SCAN_UNTIL_REMOVABLE = true;

  /**
   * Defines how often new updates should be pulled to a local repository.
   */
  private static Duration PULL_INTERVAL = DEFAULT_PULL_INTERVAL;

  /**
   * A synchronized cache of local repositories.
   */
  static final Map<URL, LocalRepository> LOCAL_REPOSITORIES
      = Collections.synchronizedMap(
          new LRUMap<>(LOCAL_REPOSITORIES_CACHE_CAPACITY, SCAN_UNTIL_REMOVABLE));

  /**
   * Synchronized map containing info about local repositories.
   */
  static final Map<URL, LocalRepositoryInfo> LOCAL_REPOSITORIES_INFO
      = Collections.synchronizedMap(new HashMap<>());

  static {
    try {
      loadLocalRepositoriesInfo();
    } catch (IOException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  /**
   * An interface to the GitHub API.
   */
  private final GitHub github;

  /**
   * A token for accessing the GitHub API.
   */
  private final String token;

  /**
   * A limited capacity cache to store the repository of a {@link GitHubProject}.
   */
  private final GitHubDataCache<GHRepository> repositoryCache = new GitHubDataCache<>();

  /**
   * Initializes a new data fetcher.
   *
   * @param github An interface to the GitHub API.
   * @param token A token for accessing the GitHub API.
   * @throws IOException If something went wrong.
   */
  public GitHubDataFetcher(GitHub github, String token) throws IOException {
    Objects.requireNonNull(github, "Hey! An interface to GitHub can not be null!");
    Objects.requireNonNull(token, "Hey! Token can't be null!");

    if (!Files.exists(REPOSITORIES_BASE_PATH)) {
      Files.createDirectories(REPOSITORIES_BASE_PATH);
    }

    if (!Files.isDirectory(REPOSITORIES_BASE_PATH)) {
      throw new IOException(format("Hey! %s is not a directory!", REPOSITORIES_BASE_PATH));
    }

    this.github = github;
    this.token = token;
  }

  /**
   * Get an interface to the GitHub API.
   *
   * @return The interface to the GitHub API.
   */
  public synchronized GitHub github() {
    return github;
  }

  /**
   * Get a token for accessing the GitHub API.
   *
   * @return A token for accessing the GitHub API.
   */
  public synchronized String token() {
    return token;
  }

  /**
   * Returns a number of latest commits.
   *
   * @param project The project.
   * @param n The number of commits.
   * @return The list of commits.
   * @throws IOException If something went wrong.
   */
  public List<GHCommit> githubCommitsFor(GitHubProject project, int n) throws IOException {
    Objects.requireNonNull(project, "Oh no! The project is null!");
    if (n <= 0) {
      throw new IllegalArgumentException("Oh no! The number of commit is not positive!");
    }

    try {
      List<GHCommit> commits = new ArrayList<>();
      for (GHCommit commit : repositoryFor(project).listCommits()) {
        commits.add(commit);
        n--;
        if (n == 0) {
          break;
        }
      }

      return commits;
    } catch (HttpException e) {
      LOGGER.error(format("Could not fetch commits from %s", project.scm()), e);
      return Collections.emptyList();
    }
  }

  /**
   * Creates a new GitHub issue in the given project with the provided title and body.
   * 
   * @param project The project that shall receive the new issue.
   * @param title The title of the new issue.
   * @param body The body of the new issue.
   * @return The newly created issue.
   * @throws IOException If something went wrong.
   */
  public GHIssue createGitHubIssue(GitHubProject project, String title, String body)
      throws IOException {
    Objects.requireNonNull(project, "Oh no! The project is null!");
    if (title == null || title.isEmpty()) {
      throw new IllegalArgumentException("Oh no! The issue title is invalid!");
    }
    if (body == null || body.isEmpty()) {
      throw new IllegalArgumentException("Oh no! The issue body is invalid!");
    }
    
    GHRepository gitHubRepository = repositoryFor(project);
    GHIssueBuilder issueBuilder = gitHubRepository.createIssue(title);
    issueBuilder.body(body);
    return issueBuilder.create();
    
  }

  /**
   * Search existing GitHub issues in the given project.
   * 
   * @param project The project that shall be searched for issues.
   * @param text The text that shall be used for the search.
   * @return A list of found issues. Empty if search was unsuccessful.
   * @throws IOException If something went wrong.
   */
  public List<GHIssue> gitHubIssuesFor(GitHubProject project, String text) throws IOException {
    Objects.requireNonNull(project, "Oh no! The project is null!");
    if (StringUtils.isEmpty(text)) {
      throw new IllegalArgumentException("Oh no! The search query is invalid!");
    }

    String searchQuery = format("%s repo:%s/%s",
        text, project.organization().name(), project.name());
    List<GHIssue> issues = new ArrayList<>();
    for (GHIssue issue : github().searchIssues().isOpen().q(searchQuery).list()) {
      issues.add(issue);
    }

    return issues;
  }

  /**
   * Gets the GitHub project repository.
   * This repository will then be stored in a cache ({@link LRUMap}).
   *
   * @param project of type {@link GitHubProject}, which holds the project information.
   * @return {@link GHRepository} with the project information.
   * @throws IOException occurred during REST call to GitHub API.
   */
  public GHRepository repositoryFor(GitHubProject project) throws IOException {
    Optional<GHRepository> cachedRepository = repositoryCache.get(project);
    if (cachedRepository.isPresent()) {
      return cachedRepository.get();
    }

    GHRepository repository = github().getRepository(project.path());
    if (repository == null) {
      throw new IOException(format("Could not fetch repository %s (null)", project.scm()));
    }

    try {
      repository.getDirectoryContent("/");
    } catch (GHFileNotFoundException e) {
      throw new IOException(format("Could not fetch content of / in %s", project.scm()));
    }

    repositoryCache.put(project, repository, expiration());

    return repository;
  }

  /**
   * Clones a repository of a specified project.
   *
   * @param project The project.
   * @return A local repository.
   * @throws IOException If something went wrong while cloning the repository.
   */
  public static LocalRepository localRepositoryFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "On no! Project is null!");
    LocalRepository repository = LOCAL_REPOSITORIES.get(project.scm());

    if (repository == null) {
      repository = loadLocalRepositoryFor(project);
      LOCAL_REPOSITORIES.put(project.scm(), repository);
    }

    return repository;
  }

  /**
   * Loads a repository of a specified project.
   *
   * @param project The project.
   * @return A local repository.
   * @throws IOException If something went wrong.
   */
  private static LocalRepository loadLocalRepositoryFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "On no! Project is null!");

    synchronized (LOCAL_REPOSITORIES_INFO) {
      LocalRepositoryInfo info = LOCAL_REPOSITORIES_INFO.get(project.scm());
      if (info == null) {
        Path repositoryPath
            = REPOSITORIES_BASE_PATH.resolve(project.organization().name()).resolve(project.name());
        info = new LocalRepositoryInfo(repositoryPath, Date.from(Instant.now()), project.scm());
      }

      if (Files.isRegularFile(info.path())) {
        LOGGER.warn("{} is a file but it should be a directory, let's remove it", info.path());
        Files.delete(info.path());
      }

      try {
        Optional<Repository> repository = openRepository(info.path());
        if (!repository.isPresent()) {
          Files.deleteIfExists(info.path());
          clone(project, info.path());
          repository = openRepository(info.path());
        }

        if (!repository.isPresent()) {
          throw new IOException("Could not fetch project's repository!");
        }

        LocalRepository localRepository = new LocalRepository(info, repository.get());

        if (shouldUpdate(localRepository)) {
          LOGGER.info("Pulling updates from {} ...", project.scm());
          localRepository.reset();
          localRepository.pull();
        }

        info.updated(Date.from(Instant.now()));
        LOCAL_REPOSITORIES_INFO.put(project.scm(), info);
        return localRepository;
      } catch (IOException e) {

        // if something went wrong, then clean up info and cache,
        // and remove the local repository if it exists
        LOCAL_REPOSITORIES_INFO.remove(project.scm());
        LOCAL_REPOSITORIES.remove(project.scm());
        Files.deleteIfExists(info.path());

        // then, re-throw the original exception
        throw e;
      } finally {
        storeLocalRepositoriesInfo();
      }
    }
  }

  /**
   * Sets how often new updates should be pulled to a local repository by default.
   *
   * @param duration The interval.
   */
  public static synchronized void pullAfter(Duration duration) {
    Objects.requireNonNull(duration, "Oh no! Duration is null!");
    PULL_INTERVAL = duration;
  }

  /**
   * Checks if a repository should be updated.
   *
   * @param repository The repository.
   * @return True if the repository should be updated, false otherwise.
   */
  public static synchronized boolean shouldUpdate(LocalRepository repository) {
    Objects.requireNonNull(repository, "Oh no! Repository is null!");
    Instant nextUpdate = repository.info().updated().toInstant().plus(PULL_INTERVAL);
    return nextUpdate.isBefore(Instant.now());
  }

  /**
   * Clones a repository of a specified project.
   *
   * @param project The project.
   * @param path Where the repository should be cloned to.
   * @throws IOException If something went wrong while cloning the repository.
   */
  private static void clone(GitHubProject project, Path path) throws IOException {
    LOGGER.info("Cloning {} ...", project.scm());
    try {
      Git.cloneRepository()
          .setURI(project.scm().toString())
          .setDirectory(path.toFile())
          .call();
    } catch (GitAPIException e) {
      throw new IOException("Could not clone repository!", e);
    }
  }

  /**
   * Opens a local repository.
   *
   * @param path A path to the repository.
   * @return The repository.
   */
  private static Optional<Repository> openRepository(Path path) {
    if (!Files.exists(path)) {
      return Optional.empty();
    }

    if (!Files.isDirectory(path)) {
      return Optional.empty();
    }

    try {
      return Optional.of(
          new FileRepositoryBuilder()
              .setGitDir(path.resolve(".git").toFile())
              .setMustExist(true)
              .build());
    } catch (IOException e) {
      LOGGER.error(() -> format("Could not open a repository at %s", path), e);
      return Optional.empty();
    }
  }

  /**
   * Get the cache of repositories.
   *
   * @return The cache of repositories.
   */
  GitHubDataCache<GHRepository> repositoryCache() {
    return repositoryCache;
  }

  /**
   * Get an expiration date for the cache entries.
   *
   * @return An expiration date for cache entries.
   */
  public Date expiration() {
    return Date.from(Instant.now().plus(1, ChronoUnit.DAYS)); // tomorrow
  }

  /**
   * Loads information about local repositories.
   *
   * @throws IOException If something went wrong.
   */
  static void loadLocalRepositoriesInfo() throws IOException {
    synchronized (LOCAL_REPOSITORIES_INFO) {
      if (!Files.exists(LOCAL_REPOSITORIES_INFO_FILE)) {
        return;
      }

      if (!Files.isRegularFile(LOCAL_REPOSITORIES_INFO_FILE)) {
        throw new IOException(
            format("Hey! %s is not a file!", LOCAL_REPOSITORIES_INFO_FILE));
      }

      try (InputStream is = Files.newInputStream(LOCAL_REPOSITORIES_INFO_FILE)) {
        LOCAL_REPOSITORIES_INFO.clear();
        LOCAL_REPOSITORIES_INFO.putAll(
            Json.mapper().readValue(is, LOCAL_REPOSITORIES_TYPE_REF));
      }
    }
  }

  /**
   * Stores info about local repositories.
   *
   * @throws IOException If something went wrong.
   */
  private static void storeLocalRepositoriesInfo() throws IOException {
    synchronized (LOCAL_REPOSITORIES_INFO) {
      Files.write(
          LOCAL_REPOSITORIES_INFO_FILE,
          Json.toBytes(LOCAL_REPOSITORIES_INFO));
    }
  }

  /**
   * Cleans up local repositories.
   *
   * @param strategy Decides which repositories need to cleaned up.
   * @throws IOException If something goes wrong.
   */
  public void cleanup(CleanupStrategy strategy) throws IOException {
    Objects.requireNonNull(strategy, "Hey! Cleanup strategy can't be null!");

    LOGGER.info("Cleaning up local repositories ...");

    synchronized (LOCAL_REPOSITORIES_INFO) {
      BigInteger total = BigInteger.valueOf(0L);
      for (Map.Entry<URL, LocalRepositoryInfo> entry : LOCAL_REPOSITORIES_INFO.entrySet()) {
        LocalRepositoryInfo info = entry.getValue();
        if (!Files.exists(info.path())) {
          continue;
        }
        total = total.add(info.repositorySize());
      }

      Set<URL> toBeRemoved = new HashSet<>();
      for (Map.Entry<URL, LocalRepositoryInfo> entry : LOCAL_REPOSITORIES_INFO.entrySet()) {
        URL url = entry.getKey();
        LocalRepositoryInfo info = entry.getValue();

        if (strategy.shouldBeDeleted(url, info, total)) {
          LocalRepository repository = LOCAL_REPOSITORIES.get(url);
          if (repository != null) {
            repository.close();
          }

          try {
            FileUtils.deleteDirectory(info.path().toFile());
          } catch (IOException e) {
            LOGGER.error(
                () -> format("Could not delete a local repository: %s", info.path()), e);
          }

          toBeRemoved.add(url);
        }
      }

      toBeRemoved.forEach(LOCAL_REPOSITORIES::remove);
      toBeRemoved.forEach(LOCAL_REPOSITORIES_INFO::remove);
      storeLocalRepositoriesInfo();
    }
  }

  /**
   * An interface of a cleanup strategy.
   */
  public interface CleanupStrategy {

    /**
     * Decides if a repository should be cleaned up.
     *
     * @param url An URL to the project.
     * @param info Info about the repository.
     * @param total A total size of all local repositories.
     * @return True if the repository should be cleaned up, false otherwise.
     */
    boolean shouldBeDeleted(URL url, LocalRepositoryInfo info, BigInteger total);
  }

}
