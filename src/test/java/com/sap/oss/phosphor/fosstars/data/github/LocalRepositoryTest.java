package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.TestUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Test;

public class LocalRepositoryTest {

  @Test
  public void testReadFile() throws IOException, GitAPIException {
    Path directory = Files.createTempDirectory(getClass().getSimpleName());
    try (Repository repository = FileRepositoryBuilder.create(directory.resolve(".git").toFile());
        Git git = new Git(repository)) {

      repository.create();

      final String filename = "file";
      final String content = "test";

      Path path = new File(repository.getDirectory().getParent(), filename).toPath();
      Files.write(path, content.getBytes());
      git.add().addFilepattern(filename).call();
      CommitCommand commit = git.commit();
      commit.setCredentialsProvider(
              new UsernamePasswordCredentialsProvider("test", "don't tell anyone"));
      commit.setMessage("Added " + filename)
          .setSign(false)
          .setAuthor("Mr. Test", "test@test.com")
          .setCommitter("Mr. Test", "test@test.com")
          .call();

      LocalRepository localRepository = new LocalRepository(
          new LocalRepositoryInfo(directory, new Date(), new URL("https://scm/org/test")),
          repository);

      Optional<String> something = localRepository.file("file");
      assertTrue(something.isPresent());
      assertEquals("test", something.get());

      Optional<String> text = localRepository.readTextFrom("file");
      assertTrue(text.isPresent());
      assertEquals("test", text.get());
    } finally {
      FileUtils.forceDeleteOnExit(directory.toFile());
    }
  }

  @Test
  public void testCommitHistory() throws IOException, GitAPIException, InterruptedException {
    Path directory = Files.createTempDirectory(getClass().getName());
    try (Repository repository = FileRepositoryBuilder.create(directory.resolve(".git").toFile());
        Git git = new Git(repository)) {

      repository.create();

      Files.write(
          Paths.get(repository.getDirectory().getParent()).resolve("file"),
          "test".getBytes());
      git.add().addFilepattern("README.md").call();
      CommitCommand commit = git.commit();
      commit.setCredentialsProvider(
              new UsernamePasswordCredentialsProvider("mr.white", "don't tell anyone"));
      commit.setMessage("Old commit")
          .setSign(false)
          .setAuthor("Mr. White", "mr.white@test.com")
          .setCommitter("Mr. White", "mr.white@test.com")
          .call();

      // make sure that the first commit goes after the second one
      Thread.sleep(1000);

      Files.write(
          Paths.get(repository.getDirectory().getParent()).resolve("file"),
          "test".getBytes());
      git.add().addFilepattern("SECURITY.md").call();
      commit = git.commit();
      commit.setCredentialsProvider(
              new UsernamePasswordCredentialsProvider("mr.black", "don't tell anyone"));
      commit.setMessage("Latest commit")
          .setSign(false)
          .setAuthor("Mr. Black", "mr.black@test.com")
          .setCommitter("Mr. Black", "mr.black@test.com")
          .call();

      LocalRepository localRepository = new LocalRepository(
          new LocalRepositoryInfo(directory, new Date(), new URL("https://scm/org/test")),
          repository);

      List<GitCommit> commits = localRepository.commits();
      assertNotNull(commits);
      assertEquals(2, commits.size());
      assertEquals("Mr. Black", commits.get(0).authorName());
      assertEquals("Mr. Black", commits.get(0).committerName());
      assertEquals("Mr. White", commits.get(1).authorName());
      assertEquals("Mr. White", commits.get(1).committerName());

      // the top commit in the history is the newest one
      assertTrue(commits.get(0).date().after(commits.get(1).date()));
      Optional<Commit> firstCommit = localRepository.firstCommit();
      assertTrue(firstCommit.isPresent());
      assertEquals("Mr. White", firstCommit.get().authorName());
      assertEquals("Mr. White", firstCommit.get().committerName());
    } finally {
      FileUtils.forceDeleteOnExit(directory.toFile());
    }
  }

  @Test
  public void testChanges() throws IOException, GitAPIException {
    Path directory = Files.createTempDirectory(getClass().getName());
    try (Repository repository = FileRepositoryBuilder.create(directory.resolve(".git").toFile());
        Git git = new Git(repository)) {

      repository.create();

      TestUtils.commit(
          new HashMap<String, String>() {
            {
              put("App.java", "public class App {}");
              put("One.java", "public class One {}");
              put("Two.java", "public class Two {}");
              put("Three.java", "public class Three {}");
            }
            }, "First commit: init", git);

      TestUtils.commit(
          new HashMap<String, String>() {
            {
              put("App.java", "public class App { /* something new */ }");
            }
            }, "Second commit: updated App", git);

      TestUtils.commit(
          new HashMap<String, String>() {
            {
              put("Other.java", "public class Other {}");
            }
            }, "Third commit: added Other", git);

      LocalRepository localRepository = new LocalRepository(
          new LocalRepositoryInfo(directory, new Date(), new URL("https://scm/org/test")),
          repository);

      List<GitCommit> commits = localRepository.commits();
      assertNotNull(commits);
      assertEquals(3, commits.size());
      assertEquals("Third commit: added Other", commits.get(0).message().get(0));
      assertEquals("Second commit: updated App", commits.get(1).message().get(0));
      assertEquals("First commit: init", commits.get(2).message().get(0));

      localRepository = spy(localRepository);
      Date reviewDate = new Date();
      Predicate<Path> forAllFiles = p -> Files.isRegularFile(p)
          && !p.toString().contains(File.separator + ".git");

      when(localRepository.firstCommitAfter(reviewDate)).thenReturn(Optional.of(commits.get(0)));
      Double changes = localRepository.changedSince(reviewDate, forAllFiles);
      assertEquals(0.0, changes, DELTA);

      when(localRepository.firstCommitAfter(reviewDate)).thenReturn(Optional.of(commits.get(1)));
      changes = localRepository.changedSince(reviewDate, forAllFiles);
      assertEquals(0.2, changes, DELTA);

      when(localRepository.firstCommitAfter(reviewDate)).thenReturn(Optional.of(commits.get(2)));
      changes = localRepository.changedSince(reviewDate, forAllFiles);
      assertEquals(0.4, changes, DELTA);
    } finally {
      FileUtils.forceDeleteOnExit(directory.toFile());
    }
  }
}