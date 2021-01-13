package com.sap.oss.phosphor.fosstars.data.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
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
      git.commit()
          .setMessage("Added " + filename)
          .call();

      LocalRepository localRepository = new LocalRepository(
          new LocalRepositoryInfo(directory, new Date(), new URL("https://scm/org/test")),
          repository);

      Optional<String> something = localRepository.file("file");
      assertTrue(something.isPresent());
      assertEquals("test", something.get());

      Optional<InputStream> file = localRepository.read("file");
      assertTrue(file.isPresent());
      try (InputStream is = file.get()) {
        assertEquals("test", IOUtils.toString(is));
      }
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
      git.commit()
          .setMessage("Old commit")
          .setAuthor("Mr. White", "mr.white@test.com")
          .setCommitter("Mr. White", "mr.white@test.com")
          .call();

      // make sure that the first commit goes after the second one
      Thread.sleep(1000);

      Files.write(
          Paths.get(repository.getDirectory().getParent()).resolve("file"),
          "test".getBytes());
      git.add().addFilepattern("SECURITY.md").call();
      git.commit()
          .setMessage("Latest commit")
          .setAuthor("Mr. Black", "mr.black@test.com")
          .setCommitter("Mr. Black", "mr.black@test.com")
          .call();

      LocalRepository localRepository = new LocalRepository(
          new LocalRepositoryInfo(directory, new Date(), new URL("https://scm/org/test")),
          repository);

      List<Commit> commits = localRepository.commits();
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
      FileUtils.deleteDirectory(directory.toFile());
    }
  }
}