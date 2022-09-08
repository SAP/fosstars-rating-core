package com.sap.oss.phosphor.fosstars.data.github;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * The class holds information about repository that was cloned with JGit.
 */
public class LocalRepository implements AutoCloseable {

  /**
   * No changes in the repository.
   */
  private static final double NO_CHANGES = 0.0;

  /**
   * Info about the repository.
   */
  private final LocalRepositoryInfo info;

  /**
   * An instance of {@link Repository} from JGit.
   */
  private final Repository repository;

  /**
   * Initializes a repository.
   *
   * @param info Info about repository.
   * @param repository An instance of {@link Repository} from JGit.
   */
  public LocalRepository(LocalRepositoryInfo info, Repository repository) {
    Objects.requireNonNull(info, "Oh no! Info is null!");
    Objects.requireNonNull(repository, "Oh no! Repository is null!");

    if (!info.path().toAbsolutePath().equals(
        Paths.get(repository.getDirectory().getParent()).toAbsolutePath())) {

      throw new IllegalArgumentException("Oh no! Paths don't match!");
    }

    this.info = info;
    this.repository = repository;
  }

  /**
   * Get info about the repository.
   *
   * @return Info about the repository.
   */
  public LocalRepositoryInfo info() {
    return info;
  }

  /**
   * Returns a list of commits in the repository.
   *
   * @return The list of commits.
   * @throws IOException If something went wrong.
   */
  @JsonIgnore
  public List<GitCommit> commits() throws IOException {
    List<GitCommit> commits = new ArrayList<>();
    try (Git git = new Git(repository)) {
      for (RevCommit rev : git.log().call()) {
        commits.add(new GitCommit(rev));
      }
    } catch (GitAPIException e) {
      throw new IOException("Could not list commits!", e);
    }

    commits.sort(Comparator.comparing(Commit::date).reversed());

    return commits;
  }

  /**
   * Returns a list of commits after a specific date.
   *
   * @param date The date.
   * @return The list of commits.
   * @throws IOException If something went wrong.
   */
  @JsonIgnore
  public List<Commit> commitsAfter(Date date) throws IOException {
    Objects.requireNonNull(date, "Hey! Date can't be null!");

    List<Commit> result = new ArrayList<>();
    for (Commit commit : commits()) {
      if (commit.date().before(date)) {
        break;
      }
      result.add(commit);
    }

    return result;
  }

  /**
   * Returns a list of commits during some time from now.
   *
   * @param duration The duration
   * @return The list of commits.
   * @throws IOException If something went wrong.
   */
  @JsonIgnore
  public List<Commit> commitsWithin(Duration duration) throws IOException {
    Objects.requireNonNull(duration, "Oh no! Duration is null!");
    Date date = Date.from(Instant.now().minus(duration));
    return commitsAfter(date);
  }

