package com.sap.sgs.phosphor.fosstars.data.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * The class holds information about repository that was cloned with JGit.
 */
public class LocalRepository implements AutoCloseable {

  /**
   * Info about the repository.
   */
  private final LocalRepositoryInfo info;

  /**
   * A list of commits in the repository.
   */
  private final List<Commit> commits = new ArrayList<>();

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
   * Returns info about the repository.
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
  public List<Commit> commits() throws IOException {
    loadCommitsIfNecessary();
    return new ArrayList<>(commits);
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

    loadCommitsIfNecessary();

    List<Commit> result = new ArrayList<>();
    for (Commit commit : commits) {
      if (commit.date().before(date)) {
        break;
      }
      result.add(commit);
    }

    return result;
  }

  /**
   * Returns the first commit in the repository.
   */
  @JsonIgnore
  public Optional<Commit> firstCommit() throws IOException {
    loadCommitsIfNecessary();

    if (commits.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(commits.get(commits.size() - 1));
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
    commits.clear();
  }

  /**
   * Returns a content of a file if it exists.
   *
   * @param file The file name.
   * @return A content of the file.
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
   */
  public Optional<String> file(Path file) throws IOException {
    Objects.requireNonNull(file, "On no! File name is null!");
    Path path = info.path().resolve(file);
    if (!Files.isRegularFile(path)) {
      return Optional.empty();
    }

    return Optional.of(IOUtils.toString(Files.newInputStream(path)));
  }

  /**
   * Returns a stream of a file if it exists.
   *
   * @param file The file name.
   * @return A content of the file.
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
   */
  public Optional<InputStream> read(Path file) throws IOException {
    Objects.requireNonNull(file, "On no! File name is null!");
    Path path = info.path().resolve(file);
    if (!Files.isRegularFile(path)) {
      return Optional.empty();
    }

    return Optional.of(Files.newInputStream(path));
  }

  @Override
  public void close() {
    repository.close();
  }

  /**
   * Loads commits in the repository.
   *
   * @throws IOException If something went wrong.
   */
  private void loadCommitsIfNecessary() throws IOException {
    if (!commits.isEmpty()) {
      return;
    }

    try (Git git = new Git(repository)) {
      for (RevCommit rev : git.log().call()) {
        commits.add(new GitCommit(rev));
      }
    } catch (GitAPIException e) {
      throw new IOException("Could not list commits!", e);
    }

    commits.sort(Comparator.comparing(Commit::date).reversed());
  }
}
