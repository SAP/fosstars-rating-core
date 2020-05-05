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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.kohsuke.github.GHFileNotFoundException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * Helper class for GitHub data providers which pulls the data from a GitHub repository.
 * Also, the class caches the fetched data for a certain amount of time.
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
   * A type reference for serializing {@link #localRepositories}.
   */
  private static final TypeReference<HashMap<URL, LocalRepository>> LOCAL_REPOSITORIES_TYPE_REF
      = new TypeReference<HashMap<URL, LocalRepository>>() {};

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
   * An interface to the GitHub API.
   */
  private final GitHub github;

  /**
   * A limited capacity cache to store the repository of a {@link GitHubProject}.
   */
  private final GitHubDataCache<GHRepository> repositoryCache = new GitHubDataCache<>();

  /**
   * A base directory.
   */
  private final Path base;

  /**
   * Info about local repositories.
   */
  private final Map<URL, LocalRepository> localRepositories;

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
  public GitHub github() {
    return github;
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

    this.localRepositories = loadLocalRepositories();
  }

  /**
   * Gets the GitHub project repository. This repository will then be stored in a cache
   * ({@link LRUMap}).
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

    GHRepository repository = github.getRepository(project.path());
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
   * @return Info about the cloned repository.
   * @throws IOException If something went wrong while cloning the repository.
   */
  public LocalRepository localRepositoryFor(GitHubProject project) throws IOException {
    Objects.requireNonNull(project, "On no! Project is null!");

    LocalRepository repository = localRepositories.get(project.url());
    if (repository == null) {
      repository = clone(project, base);
    } else if (shouldUpdate(repository)) {
      repository.pull();
    }

    repository.updated(Date.from(Instant.now()));

    localRepositories.put(project.url(), repository);
    storeLocalRepositories();

    return repository;
  }

  /**
   * Sets how often new updates should be pulled to a local repository by default.
   */
  public void pullAfter(Duration duration) {
    pullInterval = duration;
  }

  /**
   * Returns true if a repository should be updated, false otherwise.
   */
  boolean shouldUpdate(LocalRepository repository) {
    return repository.updated().toInstant().plus(pullInterval).isBefore(Instant.now());
  }

  /**
   * Clones a repository of a specified project.
   *
   * @param project The project.
   * @param base The base directory where a repository should be created.
   * @return Info about cloned repository.
   * @throws IOException If something went wrong while cloning the repository.
   */
  protected LocalRepository clone(GitHubProject project, Path base) throws IOException {
    Path repositoryPath = base
        .resolve(project.organization().name())
        .resolve(project.name());

    if (Files.exists(repositoryPath)) {
      throw new IOException(String.format("Hey! %s already exists!", repositoryPath));
    }

    try {
      Git.cloneRepository()
          .setURI(project.url().toString())
          .setDirectory(repositoryPath.toFile())
          .call();
    } catch (GitAPIException e) {
      throw new IOException("Could not clone repository!", e);
    }

    Repository repository = new FileRepositoryBuilder()
        .setGitDir(repositoryPath.toFile())
        .readEnvironment()
        .findGitDir()
        .build();

    return new LocalRepository(repositoryPath, repository);
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
  protected Map<URL, LocalRepository> loadLocalRepositories() throws IOException {
    Path path = base.resolve(DEFAULT_LOCAL_REPOSITORIES_INFO_FILE);

    if (!Files.exists(path)) {
      return new HashMap<>();
    }

    if (!Files.isRegularFile(path)) {
      throw new IOException(String.format("Hey! %s is not a file!", path));
    }

    return MAPPER.readValue(Files.newInputStream(path), LOCAL_REPOSITORIES_TYPE_REF);
  }

  /**
   * Stores info about local repositories.
   *
   * @throws IOException If something went wrong.
   */
  protected void storeLocalRepositories() throws IOException {
    Files.write(
        base.resolve(DEFAULT_LOCAL_REPOSITORIES_INFO_FILE),
        MAPPER.writeValueAsBytes(localRepositories));
  }

  /**
   * Cleans up local repositories.
   *
   * @param strategy Decides which repositories need to cleaned up.
   */
  public void cleanup(CleanupStrategy strategy) {
    Objects.requireNonNull(strategy, "Hey! Cleanup strategy can't be null!");

    BigInteger total = BigInteger.valueOf(0L);
    for (Map.Entry<URL, LocalRepository> entry : localRepositories.entrySet()) {
      LocalRepository repository = entry.getValue();
      if (!Files.exists(repository.path())) {
        continue;
      }
      total = total.add(FileUtils.sizeOfAsBigInteger(repository.path().toFile()));
    }

    for (Map.Entry<URL, LocalRepository> entry : localRepositories.entrySet()) {
      URL url = entry.getKey();
      LocalRepository repository = entry.getValue();

      if (strategy.shouldBeDeleted(url, repository, total)) {
        try {
          FileUtils.deleteDirectory(repository.path().toFile());
        } catch (IOException e) {
          LOGGER.error(
              String.format("Could not delete a local repository: %s", repository.path()), e);
        }
        repository.close();
        localRepositories.remove(url);
      }
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
     * @param repository A path to the repository.
     * @param total A total size of all local repositories.
     * @return True if the repository should be cleaned up, false otherwise.
     */
    boolean shouldBeDeleted(URL url, LocalRepository repository, BigInteger total);
  }
}