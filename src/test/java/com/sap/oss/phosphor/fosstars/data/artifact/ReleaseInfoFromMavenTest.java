package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.ARTIFACT_VERSION;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.RELEASED_ARTIFACT_VERSIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import java.time.LocalDateTime;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Test;

public class ReleaseInfoFromMavenTest {

  private static final MavenArtifact MAVEN_ARTIFACT =
      new MavenArtifact("group", "artifact", "2.7.0", new GitHubProject("org", "project"));

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

    try (InputStream content = getClass().getResourceAsStream("ReleaseInfoFromMaven.html")) {
      when(entity.getContent()).thenReturn(content);
      processProvider(provider);
    }
  }

  @Test
  public void testIfMavenArtifactNotFoundInList() throws IOException {
    ReleaseInfoFromMaven provider = new ReleaseInfoFromMaven();
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content =
             getClass().getResourceAsStream("ReleaseInfoFromMavenNoArtifactInList.html")) {
      when(entity.getContent()).thenReturn(content);
      processProviderNotFoundInList(provider);
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
    assertEquals(155, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("2.7.0").isPresent());
    assertFalse(values.of(ARTIFACT_VERSION).get().isUnknown());
    ArtifactVersion foundArtifactVersion = values.of(ARTIFACT_VERSION).get().get();
    assertEquals("2.7.0", foundArtifactVersion.version());
    assertEquals(LocalDateTime.parse("2016-01-10T06:10"), foundArtifactVersion.releaseDate());
  }

  private void processProviderNotFoundInList(ReleaseInfoFromMaven provider) throws IOException {
    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(MAVEN_ARTIFACT, values);

    assertEquals(2, values.size());
    assertTrue(values.has(RELEASED_ARTIFACT_VERSIONS));
    assertTrue(values.of(RELEASED_ARTIFACT_VERSIONS).isPresent());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().isUnknown());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().empty());
    assertEquals(154, values.of(RELEASED_ARTIFACT_VERSIONS).get().get().size());
    assertFalse(values.of(RELEASED_ARTIFACT_VERSIONS).get().get().get("2.7.0").isPresent());
    assertTrue(values.of(ARTIFACT_VERSION).get().isUnknown());
  }
}
