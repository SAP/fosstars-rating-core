package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.subject.oss.NpmArtifact;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

public class ReleaseInfoFromNpmTest {

  private static final NpmArtifact NPM_ARTIFACT =
      new NpmArtifact("identifier", "0.3.0", new GitHubProject("org", "project"));

  @Test
  public void testIfNpmArtifactExist() throws IOException {
    ReleaseInfoFromNpm provider = new ReleaseInfoFromNpm();
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content = getClass().getResourceAsStream("ReleaseInfoFromNpm.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.update(NPM_ARTIFACT, values);

      assertEquals(2, values.size());
      assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
      assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
      assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
      assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().empty());
      assertEquals(24, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
      assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("0.7.1").isPresent());
      assertEquals("0.3.0", values.of(ARTIFACT_VERSION).get().get().version());
    }
  }

  @Test
  public void testIfNpmArtifactNotExist() throws IOException {
    ReleaseInfoFromNpm provider = new ReleaseInfoFromNpm();
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    InputStream content = IOUtils.toInputStream("");
    when(entity.getContent()).thenReturn(content);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(NPM_ARTIFACT, values);

    assertEquals(2, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertTrue(values.of(ARTIFACT_VERSION).get().isUnknown());
  }
}