package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_MYPY_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_MYPY_SCAN_CHECKS;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyPyDataProviderTest extends TestGitHubDataFetcherHolder {

  private static final GitHubProject PROJECT = new GitHubProject("org", "test");

  private static final String GITHUB_WORKFLOW_FILENAME = ".github/workflows/mypy.yml";

  private static final String GITHUB_PRE_COMMIT_HOOK_CONFIG_FILENAME = ".pre-commit-config.yaml";

  private static final String INI_CONFIG_FILENAME = "pylint.ini";

  private static Path repositoryDirectory;

  private static LocalRepository localRepository;

  @Before
  public void setup() {
    try {
      repositoryDirectory = Files.createTempDirectory(MyPyDataProviderTest.class.getName());
      localRepository = mock(LocalRepository.class);
      TestGitHubDataFetcher.addForTesting(PROJECT, localRepository);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test
  public void testNotInteractive() {
    assertFalse(new MyPyDataProvider(fetcher).interactive());
  }

  @Test
  public void testSupportedFeatures() {
    Set<Feature<?>> features = new MyPyDataProvider(fetcher).supportedFeatures();
    assertEquals(2, features.size());
    assertThat(features, hasItem(RUNS_MYPY_SCANS));
    assertThat(features, hasItem(USES_MYPY_SCAN_CHECKS));
  }

  @Test
  public void testWithPylintRunsAndChecks() throws IOException {
    try (InputStream content = getClass().getResourceAsStream("mypy-analysis-with-run.yml")) {
      testPylintFilesCheck(GITHUB_WORKFLOW_FILENAME, content, RUNS_MYPY_SCANS.value(true),
          USES_MYPY_SCAN_CHECKS.value(true));
    }
  }

  @Test
  public void testWithPylintInRepo() throws IOException {
    try (InputStream content =
        getClass().getResourceAsStream("mypy-analysis-with-pre-commit-hook.yml")) {
      testPylintFileStreamCheck(GITHUB_PRE_COMMIT_HOOK_CONFIG_FILENAME, content,
          RUNS_MYPY_SCANS.value(true), USES_MYPY_SCAN_CHECKS.value(true));
    }
  }

  @Test
  public void testWithMypyProspector() throws IOException {
    try (
        InputStream content = getClass().getResourceAsStream("mypy-analysis-with-prospector.yml")) {
      testPylintFileStreamCheck(GITHUB_PRE_COMMIT_HOOK_CONFIG_FILENAME, content,
          RUNS_MYPY_SCANS.value(true), USES_MYPY_SCAN_CHECKS.value(true));
    }
  }

  @Test
  public void testWithMypyIniConfig() throws IOException {
    try (InputStream content = getClass().getResourceAsStream("tox.ini")) {
      testPylintFileCheck(INI_CONFIG_FILENAME, content, RUNS_MYPY_SCANS.value(true),
          USES_MYPY_SCAN_CHECKS.value(false));
    }
  }

  private void testPylintFilesCheck(String filename, InputStream content,
      Value<?>... expectedValues) throws IOException {
    Path file = repositoryDirectory.resolve(filename);
    Files.createDirectories(file.getParent());
    when(localRepository.hasDirectory(any(Path.class))).thenReturn(true);
    IOUtils.copy(content, Files.newOutputStream(file));
    when(localRepository.files(any(), any())).thenReturn(Collections.singletonList(file));

    MyPyDataProvider provider = new MyPyDataProvider(fetcher);
    ValueSet values = provider.fetchValuesFor(PROJECT);

    assertEquals(2, values.size());
    for (Value<?> expectedValue : expectedValues) {
      Optional<? extends Value<?>> something = values.of(expectedValue.feature());
      assertTrue(something.isPresent());
      assertEquals(expectedValue, something.get());
    }
  }

  private void testPylintFileStreamCheck(String filename, InputStream content,
      Value<?>... expectedValues) throws IOException {
    Path file = repositoryDirectory.resolve(filename);
    Files.createDirectories(file.getParent());
    when(localRepository.hasDirectory(any(Path.class))).thenReturn(true);
    IOUtils.copy(content, Files.newOutputStream(file));
    when(localRepository.read(any(String.class)))
        .thenReturn(Optional.of(Files.newInputStream(file)));

    MyPyDataProvider provider = new MyPyDataProvider(fetcher);
    ValueSet values = provider.fetchValuesFor(PROJECT);

    assertEquals(2, values.size());
    for (Value<?> expectedValue : expectedValues) {
      Optional<? extends Value<?>> something = values.of(expectedValue.feature());
      assertTrue(something.isPresent());
      assertEquals(expectedValue, something.get());
    }
  }

  private void testPylintFileCheck(String filename, InputStream content, Value<?>... expectedValues)
      throws IOException {
    Path file = repositoryDirectory.resolve(filename);
    Files.createDirectories(file.getParent());
    when(localRepository.hasDirectory(any(Path.class))).thenReturn(true);
    IOUtils.copy(content, Files.newOutputStream(file));
    when(localRepository.files(any())).thenReturn(Collections.singletonList(file));

    MyPyDataProvider provider = new MyPyDataProvider(fetcher);
    ValueSet values = provider.fetchValuesFor(PROJECT);

    assertEquals(2, values.size());
    for (Value<?> expectedValue : expectedValues) {
      Optional<? extends Value<?>> something = values.of(expectedValue.feature());
      assertTrue(something.isPresent());
      assertEquals(expectedValue, something.get());
    }
  }

  @After
  public void shutdown() {
    try {
      FileUtils.forceDeleteOnExit(repositoryDirectory.toFile());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
