package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.FuzzedInOssFuzz.OSS_FUZZ_PROJECT;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.FUZZED_IN_OSS_FUZZ;
import static org.junit.Assert.assertEquals;

import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;

public class FuzzedInOssFuzzTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testFetchValueFor() throws IOException, GitAPIException {
    Path directory = Files.createTempDirectory(getClass().getSimpleName());
    try (Repository repository = FileRepositoryBuilder.create(directory.resolve(".git").toFile());
        Git git = new Git(repository)) {

      repository.create();

      Path projectDirectory = Paths.get("projects").resolve("project");
      Files.createDirectories(projectDirectory);

      Path dockerFilePath = new File(repository.getDirectory().getParent(), "Dockerfile").toPath();
      String content = "git clone https://github.com/test/project";
      Files.write(dockerFilePath, content.getBytes());

      git.add().addFilepattern("projects/project/Dockerfile").call();
      git.commit()
          .setMessage("Added Dockerfile")
          .call();

      LocalRepository localRepository = new LocalRepository(
          new LocalRepositoryInfo(directory, new Date(), OSS_FUZZ_PROJECT.url()),
          repository
      );
      fetcher.addForTesting(OSS_FUZZ_PROJECT, localRepository);

      FuzzedInOssFuzz provider = new FuzzedInOssFuzz(fetcher);
      provider.set(new GitHubProjectValueCache());

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