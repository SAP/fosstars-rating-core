package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.FuzzedInOssFuzz.OSS_FUZZ_PROJECT;
import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static org.junit.Assert.assertEquals;

import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Test;

public class FuzzedInOssFuzzTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testFetchValueFor() throws IOException, GitAPIException {
    Path directory = Files.createTempDirectory(getClass().getSimpleName());
    try (Repository repository = FileRepositoryBuilder.create(directory.resolve(".git").toFile());
        Git git = new Git(repository)) {

      repository.create();

      Path projectDirectory = directory.resolve("projects").resolve("project");
      Files.createDirectories(projectDirectory);

      Path dockerFilePath = new File(repository.getDirectory().getParent(), "Dockerfile").toPath();
      String content = "git clone https://github.com/test/project";
      Files.write(dockerFilePath, content.getBytes());

      git.add().addFilepattern("projects/project/Dockerfile").call();
      CommitCommand commit = git.commit();
      commit.setCredentialsProvider(
              new UsernamePasswordCredentialsProvider("fuzzer", "don't tell anyone"));
      commit.setMessage("Added Dockerfile")
          .setSign(false)
          .setAuthor("Mr. Fuzzer", "fuzzer@test.com")
          .setCommitter("Mr. Fuzzer", "fuzzer@test.com")
          .call();

      LocalRepository localRepository = new LocalRepository(
          new LocalRepositoryInfo(directory, new Date(), OSS_FUZZ_PROJECT.scm()),
          repository
      );
      addForTesting(OSS_FUZZ_PROJECT, localRepository);

      FuzzedInOssFuzz provider = new FuzzedInOssFuzz(fetcher);
      provider.set(new SubjectValueCache());

      assertEquals(
          FUZZED_IN_OSS_FUZZ.value(true),
          provider.fetchValueFor(
              new GitHubProject("test", "project")));

      assertEquals(
          FUZZED_IN_OSS_FUZZ.value(false),
          provider.fetchValueFor(
              new GitHubProject("something", "else")));
    } finally {
      FileUtils.forceDeleteOnExit(directory.toFile());
    }
  }
}