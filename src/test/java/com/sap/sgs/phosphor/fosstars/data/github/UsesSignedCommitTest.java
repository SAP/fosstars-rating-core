package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_VERIFIED_SIGNED_COMMITS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.data.ValueCache;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.kohsuke.github.GitHub;

public class UsesSignedCommitTest {

  @Test
  public void testUsesSignedCommitsTrue() throws IOException {
    GitHub github = mock(GitHub.class);
    UsesSignedCommits provider =
        new UsesSignedCommits("spring-projects", "spring-integration", github,
            null);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new ValueCache());

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content = getClass().getResourceAsStream("UsesSignedCommitTrue.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.update(values);

      assertEquals(1, values.size());
      assertTrue(values.has(USES_VERIFIED_SIGNED_COMMITS));
      assertTrue(values.of(USES_VERIFIED_SIGNED_COMMITS).isPresent());
      assertFalse(values.of(USES_VERIFIED_SIGNED_COMMITS).get().isUnknown());
      assertEquals(USES_VERIFIED_SIGNED_COMMITS.value(true),
          values.of(USES_VERIFIED_SIGNED_COMMITS).get());
    }
  }

  @Test
  public void testUsesSignedCommitsFalse() throws IOException {
    GitHub github = mock(GitHub.class);
    UsesSignedCommits provider =
        new UsesSignedCommits("spring-projects", "spring-integration", github, null);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new ValueCache());

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content = getClass().getResourceAsStream("UsesSignedCommitFalse.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.update(values);

      assertEquals(1, values.size());
      assertTrue(values.has(USES_VERIFIED_SIGNED_COMMITS));
      assertTrue(values.of(USES_VERIFIED_SIGNED_COMMITS).isPresent());
      assertFalse(values.of(USES_VERIFIED_SIGNED_COMMITS).get().isUnknown());
      assertEquals(USES_VERIFIED_SIGNED_COMMITS.value(false),
          values.of(USES_VERIFIED_SIGNED_COMMITS).get());
    }
  }

  @Test
  public void testUsesSignedUnknown() throws IOException {
    GitHub github = mock(GitHub.class);
    UsesSignedCommits provider =
        new UsesSignedCommits("spring-projects", "spring-integration", github, null);
    provider = spy(provider);
    when(provider.cache()).thenReturn(new ValueCache());

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    when(httpClient.execute(any())).thenThrow(IOException.class);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_VERIFIED_SIGNED_COMMITS));
    assertTrue(values.of(USES_VERIFIED_SIGNED_COMMITS).isPresent());
    assertTrue(values.of(USES_VERIFIED_SIGNED_COMMITS).get().isUnknown());
  }
}
