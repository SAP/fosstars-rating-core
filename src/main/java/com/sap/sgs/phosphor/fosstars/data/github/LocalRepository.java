package com.sap.sgs.phosphor.fosstars.data.github;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
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
   * A date when the repository was updated.
   */
  private Date updated;

  /**
   * A path to the repository.
   */
  private final Path path;

  /**
   * A list of commits in the repository.
   */
  @JsonIgnore
  private final List<Commit> commits = new ArrayList<>();

  /**
   * An instance of {@link Repository} from JGit.
   */
  @JsonIgnore
  private final Repository repository;

  /**
   * Initializes a repository.
   *
   * @param path A path to the repository.
   * @param repository An instance of {@link Repository} from JGit.
   */
  public LocalRepository(Path path, Repository repository) {
    this(path, Date.from(Instant.now()), repository);
  }

  /**
   * Initializes a repository.
   *
   * @param updated A date when the repository was updated.
   * @param path A path to the repository.
   */
  @JsonCreator
  public LocalRepository(
      @JsonProperty("path") Path path,
      @JsonProperty("updated") Date updated) {

    this(path, updated, null);
  }

  /**
   * Initializes a repository.
   *
   * @param updated A date when the repository was updated.
   * @param path A path to the repository.
   * @param repository An instance of {@link Repository} from JGit.
   */
  public LocalRepository(Path path, Date updated, Repository repository) {
    Objects.requireNonNull(path, "Hey! Path can't be null!");
    Objects.requireNonNull(updated, "Hey! Date can't be null!");

    this.path = path;
    this.updated = updated;
    this.repository = repository;
  }

  /**
   * Returns a date when the repository was updated.
   */
  @JsonGetter("updated")
  public Date updated() {
    return updated;
  }

  /**
   * Updates the date when the repository was updated.
   *
   * @param date A new date.
   */
  public void updated(Date date) {
    updated = date;
  }

  /**
   * Returns a path to the repository.
   */
  @JsonGetter("path")
  public Path path() {
    return path;
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
  public Optional<String> file(String file) {
    Objects.requireNonNull(file, "On no! File name is null!");
    return file(Paths.get(file));
  }

  /**
   * Returns a content of a file if it exists.
   *
   * @param file The file name.
   * @return A content of the file.
   */
  public Optional<String> file(Path file) {
    Objects.requireNonNull(file, "On no! File name is null!");
    if (!Files.isRegularFile(file)) {
      return Optional.empty();
    }

    try (InputStream is = Files.newInputStream(file)) {
      return Optional.of(IOUtils.toString(is, StandardCharsets.UTF_8.name()));
    } catch (IOException e) {
      return Optional.empty();
    }
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
  }
}
