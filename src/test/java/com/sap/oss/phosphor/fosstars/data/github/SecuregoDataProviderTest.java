package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_SECUREGO_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SECUREGO_SCAN_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_SECUREGO_WITH_RULES;
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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SecuregoDataProviderTest extends TestGitHubDataFetcherHolder {

  private static final GitHubProject PROJECT = new GitHubProject("org", "test");

  private static final String GITHUB_WORKFLOW_FILENAME = ".github/workflows/securego.yml";

  private static Path repositoryDirectory;

  private static LocalRepository localRepository;

  @BeforeClass
  public static void setup() {
    try {
      repositoryDirectory = Files.createTempDirectory(PackageManagementTest.class.getName());
      localRepository = mock(LocalRepository.class);
      TestGitHubDataFetcher.addForTesting(PROJECT, localRepository);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Test
  public void testNotInteractive() {
    assertFalse(new SecuregoDataProvider(fetcher).interactive());
  }

  @Test
  public void testSupportedFeatures() {
    Set<Feature<?>> features = new SecuregoDataProvider(fetcher).supportedFeatures();
    assertEquals(3, features.size());
    assertThat(features, hasItem(RUNS_SECUREGO_SCANS));
    assertThat(features, hasItem(USES_SECUREGO_SCAN_CHECKS));
    assertThat(features, hasItem(USES_SECUREGO_WITH_RULES));
  }

  @Test
  public void testWithSecuregoInUsesAndChecks() throws IOException {
    try (InputStream content = getClass().getResourceAsStream(
        "securego-analysis-with-uses.yml")) {
      testSecuregoRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_SECUREGO_SCANS.value(true),
          USES_SECUREGO_SCAN_CHECKS.value(true),
          USES_SECUREGO_WITH_RULES.value(false));
    }
  }

  @Test
  public void testWithSecuregoRunsWithRulesAndMultipleJobs() throws IOException {
    try (InputStream content = getClass().getResourceAsStream(
        "securego-analysis-with-multiple-jobs.yml")) {
      testSecuregoRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_SECUREGO_SCANS.value(true),
          USES_SECUREGO_SCAN_CHECKS.value(true),
          USES_SECUREGO_WITH_RULES.value(true));
    }
  }

  @Test
  public void testWithNoSecuregoRunStep() throws IOException {
    try (InputStream content = getClass().getResourceAsStream(
        "securego-analysis-with-no-securego-run.yml")) {
      testSecuregoRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_SECUREGO_SCANS.value(false),
          USES_SECUREGO_SCAN_CHECKS.value(false),
          USES_SECUREGO_WITH_RULES.value(false));
    }
  }

  @Test
  public void testWithSecuregoRunsAndRulesInDifferentStep() throws IOException {
    try (InputStream content = getClass().getResourceAsStream(
        "securego-analysis-with-rules-in-different-step.yml")) {
      testSecuregoRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_SECUREGO_SCANS.value(true),
          USES_SECUREGO_SCAN_CHECKS.value(false),
          USES_SECUREGO_WITH_RULES.value(false));
    }
  }

  @Test
  public void testWithSecuregoRunAndChecks() throws IOException {
    try (InputStream content = getClass().getResourceAsStream(
        "securego-analysis-with-run.yml")) {
      testSecuregoRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_SECUREGO_SCANS.value(true),
          USES_SECUREGO_SCAN_CHECKS.value(true),
          USES_SECUREGO_WITH_RULES.value(false));
    }
  }

  @Test
  public void testWithSecuregoRunAndChecksWithRules() throws IOException {
    try (InputStream content = getClass().getResourceAsStream(
        "securego-analysis-run-with-rules.yml")) {
      testSecuregoRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_SECUREGO_SCANS.value(true),
          USES_SECUREGO_SCAN_CHECKS.value(true),
          USES_SECUREGO_WITH_RULES.value(true));
    }
  }

  @Test
  public void testWithSecuregoRunAndChecksWithoutRules() throws IOException {
    try (InputStream content = getClass().getResourceAsStream(
        "securego-analysis-run-without-rules.yml")) {
      testSecuregoRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_SECUREGO_SCANS.value(true),
          USES_SECUREGO_SCAN_CHECKS.value(true),
          USES_SECUREGO_WITH_RULES.value(false));
    }
  }

  private void testSecuregoRuns(String filename, InputStream content, Value<?>... expectedValues)
      throws IOException {

    Path file = repositoryDirectory.resolve(filename);
    Files.createDirectories(file.getParent());
    when(localRepository.hasDirectory(any(Path.class))).thenReturn(true);
    IOUtils.copy(content, Files.newOutputStream(file));
    when(localRepository.files(any(), any())).thenReturn(Collections.singletonList(file));

    SecuregoDataProvider provider = new SecuregoDataProvider(fetcher);
    ValueSet values = provider.fetchValuesFor(PROJECT);

    assertEquals(3, values.size());
    for (Value<?> expectedValue : expectedValues) {
      Optional<? extends Value<?>> something = values.of(expectedValue.feature());
      assertTrue(something.isPresent());
      assertEquals(expectedValue, something.get());
    }
  }

  @AfterClass
  public static void shutdown() {
    try {
      FileUtils.forceDeleteOnExit(repositoryDirectory.toFile());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}