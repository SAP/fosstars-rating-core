package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_BANDIT_SCANS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_BANDIT_SCAN_CHECKS;
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

public class BanditDataProviderTest extends TestGitHubDataFetcherHolder {

  private static final GitHubProject PROJECT = new GitHubProject("org", "test");

  private static final String GITHUB_WORKFLOW_FILENAME = ".github/workflows/bandit.yml";

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
    assertFalse(new BanditDataProvider(fetcher).interactive());
  }

  @Test
  public void testSupportedFeatures() {
    Set<Feature<?>> features = new BanditDataProvider(fetcher).supportedFeatures();
    assertEquals(2, features.size());
    assertThat(features, hasItem(RUNS_BANDIT_SCANS));
    assertThat(features, hasItem(USES_BANDIT_SCAN_CHECKS));
  }

  @Test
  public void testWithBanditRunsAndChecks() throws IOException {
    try (InputStream content = getClass().getResourceAsStream("bandit-analysis-with-run.yml")) {
      testBanditRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_BANDIT_SCANS.value(true),
          USES_BANDIT_SCAN_CHECKS.value(true));
    }
  }

  @Test
  public void testWithBanditRunsAndMultipleJobs() throws IOException {
    try (InputStream content = getClass().getResourceAsStream(
        "bandit-analysis-with-multiple-jobs.yml")) {
      testBanditRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_BANDIT_SCANS.value(true),
          USES_BANDIT_SCAN_CHECKS.value(false));
    }
  }

  @Test
  public void testWithNoBanditRunsButInstallBandit() throws IOException {
    try (InputStream content = getClass().getResourceAsStream(
        "bandit-analysis-with-no-bandit-run.yml")) {
      testBanditRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_BANDIT_SCANS.value(false),
          USES_BANDIT_SCAN_CHECKS.value(false));
    }
  }

  @Test
  public void testWithNoBanditRunsButInstallsBanditAndUsesBandit() throws IOException {
    try (InputStream content = getClass().getResourceAsStream(
        "bandit-analysis-with-no-bandit-run-but-uses-bandit.yml")) {
      testBanditRuns(GITHUB_WORKFLOW_FILENAME, content,
          RUNS_BANDIT_SCANS.value(false),
          USES_BANDIT_SCAN_CHECKS.value(false));
    }
  }

  private void testBanditRuns(String filename, InputStream content, Value<?>... expectedValues)
      throws IOException {

    Path file = repositoryDirectory.resolve(filename);
    Files.createDirectories(file.getParent());
    when(localRepository.hasDirectory(any(Path.class))).thenReturn(true);
    IOUtils.copy(content, Files.newOutputStream(file));
    when(localRepository.files(any(), any())).thenReturn(Collections.singletonList(file));

    BanditDataProvider provider = new BanditDataProvider(fetcher);
    ValueSet values = provider.fetchValuesFor(PROJECT);

    assertEquals(2, values.size());
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