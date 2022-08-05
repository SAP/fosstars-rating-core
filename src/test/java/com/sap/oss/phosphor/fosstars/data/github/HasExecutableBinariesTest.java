package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_EXECUTABLE_BINARIES;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HasExecutableBinariesTest extends TestGitHubDataFetcherHolder {

  private static Path BASE_DIR;

  private static final GitHubProject PROJECT = new GitHubProject("org", "test");

  private static LocalRepository LOCAL_REPOSITORY;

  @BeforeClass
  public static void setup() {
    try {
      BASE_DIR = Files.createTempDirectory(HasExecutableBinariesTest.class.getName());
      Path git = BASE_DIR.resolve(".git");
      Files.createDirectory(git);
      Path submodule = BASE_DIR.resolve("submodule");
      Files.createDirectory(submodule);
      Path packageJson = submodule.resolve("pom.xml");
      Files.write(packageJson, StringUtils.repeat("x", 500).getBytes());

      LocalRepositoryInfo localRepositoryInfo = mock(LocalRepositoryInfo.class);
      when(localRepositoryInfo.path()).thenReturn(BASE_DIR);
      Repository repository = mock(Repository.class);
      when(repository.getDirectory()).thenReturn(git.toFile());

      LOCAL_REPOSITORY = new LocalRepository(localRepositoryInfo, repository);
      LOCAL_REPOSITORY = spy(LOCAL_REPOSITORY);
      when(LOCAL_REPOSITORY.info()).thenReturn(localRepositoryInfo);

      TestGitHubDataFetcher.addForTesting(PROJECT, LOCAL_REPOSITORY);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test
  public void testExecutableIsPresent() throws IOException {
    Path exe = BASE_DIR.resolve("game.exe");
    try {
      Files.write(exe, StringUtils.repeat("x", 1000).getBytes());

      HasExecutableBinaries provider = new HasExecutableBinaries(fetcher);
      provider = spy(provider);
      when(provider.loadLocalRepository(PROJECT)).thenReturn(LOCAL_REPOSITORY);

      ValueSet values = new ValueHashSet();
      provider.update(PROJECT, values);

      assertTrue(values.has(HAS_EXECUTABLE_BINARIES));

      Optional<Value<Boolean>> something = values.of(HAS_EXECUTABLE_BINARIES);
      assertTrue(something.isPresent());

      Value<Boolean> value = something.get();
      assertTrue(value.get());
    } finally {
      FileUtils.forceDeleteOnExit(exe.toFile());
    }
  }

  @Test
  public void testExecutableIsNotPresent() throws IOException {
    Path javaFile = BASE_DIR.resolve("Test.java");
    try {
      Files.write(javaFile, StringUtils.repeat("x", 1000).getBytes());

      HasExecutableBinaries provider = new HasExecutableBinaries(fetcher);
      provider = spy(provider);
      when(provider.loadLocalRepository(PROJECT)).thenReturn(LOCAL_REPOSITORY);

      ValueSet values = new ValueHashSet();
      provider.update(PROJECT, values);

      assertTrue(values.has(HAS_EXECUTABLE_BINARIES));

      Optional<Value<Boolean>> something = values.of(HAS_EXECUTABLE_BINARIES);
      assertTrue(something.isPresent());

      Value<Boolean> value = something.get();
      assertFalse(value.get());
    } finally {
      FileUtils.forceDeleteOnExit(javaFile.toFile());
    }

  }

  @AfterClass
  public static void shutdown() {
    try {
      FileUtils.forceDeleteOnExit(BASE_DIR.toFile());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
