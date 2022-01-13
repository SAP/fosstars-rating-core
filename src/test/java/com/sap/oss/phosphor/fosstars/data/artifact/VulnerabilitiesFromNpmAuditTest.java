package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_ARTIFACT;
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
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerability.Resolution;
import com.sap.oss.phosphor.fosstars.nvd.NVD;
import com.sap.oss.phosphor.fosstars.nvd.TestNVD;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

public class VulnerabilitiesFromNpmAuditTest {

  private static final NpmArtifact NPM_ARTIFACT =
      new NpmArtifact("tar", "4.4.14", new GitHubProject("org", "project"));

  private NVD testNvd() throws IOException {
    TestNVD nvd = new TestNVD();
    try (InputStream content = getClass().getResourceAsStream("NpmVulnerabilitiesFromNvd.json")) {
      nvd.add("file.json", content);
      return nvd;
    }
  }

  @Test
  public void testVulnerabilitiesAvailable() throws IOException {
    VulnerabilitiesFromNpmAudit provider =
        new VulnerabilitiesFromNpmAudit(testNvd());
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content = getClass().getResourceAsStream(
        "VulnerabilitiesFromNpmAudit.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.update(NPM_ARTIFACT, values);

      assertEquals(1, values.size());
      assertTrue(values.has(VULNERABILITIES_IN_ARTIFACT));
      assertTrue(values.of(VULNERABILITIES_IN_ARTIFACT).isPresent());
      assertFalse(values.of(VULNERABILITIES_IN_ARTIFACT).get().isUnknown());

      Vulnerabilities vulnerabilities = values.of(VULNERABILITIES_IN_ARTIFACT).get().get();
      assertEquals(4, vulnerabilities.size());

      Vulnerability vulnerability = vulnerabilities.entries().iterator().next();
      assertEquals("CVE-2021-37713", vulnerability.id());
      assertEquals(Resolution.PATCHED, vulnerability.resolution());
    }
  }

  @Test
  public void testVulnerabilitiesNotAllCvesAvailable() throws IOException {
    VulnerabilitiesFromNpmAudit provider =
        new VulnerabilitiesFromNpmAudit(testNvd());
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content = getClass().getResourceAsStream(
        "NotAllCvesFromNpmAudit.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.update(NPM_ARTIFACT, values);

      assertEquals(1, values.size());
      assertTrue(values.has(VULNERABILITIES_IN_ARTIFACT));
      assertTrue(values.of(VULNERABILITIES_IN_ARTIFACT).isPresent());
      assertFalse(values.of(VULNERABILITIES_IN_ARTIFACT).get().isUnknown());

      Vulnerabilities vulnerabilities = values.of(VULNERABILITIES_IN_ARTIFACT).get().get();
      assertEquals(2, vulnerabilities.size());

      Vulnerability vulnerability = vulnerabilities.entries().iterator().next();
      assertEquals("CVE-2021-32803", vulnerability.id());
      assertEquals(Resolution.PATCHED, vulnerability.resolution());
    }
  }

  @Test
  public void testVulnerabilitiesNoCvesAvailable() throws IOException {
    VulnerabilitiesFromNpmAudit provider =
        new VulnerabilitiesFromNpmAudit(testNvd());
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content = getClass().getResourceAsStream(
        "NoCvesFromNpmAudit.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      provider.update(NPM_ARTIFACT, values);

      assertEquals(1, values.size());
      assertTrue(values.has(VULNERABILITIES_IN_ARTIFACT));
      assertTrue(values.of(VULNERABILITIES_IN_ARTIFACT).isPresent());
      assertFalse(values.of(VULNERABILITIES_IN_ARTIFACT).get().isUnknown());

      Vulnerabilities vulnerabilities = values.of(VULNERABILITIES_IN_ARTIFACT).get().get();
      assertEquals(0, vulnerabilities.size());
    }
  }

  @Test
  public void testNoPatchForAdvisory() throws IOException {
    VulnerabilitiesFromNpmAudit provider =
        new VulnerabilitiesFromNpmAudit(testNvd());
    provider = spy(provider);

    CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
    when(provider.httpClient()).thenReturn(httpClient);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(httpClient.execute(any())).thenReturn(response);

    HttpEntity entity = mock(HttpEntity.class);
    when(response.getEntity()).thenReturn(entity);

    try (InputStream content = getClass().getResourceAsStream(
        "NoPatchForAdvisory.json")) {
      when(entity.getContent()).thenReturn(content);

      ValueHashSet values = new ValueHashSet();
      assertEquals(0, values.size());

      NpmArtifact npmArtifact
          = new NpmArtifact("bootstrap-table", "1.19.0", new GitHubProject("org", "project"));
      provider.update(npmArtifact, values);

      assertEquals(1, values.size());
      assertTrue(values.has(VULNERABILITIES_IN_ARTIFACT));
      assertTrue(values.of(VULNERABILITIES_IN_ARTIFACT).isPresent());
      assertFalse(values.of(VULNERABILITIES_IN_ARTIFACT).get().isUnknown());

      Vulnerabilities vulnerabilities = values.of(VULNERABILITIES_IN_ARTIFACT).get().get();
      assertEquals(1, vulnerabilities.size());

      Vulnerability vulnerability = vulnerabilities.entries().iterator().next();
      assertEquals("CVE-2021-37713", vulnerability.id());
      assertEquals(Resolution.UNPATCHED, vulnerability.resolution());
    }
  }
}