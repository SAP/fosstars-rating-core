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
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.value.ArtifactVersion;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

public class ReleaseInfoFromMavenTest {

  private static final MavenArtifact MAVEN_ARTIFACT =
      new MavenArtifact("group", "artifact", "1.10.10", new GitHubProject("org", "project"));

  @Test
  public void testIfMavenArtifactExist() throws IOException {
    ReleaseInfoFromMaven provider = new ReleaseInfoFromMaven();
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content = getClass().getResourceAsStream("ReleaseInfoFromMaven.json")) {
      when(entity.getContent()).thenReturn(content);
      processProvider(provider);
    }
  }

  @Test
  public void testIfMavenArtifactNotExist() throws IOException {
    ReleaseInfoFromMaven provider = new ReleaseInfoFromMaven();
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    InputStream content = IOUtils.toInputStream("");
    when(entity.getContent()).thenReturn(content);

    processProviderForUnknownResult(provider);
  }

  @Test
  public void testGetMavenArtifactsByPagination() throws IOException {
    ReleaseInfoFromMaven provider = new ReleaseInfoFromMaven();
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content1 = getClass().getResourceAsStream("ReleaseInfoFromMavenPage1.json");
        InputStream content2 = getClass().getResourceAsStream("ReleaseInfoFromMavenPage2.json")) {
      when(entity.getContent()).thenReturn(content1).thenReturn(content2);
      processProvider(provider);
    }
  }

  @Test
  public void testGetMavenArtifactsWhenNumFoundIsLess() throws IOException {
    ReleaseInfoFromMaven provider = new ReleaseInfoFromMaven();
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content =
        getClass().getResourceAsStream("ReleaseInfoFromMavenNumFoundLess.json")) {
      when(entity.getContent()).thenReturn(content);
      processProvider(provider);
    }
  }

  @Test
  public void testGetMavenArtifactsWhenNumFoundIsZero() throws IOException {
    ReleaseInfoFromMaven provider = new ReleaseInfoFromMaven();
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content =
        getClass().getResourceAsStream("ReleaseInfoFromMavenNumFoundZero.json")) {
      when(entity.getContent()).thenReturn(content);
      processProviderForUnknownResult(provider);
    }
  }

  @Test
  public void testGetMavenArtifactsWhenNumFoundIsNull() throws IOException {
    ReleaseInfoFromMaven provider = new ReleaseInfoFromMaven();
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content =
        getClass().getResourceAsStream("ReleaseInfoFromMavenNumFoundIsNull.json")) {
      when(entity.getContent()).thenReturn(content);
      processProviderForUnknownResult(provider);
    }
  }

  private void processProviderForUnknownResult(ReleaseInfoFromMaven provider) throws IOException {
    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(MAVEN_ARTIFACT, values);

    assertEquals(2, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertTrue(values.has(ARTIFACT_VERSION));
    assertTrue(values.of(ARTIFACT_VERSION).isPresent());
    assertTrue(values.of(ARTIFACT_VERSION).get().isUnknown());
  }

  private void processProvider(ReleaseInfoFromMaven provider) throws IOException {
    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(MAVEN_ARTIFACT, values);

    assertEquals(2, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().empty());
    assertEquals(20, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("1.10.10").isPresent());
    assertFalse(values.of(ARTIFACT_VERSION).get().isUnknown());
    ArtifactVersion foundArtifactVersion = values.of(ARTIFACT_VERSION).get().get();
    assertEquals("1.10.10", foundArtifactVersion.version());
    assertEquals(asLocalDateTime(1618200022000L), foundArtifactVersion.releaseDate());
  }

  private static LocalDateTime asLocalDateTime(long epochMilli) {
    return Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}
