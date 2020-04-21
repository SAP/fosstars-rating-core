package com.sap.sgs.phosphor.fosstars.tool.github;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.map.LRUMap;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

/**
 * Helper class for GitHub data providers which pulls the data from a GitHub repository. Also, the
 * class caches the fetched data for a certain amount of time.
 */
public class GitHubDataFetcher {

  /**
   * The singleton instance.
   */
  private static final GitHubDataFetcher INSTANCE = new GitHubDataFetcher();

  /**
   * {@link Comparator} which compares {@link Date} object.
   */
  private static final Comparator<GHCommit> BY_COMMIT_DATE = (firstCommit, secondCommit) -> {
    try {
      return firstCommit.getCommitDate().compareTo(secondCommit.getCommitDate());
    } catch (IOException e) {
      throw new UncheckedIOException("Could not get date from the commit.", e);
    }
  };

  /**
   * A limited capacity cache to store all the commits of a {@link GitHubProject}.
   */
  final GitHubDataCache<List<GHCommit>> commitsCache = new GitHubDataCache<>();

  /**
   * A limited capacity cache to store the repository of a {@link GitHubProject}.
   */
  final GitHubDataCache<GHRepository> repositoryCache = new GitHubDataCache<>();

  /**
   * A private constructor for singleton pattern.
   */
  private GitHubDataFetcher() {
    // private constructor
  }

  /**
   * Creates a single instance of this helper class.
   * 
   * @return instance of this data pull helper.
   */
  public static GitHubDataFetcher instance() {
    return INSTANCE;
  }
  
  /**
   * Gets the list of commits for a GitHub project repository using GitHub API. Then, the commit
   * list is sorted in reverse order by commit date. This will ensure that the commit list will hold
   * data from latest commit to the first commit. This sorted list will then be stored in a cache
   * ({@link LRUMap}).
   * 
   * @param project of type {@link GitHubProject}, which holds the project information.
   * @param github of type {@link GitHub} connection to GitHub API.
   * @return List of {@link GHCommit}.
   * @throws IOException occurred during REST call to GitHub API.
   */
  public List<GHCommit> commitsFor(GitHubProject project, GitHub github) throws IOException {
    Optional<List<GHCommit>> cachedCommits = commitsCache.get(project);
    if (cachedCommits.isPresent()) {
      return cachedCommits.get();
    }

    List<GHCommit> commits = new ArrayList<>(commitsFor(repositoryFor(project, github)));
    commits.sort(BY_COMMIT_DATE.reversed());
    commitsCache.put(project, commits, expiration());
    return commits;
  }

  /**
   * Gets the list of commits for a GitHub project repository using GitHub API.
   * 
   * @param repository The {@link GHRepository}.
   * @return List of {@link GHCommit}.
   * @throws IOException occurred during REST call to GitHub API.
   */
  public List<GHCommit> commitsFor(GHRepository repository) throws IOException {
    return repository.listCommits().toList();
  }

  /**
   * Gets the GitHub project repository. This repository will then be stored in a cache
   * ({@link LRUMap}).
   * 
   * @param project of type {@link GitHubProject}, which holds the project information.
   * @param github of type {@link GitHub} connection to GitHub API.
   * @return {@link GHRepository} with the project information.
   * @throws IOException occurred during REST call to GitHub API.
   */
  public GHRepository repositoryFor(GitHubProject project, GitHub github) throws IOException {
    Optional<GHRepository> cachedRepository = repositoryCache.get(project);
    if (cachedRepository.isPresent()) {
      return cachedRepository.get();
    }

    GHRepository repository = github.getRepository(project.path());
    repositoryCache.put(project, repository, expiration());
    return repository;
  }

  /**
   * Gets the first commit of the repository.
   * 
   * @param project of type {@link GitHubProject}, which holds the project information.
   * @param github of type {@link GitHub} connection to GitHub API.
   * @return {@link GHCommit} with the project information.
   * @throws IOException occurred during REST call to GitHub API.
   */
  public Optional<GHCommit> firstCommitFor(GitHubProject project, GitHub github)
      throws IOException {
    List<GHCommit> commits = commitsFor(project, github);
    if (commits.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(commits.get(commits.size() - 1));
  }

  /**
   * Get a list of commits which comes after a specific date.
   * 
   * @param date which is used as boundary.
   * @param project of type {@link GitHubProject}, which holds the project information.
   * @param github of type {@link GitHub} connection to GitHub API.
   * @return sub-list of type {@link GHCommit}.
   * @throws IOException occurred during REST call to GitHub API.
   */
  public List<GHCommit> commitsAfter(Date date, GitHubProject project, GitHub github)
      throws IOException {
    List<GHCommit> subList = new ArrayList<>();

    for (GHCommit commit : commitsFor(project, github)) {
      if (commit.getCommitDate().before(date)) {
        break;
      }
      subList.add(commit);
    }

    return subList;
  }

  /**
   * Returns an expiration date for cache entries.
   */
  protected Date expiration() {
    return Date.from(Instant.now().plus(1, ChronoUnit.DAYS)); // tomorrow
  }
}