package com.sap.sgs.phosphor.fosstars.data.github;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.HttpException;

/**
 * Helper class for GitHub data providers which pulls the data from a GitHub repository. Also, the
 * class caches the fetched data for a certain amount of time.
 */
public class GitHubDataFetcher {

  /**
   * A logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(GitHubDataFetcher.class);

  /**
   * An {@link ObjectMapper} for serialization.
   */
  private static final ObjectMapper MAPPER = new ObjectMapper();

  /**
   * A type reference for serializing {@link #localRepositoriesInfo}.
   */
  private static final TypeReference<HashMap<URL, LocalRepositoryInfo>> LOCAL_REPOSITORIES_TYPE_REF
      = new TypeReference<HashMap<URL, LocalRepositoryInfo>>() {};

  /**
   * Defines how often new updates should be pulled to a local repository by default.
   */
  private static final Duration DEFAULT_PULL_INTERVAL = Duration.ofDays(1);

  /**
   * The default base directory.
   */
  private static final String DEFAULT_DIRECTORY = ".fosstars/repositories";

  /**
   * The default file for info about local repositories.
   */
  private static final String DEFAULT_LOCAL_REPOSITORIES_INFO_FILE = "local_repositories_info.json";

  /**
   * Maximum size of the cache for local repositories.
   */
  private static final int LOCAL_REPOSITORIES_CACHE_CAPACITY = 100;

  /**
   * This flag doesn't allow exceeding the maximum cache size.
   */
  private static final boolean SCAN_UNTIL_REMOVABLE = true;

  /**
   * Lock to synchronize access to localRepositoriesInfo.
   */
  private static final Object LOCAL_REPOSITORIES_INFO_LOCK = new Object();

  /**
   * An interface to the GitHub API.
   */
  private GitHub github;

  /**
   * A limited capacity cache to store the repository of a {@link GitHubProject}.
   */
  private final GitHubDataCache<GHRepository> repositoryCache = new GitHubDataCache<>();

  /**
   * A cache of local repositories.
   */
  final LRUMap<GitHubProject, LocalRepository> localRepositories
      = new LRUMap<>(LOCAL_REPOSITORIES_CACHE_CAPACITY, SCAN_UNTIL_REMOVABLE);

  /**
   * A base directory.
   */
  private final Path base;

  /**
   * Info about local repositories.
   */
  private final Map<URL, LocalRepositoryInfo> localRepositoriesInfo;

  /**
   * Defines how often new updates should be pulled to a local repository.
   */
  private Duration pullInterval = DEFAULT_PULL_INTERVAL;

  /**
   * Initializes a new data fetcher.
   *
   * @param github An interface to the GitHub API.
   * @throws IOException If something went wrong.
   */
  public GitHubDataFetcher(GitHub github) throws IOException {
    this(github, Paths.get(DEFAULT_DIRECTORY));
  }

  /**
   * Returns the interface to the GitHub API.
   */
  public synchronized GitHub github() {
    return github;
  }

  /**
   * Set the interface to the GitHub API.
   */
  public synchronized void set(GitHub github) {
    Objects.requireNonNull(github, "Hey! An interface to GitHub can not be null!");
    this.github = github;
  }

