package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_LGTM;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.WORST_LGTM_GRADE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.data.NoUserCallback;
import com.sap.sgs.phosphor.fosstars.model.value.LgtmGrade;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

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

    try (InputStream content = getClass().getResourceAsStream("LgtmProjectInfoReply.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.set(NoUserCallback.INSTANCE);
      provider.update(project, values);

      assertEquals(2, values.size());
      assertTrue(values.has(USES_LGTM));
      assertTrue(values.of(USES_LGTM).isPresent());
      assertFalse(values.of(USES_LGTM).get().isUnknown());
      assertEquals(USES_LGTM.value(true), values.of(USES_LGTM).get());

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

    try (InputStream content =
        getClass().getResourceAsStream("LgtmProjectDoesNotExistReply.json")) {

      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.set(NoUserCallback.INSTANCE);
      provider.update(project, values);

      assertEquals(2, values.size());
      assertTrue(values.has(USES_LGTM));
      assertTrue(values.of(USES_LGTM).isPresent());
      assertFalse(values.of(USES_LGTM).get().isUnknown());
      assertEquals(USES_LGTM.value(false), values.of(USES_LGTM).get());

      assertTrue(values.has(WORST_LGTM_GRADE));
      assertTrue(values.of(WORST_LGTM_GRADE).isPresent());
      assertTrue(values.of(WORST_LGTM_GRADE).get().isUnknown());
    }
  }

}