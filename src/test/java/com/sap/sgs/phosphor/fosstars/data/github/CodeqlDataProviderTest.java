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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.BooleanValue;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.kohsuke.github.GHCheckRun;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

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
  public void testWithoutCodeqlCheck() throws IOException {
    testCodeqlCheck("Another check", false);
  }

  @Test
  public void testWithCodeqlCheck() throws IOException {
    testCodeqlCheck("CodeQL / Analyze (java) (pull_request)", true);
  }

  @Test
  public void testWithoutCodeqlRuns() throws IOException {
    try (InputStream content = getClass().getResourceAsStream("no-codeql-analysis.yml")) {
      testCodeqlRuns(".github/workflows/action.yml", content, false);
    }
  }

  @Test
  public void testWithCodeqlRuns() throws IOException {
    try (InputStream content = getClass().getResourceAsStream("codeql-analysis.yml")) {
      testCodeqlRuns(".github/workflows/codeql.yml", content, true);
    }
  }

  private void testCodeqlRuns(String filename, InputStream content, boolean expectedValue)
      throws IOException {

    GHRepository repository = mock(GHRepository.class);
    mockCommitChecks(repository, "Some check");
    mockFile(filename, String.join("\n", IOUtils.readLines(content)));

    when(fetcher.github().getRepository(anyString())).thenReturn(repository);

    CodeqlDataProvider provider = new CodeqlDataProvider(fetcher);

    ValueSet values = provider.fetchValuesFor(PROJECT);

    assertEquals(2, values.size());;
    assertTrue(values.has(RUNS_CODEQL_SCANS));
    Optional<Value> something = values.of(RUNS_CODEQL_SCANS);
    assertTrue(something.isPresent());
    assertTrue(something.get() instanceof BooleanValue);
    Value<Boolean> value = something.get();
    assertEquals(expectedValue, value.get());
  }

  private void testCodeqlCheck(String checkName, boolean expectedValue) throws IOException {
    GHRepository repository = mock(GHRepository.class);
    mockCommitChecks(repository, checkName);
    mockFile(".github/workflows/action.yml", "test");

    when(fetcher.github().getRepository(anyString())).thenReturn(repository);

    CodeqlDataProvider provider = new CodeqlDataProvider(fetcher);

    ValueSet values = provider.fetchValuesFor(PROJECT);

    assertEquals(2, values.size());;
    assertTrue(values.has(USES_CODEQL_CHECKS));
    Optional<Value> something = values.of(USES_CODEQL_CHECKS);
    assertTrue(something.isPresent());
    assertTrue(something.get() instanceof BooleanValue);
    Value<Boolean> value = something.get();
    assertEquals(expectedValue, value.get());
  }

  private static void mockFile(String filename, String content) throws IOException {
    LocalRepository repository = mock(LocalRepository.class);
    when(repository.file(filename)).thenReturn(Optional.of(content));
    when(repository.read(Paths.get(filename)))
        .thenReturn(Optional.of(IOUtils.toInputStream(content)));
    when(repository.files(any())).thenReturn(Collections.singletonList(Paths.get(filename)));
    addForTesting(PROJECT, repository);
  }

  private static void mockCommitChecks(GHRepository repository, String checkName)
      throws IOException {

    GHCheckRun checkRun = mock(GHCheckRun.class);
    when(checkRun.getName()).thenReturn(checkName);

    PagedIterator<GHCheckRun> checkRunPagedIterator = mock(PagedIterator.class);
    when(checkRunPagedIterator.hasNext()).thenReturn(true, false);
    when(checkRunPagedIterator.next()).thenReturn(checkRun);

    PagedIterable<GHCheckRun> checkRunPagedIterable = mock(PagedIterable.class);
    when(checkRunPagedIterable.iterator()).thenReturn(checkRunPagedIterator);

    GHCommit commit = mock(GHCommit.class);
    when(commit.getCheckRuns()).thenReturn(checkRunPagedIterable);

    PagedIterator<GHCommit> commitPagedIterator = mock(PagedIterator.class);
    when(commitPagedIterator.hasNext()).thenReturn(true, false);
    when(commitPagedIterator.next()).thenReturn(commit);

    PagedIterable<GHCommit> commitPagedIterable = mock(PagedIterable.class);
    when(commitPagedIterable.iterator()).thenReturn(commitPagedIterator);

    when(repository.listCommits()).thenReturn(commitPagedIterable);
  }
}