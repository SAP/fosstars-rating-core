package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM_CHECKS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.NoUserCallback;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.kohsuke.github.GHCheckRun;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

public class LgtmDataProviderTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testIfProjectExist() throws IOException {
    LgtmDataProvider provider = new LgtmDataProvider(fetcher);
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    GitHubProject project = new GitHubProject("org", "project");

    GHCheckRun checkRun = mock(GHCheckRun.class);
    when(checkRun.getName()).thenReturn("LGTM analysis");

    PagedIterator<GHCheckRun> checkRunPagedIterator = mock(PagedIterator.class);
    when(checkRunPagedIterator.hasNext()).thenReturn(true, false);
    when(checkRunPagedIterator.next()).thenReturn(checkRun);

    PagedIterable<GHCheckRun> checkRunPagedIterable = mock(PagedIterable.class);
    when(checkRunPagedIterable.iterator()).thenReturn(checkRunPagedIterator);

    GHCommit commit = mock(GHCommit.class);
    when(commit.getCheckRuns()).thenReturn(checkRunPagedIterable);

    GHRepository repository = mock(GHRepository.class);
    when(fetcher.github().getRepository(anyString())).thenReturn(repository);

    PagedIterator<GHCommit> commitPagedIterator = mock(PagedIterator.class);
    when(commitPagedIterator.hasNext()).thenReturn(true, false);
    when(commitPagedIterator.next()).thenReturn(commit);

    PagedIterable<GHCommit> commitPagedIterable = mock(PagedIterable.class);
    when(commitPagedIterable.iterator()).thenReturn(commitPagedIterator);

    when(repository.listCommits()).thenReturn(commitPagedIterable);

    try (InputStream content = getClass().getResourceAsStream("LgtmProjectInfoReply.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.set(NoUserCallback.INSTANCE);
      provider.update(project, values);

      assertEquals(2, values.size());
      assertTrue(values.has(USES_LGTM_CHECKS));
      assertTrue(values.of(USES_LGTM_CHECKS).isPresent());
      assertFalse(values.of(USES_LGTM_CHECKS).get().isUnknown());
      assertEquals(USES_LGTM_CHECKS.value(true), values.of(USES_LGTM_CHECKS).get());

      assertTrue(values.has(WORST_LGTM_GRADE));
      assertTrue(values.of(WORST_LGTM_GRADE).isPresent());
      assertFalse(values.of(WORST_LGTM_GRADE).get().isUnknown());
      assertEquals(WORST_LGTM_GRADE.value(LgtmGrade.C), values.of(WORST_LGTM_GRADE).get());
    }
  }

  @Test
  public void testIfProjectDoesNotExist() throws IOException {
    LgtmDataProvider provider = new LgtmDataProvider(fetcher);
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    GitHubProject project = new GitHubProject("org", "project");

    PagedIterator<GHCheckRun> checkRunPagedIterator = mock(PagedIterator.class);
    when(checkRunPagedIterator.hasNext()).thenReturn(false);

    PagedIterable<GHCheckRun> checkRunPagedIterable = mock(PagedIterable.class);
    when(checkRunPagedIterable.iterator()).thenReturn(checkRunPagedIterator);

    GHCommit commit = mock(GHCommit.class);
    when(commit.getCheckRuns()).thenReturn(checkRunPagedIterable);

    GHRepository repository = mock(GHRepository.class);
    when(fetcher.github().getRepository(anyString())).thenReturn(repository);

    PagedIterator<GHCommit> commitPagedIterator = mock(PagedIterator.class);
    when(commitPagedIterator.hasNext()).thenReturn(true, false);
    when(commitPagedIterator.next()).thenReturn(commit);

    PagedIterable<GHCommit> commitPagedIterable = mock(PagedIterable.class);
    when(commitPagedIterable.iterator()).thenReturn(commitPagedIterator);

    when(repository.listCommits()).thenReturn(commitPagedIterable);

    try (InputStream content =
        getClass().getResourceAsStream("LgtmProjectDoesNotExistReply.json")) {

      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.set(NoUserCallback.INSTANCE);
      provider.update(project, values);

      assertEquals(2, values.size());
      assertTrue(values.has(USES_LGTM_CHECKS));
      assertTrue(values.of(USES_LGTM_CHECKS).isPresent());
      assertFalse(values.of(USES_LGTM_CHECKS).get().isUnknown());
      assertEquals(USES_LGTM_CHECKS.value(false), values.of(USES_LGTM_CHECKS).get());

      assertTrue(values.has(WORST_LGTM_GRADE));
      assertTrue(values.of(WORST_LGTM_GRADE).isPresent());
      assertTrue(values.of(WORST_LGTM_GRADE).get().isUnknown());
    }
  }

}