  /**
   * Initializes a new data fetcher.
   *
   * @param github An interface to the GitHub API.
   * @param base A base directory.
   * @throws IOException If something went wrong.
   */
  public GitHubDataFetcher(GitHub github, Path base) throws IOException {
    Objects.requireNonNull(github, "Hey! An interface to GitHub can not be null!");
    Objects.requireNonNull(base, "Hey! Base directory can not be null!");

    if (!Files.exists(base)) {
      Files.createDirectories(base);
    }

    if (!Files.isDirectory(base)) {
      throw new IOException(String.format("Hey! %s is not a directory!", base));
    }

    this.base = base;
    this.github = github;

    this.localRepositoriesInfo = loadLocalRepositoriesInfo();
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
      LOGGER.error(String.format("Could not fetch commits from %s", project.url()), e);
      return Collections.emptyList();
    }
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
      throw new IOException(String.format("Could not fetch repository %s (null)", project.url()));
    }

    try {
      repository.getDirectoryContent("/");
    } catch (GHFileNotFoundException e) {
      throw new IOException(String.format("Could not fetch content of / in %s", project.url()));
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
  public LocalRepository localRepositoryFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "On no! Project is null!");

    LocalRepository repository = localRepositories.get(project);
    if (repository == null) {
      repository = loadLocalRepositoryFor(project);
      localRepositories.put(project, repository);
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
  private LocalRepository loadLocalRepositoryFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "On no! Project is null!");

    synchronized (LOCAL_REPOSITORIES_INFO_LOCK) {
      LocalRepositoryInfo info = localRepositoriesInfo.get(project.url());
      if (info == null) {
        Path repositoryPath = base
            .resolve(project.organization().name())
            .resolve(project.name());

        info = new LocalRepositoryInfo(
            repositoryPath, Date.from(Instant.now()), project.url());
      }

      if (Files.isRegularFile(info.path())) {
        LOGGER.warn("{} is a file but it should be a directory, let's remove it", info.path());
        Files.delete(info.path());
      }

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
        LOGGER.info("Pulling updates from {} ...", project.url());
        localRepository.pull();
      }

      info.updated(Date.from(Instant.now()));
      localRepositoriesInfo.put(project.url(), info);
      storeLocalRepositoriesInfo();

      return localRepository;
    }
  }

  /**
   * Sets how often new updates should be pulled to a local repository by default.
   */
  public synchronized void pullAfter(Duration duration) {
    Objects.requireNonNull(duration, "Oh no! Duration is null!");
    pullInterval = duration;
  }

  /**
   * Returns true if a repository should be updated, false otherwise.
   */
  protected synchronized boolean shouldUpdate(LocalRepository repository) {
    Objects.requireNonNull(repository, "Oh no! Repository is null!");
    Instant nextUpdate = repository.info().updated().toInstant().plus(pullInterval);
    return nextUpdate.isBefore(Instant.now());
  }

  /**
   * Clones a repository of a specified project.
   *
   * @param project The project.
   * @param path Where the repository should be cloned to.
   * @throws IOException If something went wrong while cloning the repository.
   */
  protected void clone(GitHubProject project, Path path) throws IOException {
    LOGGER.info("Cloning {} ...", project.url());
    try {
      Git.cloneRepository()
          .setURI(project.url().toString())
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
      LOGGER.error(() -> String.format("Could not open a repository at %s", path), e);
      return Optional.empty();
    }
  }

  /**
   * Returns the cache for repositories.
   */
  public GitHubDataCache<GHRepository> repositoryCache() {
    return repositoryCache;
  }

  /**
   * Returns an expiration date for cache entries.
   */
  protected Date expiration() {
    return Date.from(Instant.now().plus(1, ChronoUnit.DAYS)); // tomorrow
  }

  /**
   * Loads information about local repositories.
   *
   * @return The info about local repositories.
   * @throws IOException If something went wrong.
   */
  protected Map<URL, LocalRepositoryInfo> loadLocalRepositoriesInfo() throws IOException {
    synchronized (LOCAL_REPOSITORIES_INFO_LOCK) {
      Path path = base.resolve(DEFAULT_LOCAL_REPOSITORIES_INFO_FILE);

      if (!Files.exists(path)) {
        return new HashMap<>();
      }

      if (!Files.isRegularFile(path)) {
        throw new IOException(String.format("Hey! %s is not a file!", path));
      }

      return MAPPER.readValue(Files.newInputStream(path), LOCAL_REPOSITORIES_TYPE_REF);
    }
  }

  /**
   * Stores info about local repositories.
   *
   * @throws IOException If something went wrong.
   */
  protected void storeLocalRepositoriesInfo() throws IOException {
    synchronized (LOCAL_REPOSITORIES_INFO_LOCK) {
      Files.write(
          base.resolve(DEFAULT_LOCAL_REPOSITORIES_INFO_FILE),
          MAPPER.writeValueAsBytes(localRepositoriesInfo));
    }
  }

  /**
   * Cleans up local repositories.
   *
   * @param strategy Decides which repositories need to cleaned up.
   */
  public void cleanup(CleanupStrategy strategy) {
    Objects.requireNonNull(strategy, "Hey! Cleanup strategy can't be null!");

    LOGGER.info("Cleaning up local repositories ...");

    synchronized (LOCAL_REPOSITORIES_INFO_LOCK) {
      BigInteger total = BigInteger.valueOf(0L);
      for (Map.Entry<URL, LocalRepositoryInfo> entry : localRepositoriesInfo.entrySet()) {
        LocalRepositoryInfo info = entry.getValue();
        if (!Files.exists(info.path())) {
          continue;
        }
        total = total.add(info.repositorySize());
      }

      Set<URL> toBeRemoved = new HashSet<>();
      for (Map.Entry<URL, LocalRepositoryInfo> entry : localRepositoriesInfo.entrySet()) {
        URL url = entry.getKey();
        LocalRepositoryInfo info = entry.getValue();

        if (strategy.shouldBeDeleted(url, info, total)) {
          try {
            FileUtils.deleteDirectory(info.path().toFile());
          } catch (IOException e) {
            LOGGER.error(
                () -> String.format("Could not delete a local repository: %s", info.path()), e);
          }
          toBeRemoved.add(url);
        }
      }
      toBeRemoved.forEach(localRepositoriesInfo::remove);
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