  /**
   * Get the first commit in the repository.
   *
   * @return The first commit in the repository.
   * @throws IOException If something went wrong.
   */
  @JsonIgnore
  public Optional<Commit> firstCommit() throws IOException {
    List<GitCommit> commits = commits();

    if (commits.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(commits.get(commits.size() - 1));
  }

  /**
   * Resets the repository in a hard way by calling "git reset --hard".
   *
   * @throws IOException If something went wrong.
   */
  public void reset() throws IOException {
    try (Git git = new Git(repository)) {
      git.reset().setMode(ResetType.HARD).call();
    } catch (GitAPIException e) {
      throw new IOException("Could not reset the repository!", e);
    }
  }

  /**
   * Pulls updates to the local repository.
   *
   * @throws IOException If something went wrong.
   */
  public void pull() throws IOException {
    try (Git git = new Git(repository)) {
      String branch = git.getRepository().getBranch();
      git.pull().setRemote("origin").setRemoteBranchName(branch).call();
    } catch (GitAPIException e) {
      throw new IOException("Could not pull to repository!", e);
    }
  }

  /**
   * Returns a content of a file if it exists.
   *
   * @param file The file name.
   * @return A content of the file.
   * @throws IOException If something went wrong.
   */
  public Optional<String> file(String file) throws IOException {
    Objects.requireNonNull(file, "On no! File name is null!");
    return file(Paths.get(file));
  }

  /**
   * Returns a content of a file if it exists.
   *
   * @param file The file name.
   * @return A content of the file.
   * @throws IOException If something went wrong.
   */
  public Optional<String> file(Path file) throws IOException {
    Objects.requireNonNull(file, "On no! File name is null!");
    Path path = info.path().resolve(file);
    if (!Files.isRegularFile(path)) {
      return Optional.empty();
    }

    try (InputStream is = Files.newInputStream(path)) {
      return Optional.of(IOUtils.toString(is, UTF_8));
    }
  }

  /**
   * Checks if the repository has a specified directory.
   *
   * @param path A path to the directory.
   * @return Ture if the repository contains the specified directory, false otherwise.
   */
  public boolean hasDirectory(Path path) {
    return Files.isDirectory(info.path().resolve(path));
  }

  /**
   * Checks if the repository has a specified file.
   *
   * @param file A path to the file.
   * @return Ture if the repository contains the specified file, false otherwise.
   */
  public boolean hasFile(String file) {
    return Files.isRegularFile(info.path().resolve(Paths.get(file)));
  }

  /**
   * Resolve the repository path for specified file.
   *
   * @param file A path to the file.
   * @return Optional resolved path within the {@link LocalRepository}.
   */
  public Optional<Path> path(String file) {
    return Optional.ofNullable(info.path().resolve(Paths.get(file)));
  }

  /**
   * Returns a stream of a file if it exists.
   *
   * @param file The file name.
   * @return A content of the file.
   * @throws IOException If something went wrong.
   */
  public Optional<InputStream> read(String file) throws IOException {
    Objects.requireNonNull(file, "On no! File name is null!");
    return read(Paths.get(file));
  }

  /**
   * Returns a stream of a file if it exists.
   *
   * @param file The file name.
   * @return A content of the file.
   * @throws IOException If something went wrong.
   */
  public Optional<InputStream> read(Path file) throws IOException {
    Objects.requireNonNull(file, "On no! File name is null!");
    Path path = info.path().resolve(file);
    if (!Files.isRegularFile(path)) {
      return Optional.empty();
    }

    return Optional.of(Files.newInputStream(path));
  }

  /**
   * Reads a file and returns its content as a string.
   *
   * @param file The file.
   * @return Content of the file.
   * @throws IOException If something went wrong.
   */
  public Optional<String> readTextFrom(String file) throws IOException {
    Optional<InputStream> content = read(file);
    if (content.isPresent()) {
      try (InputStream is = content.get()) {
        return Optional.of(IOUtils.toString(is, UTF_8));
      }
    }

    return Optional.empty();
  }

  /**
   * Read lines in a file.
   *
   * @param file A path to the file.
   * @return A list of lines in the file if it exists.
   * @throws IOException If something went wrong.
   */
  public Optional<List<String>> readLinesOf(String file) throws IOException {
    return readLinesOf(Paths.get(file));
  }

  /**
   * Read lines in a file.
   *
   * @param file A path to the file.
   * @return A list of lines in the file if it exists.
   * @throws IOException If something went wrong.
   */
  public Optional<List<String>> readLinesOf(Path file) throws IOException {
    Objects.requireNonNull(file, "On no! File name is null!");
    Path path = info.path().resolve(file);
    if (!Files.isRegularFile(path)) {
      return Optional.empty();
    }

    try (InputStream is = Files.newInputStream(path)) {
      return Optional.of(IOUtils.readLines(is, UTF_8));
    }
  }

  /**
   * Looks for files in the repository.
   *
   * @param criteria Defines which files should be returned.
   * @return A list of files.
   * @throws IOException If something went wrong.
   */
  public List<Path> files(Predicate<Path> criteria) throws IOException {
    Objects.requireNonNull(criteria, "Oh no! Search criteria is null!");

    try (Stream<Path> paths = Files.walk(info().path())) {
      return paths.filter(criteria).collect(Collectors.toList());
    }
  }

  /**
   * Looks for files in a subdirectory of the repository.
   *
   * @param directory A path to the subdirectory.
   * @param criteria Defines which files should be returned.
   * @return A list of files that match the criteria.
   * @throws IOException If something went wrong.
   */
  public List<Path> files(Path directory, Predicate<Path> criteria) throws IOException {
    Objects.requireNonNull(directory, "Oh no! Directory is null!");
    Objects.requireNonNull(criteria, "Oh no! Search criteria is null!");

    try (Stream<Path> paths = Files.walk(info().path().resolve(directory))) {
      return paths.filter(criteria).collect(Collectors.toList());
    }
  }

  @Override
  public void close() {
    repository.close();
  }

  /**
   * Estimate how much the project has changed since a specified date.
   * The method inspects the commit history after the specified date,
   * and looks for updated files.
   * The method doesn't take into account what was exactly updated in the files.
   *
   * @param date The date.
   * @param toConsider Defines which files should be considered.
   * @return An estimated amount of changes in the project.
   * @throws IOException If something went wrong.
   */
  public Double changedSince(Date date, Predicate<Path> toConsider) throws IOException {
    List<GitCommit> commits = commits();

    if (commits.isEmpty()) {
      return NO_CHANGES;
    }

    GitCommit head = commits.get(0);
    Optional<GitCommit> target = firstCommitAfter(date);

    if (!target.isPresent() || target.get().equals(head)) {
      return NO_CHANGES;
    }

    Map<Path, Boolean> fileMap = new HashMap<>();
    files(toConsider).forEach(path -> fileMap.put(path, false));

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
         DiffFormatter diffFormatter = new DiffFormatter(out)) {

      diffFormatter.setRepository(repository);
      for (DiffEntry entry : diffFormatter.scan(target.get().raw(), head.raw())) {
        validEntryPath(entry.getNewPath(), toConsider).ifPresent(path -> fileMap.put(path, true));
        validEntryPath(entry.getOldPath(), toConsider).ifPresent(path -> fileMap.put(path, true));
      }
    }

    long updates = fileMap.entrySet().stream().filter(Entry::getValue).count();
    return (double) updates / (double) fileMap.size();
  }

  /**
   * Looks for the first commit after a specified date.
   *
   * @param date The date.
   * @return The first commit after the specified date if found.
   */
  Optional<GitCommit> firstCommitAfter(Date date) throws IOException {
    int i = 0;
    GitCommit target = null;
    List<GitCommit> commits = commits();
    while (i < commits.size()) {
      if (commits.get(i).date().before(date)) {
        break;
      }
      target = commits.get(i++);
    }
    return Optional.ofNullable(target);
  }

  /**
   * Resolve a path from a diff entry against the repository's root
   * if the path satisfies a specified criteria.
   *
   * @param entryPath The path.
   * @param criteria The criteria.
   * @return A resolved path if all the conditions are met.
   */
  private Optional<Path> validEntryPath(String entryPath, Predicate<Path> criteria) {
    if (entryPath == null || entryPath.equals(DiffEntry.DEV_NULL)) {
      return Optional.empty();
    }

    Path path = info.path().resolve(Paths.get(entryPath));
    return criteria.test(path) ? Optional.of(path) : Optional.empty();
  }
}
