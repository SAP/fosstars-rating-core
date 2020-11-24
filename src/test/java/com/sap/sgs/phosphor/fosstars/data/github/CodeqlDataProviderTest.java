package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.RUNS_CODEQL_SCANS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_CODEQL_CHECKS;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class CodeqlDataProviderTest extends TestGitHubDataFetcherHolder {

  private static final GitHubProject PROJECT = new GitHubProject("org", "test");

  @Test
  public void testNotInteractive() {
    assertFalse(new CodeqlDataProvider(fetcher).interactive());
  }

  @Test
  public void testSupportedFeatures() {
    Set<Feature> features = new CodeqlDataProvider(fetcher).supportedFeatures();
    assertEquals(2, features.size());
    assertThat(features, hasItem(RUNS_CODEQL_SCANS));
    assertThat(features, hasItem(USES_CODEQL_CHECKS));
  }

  @Test
  public void testWithoutCodeqlRuns() throws IOException {
    try (InputStream content = getClass().getResourceAsStream("no-codeql-analysis.yml")) {
      testCodeqlRuns(".github/workflows/action.yml", content,
          USES_CODEQL_CHECKS.value(false), RUNS_CODEQL_SCANS.value(false));
    }
  }

  @Test
  public void testWithCodeqlRunsAndChecks() throws IOException {
    try (InputStream content = getClass().getResourceAsStream("codeql-analysis-with-pr.yml")) {
      testCodeqlRuns(".github/workflows/codeql.yml", content,
          USES_CODEQL_CHECKS.value(true), RUNS_CODEQL_SCANS.value(true));
    }
  }

  @Test
  public void testWithCodeqlRunsButWithoutChecks() throws IOException {
    try (InputStream content = getClass().getResourceAsStream("codeql-analysis-without-pr.yml")) {
      testCodeqlRuns(".github/workflows/codeql.yml", content,
          USES_CODEQL_CHECKS.value(false), RUNS_CODEQL_SCANS.value(true));
    }
  }

  private void testCodeqlRuns(String filename, InputStream content, Value<?>... expectedValues)
      throws IOException {

    mockFile(filename, String.join("\n", IOUtils.readLines(content)));

    CodeqlDataProvider provider = new CodeqlDataProvider(fetcher);
    ValueSet values = provider.fetchValuesFor(PROJECT);

    assertEquals(2, expectedValues.length);
    for (Value<?> expectedValue : expectedValues) {
      Optional<? extends Value<?>> something = values.of(expectedValue.feature());
      assertTrue(something.isPresent());
      assertEquals(expectedValue, something.get());
    }
  }

  private static void mockFile(String filename, String content) throws IOException {
    LocalRepository repository = mock(LocalRepository.class);
    when(repository.file(filename)).thenReturn(Optional.of(content));
    when(repository.read(Paths.get(filename)))
        .thenReturn(Optional.of(IOUtils.toInputStream(content)));
    when(repository.files(any(), any())).thenReturn(Collections.singletonList(Paths.get(filename)));
    addForTesting(PROJECT, repository);
  }

}