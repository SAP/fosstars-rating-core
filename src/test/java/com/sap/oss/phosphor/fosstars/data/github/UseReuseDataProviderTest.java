package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.TestUtils.PROJECT;
import static com.sap.oss.phosphor.fosstars.data.github.UseReuseDataProvider.REUSE_CONFIG;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_REUSE_LICENSES;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.IS_REUSE_COMPLIANT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.README_HAS_REUSE_INFO;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.REGISTERED_IN_REUSE;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_REUSE;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class UseReuseDataProviderTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeature() throws IOException {
    UseReuseDataProvider provider = new UseReuseDataProvider(fetcher);
    assertTrue(provider.supportedFeatures().contains(USES_REUSE));
    assertTrue(provider.supportedFeatures().contains(README_HAS_REUSE_INFO));
    assertTrue(provider.supportedFeatures().contains(HAS_REUSE_LICENSES));
    assertTrue(provider.supportedFeatures().contains(REGISTERED_IN_REUSE));
    assertTrue(provider.supportedFeatures().contains(IS_REUSE_COMPLIANT));
  }

  @Test
  public void testUseReuse() throws IOException {
    LocalRepository localRepository = mock(LocalRepository.class);
    TestGitHubDataFetcher.addForTesting(PROJECT, localRepository);

    when(localRepository.hasFile(REUSE_CONFIG)).thenReturn(false);
    Value<Boolean> value = UseReuseDataProvider.useReuse(PROJECT);
    assertFalse(value.isUnknown());
    assertFalse(value.get());
    assertFalse(value.explanation().isEmpty());

    when(localRepository.hasFile(REUSE_CONFIG)).thenReturn(true);
    value = UseReuseDataProvider.useReuse(PROJECT);
    assertEquals(USES_REUSE, value.feature());
    assertFalse(value.isUnknown());
    assertTrue(value.get());
    assertTrue(value.explanation().isEmpty());
  }

  @Test
  public void testReadmeHasReuseInfo() throws IOException {
    LocalRepository localRepository = mock(LocalRepository.class);
    TestGitHubDataFetcher.addForTesting(PROJECT, localRepository);

    when(localRepository.hasFile(anyString())).thenReturn(false);
    Value<Boolean> value = UseReuseDataProvider.readmeHasReuseInfo(PROJECT);
    assertEquals(README_HAS_REUSE_INFO, value.feature());
    assertFalse(value.isUnknown());
    assertFalse(value.get());
    assertFalse(value.explanation().isEmpty());

    when(localRepository.hasFile("README.md")).thenReturn(true);
    when(localRepository.file("README.md")).thenReturn(Optional.of("This is README."));
    value = UseReuseDataProvider.readmeHasReuseInfo(PROJECT);
    assertEquals(README_HAS_REUSE_INFO, value.feature());
    assertFalse(value.isUnknown());
    assertFalse(value.get());
    assertFalse(value.explanation().isEmpty());

    when(localRepository.hasFile("README.md")).thenReturn(true);
    when(localRepository.file("README.md"))
        .thenReturn(Optional.of(format(
            "Yes, README has a link to REUSE: https://api.reuse.software/info/github.com/%s/%s",
            PROJECT.organization().name(), PROJECT.name())));
    value = UseReuseDataProvider.readmeHasReuseInfo(PROJECT);
    assertEquals(README_HAS_REUSE_INFO, value.feature());
    assertFalse(value.isUnknown());
    assertTrue(value.get());
    assertTrue(value.explanation().isEmpty());
    
    when(localRepository.hasFile("README.md")).thenReturn(true);
    when(localRepository.file("README.md"))
        .thenReturn(Optional.of(format(
            "Yes, README has a link to REUSE: https://api.reuse.software/info/github.com/%s/%s",
            PROJECT.organization().name().toUpperCase(), PROJECT.name().toUpperCase())));
    value = UseReuseDataProvider.readmeHasReuseInfo(PROJECT);
    assertEquals(README_HAS_REUSE_INFO, value.feature());
    assertFalse(value.isUnknown());
    assertTrue(value.get());
    assertTrue(value.explanation().isEmpty());
  }

  @Test
  public void testHasReuseLicenses() throws IOException {
    LocalRepository localRepository = mock(LocalRepository.class);
    TestGitHubDataFetcher.addForTesting(PROJECT, localRepository);

    Path licensesDirectory = Paths.get("LICENSES");

    when(localRepository.hasDirectory(licensesDirectory)).thenReturn(false);
    Value<Boolean> value = UseReuseDataProvider.hasReuseLicenses(PROJECT);
    assertEquals(HAS_REUSE_LICENSES, value.feature());
    assertFalse(value.isUnknown());
    assertFalse(value.get());
    assertFalse(value.explanation().isEmpty());

    when(localRepository.hasDirectory(licensesDirectory)).thenReturn(true);
    when(localRepository.files(any(), any())).thenReturn(Collections.emptyList());
    value = UseReuseDataProvider.hasReuseLicenses(PROJECT);
    assertEquals(HAS_REUSE_LICENSES, value.feature());
    assertFalse(value.isUnknown());
    assertFalse(value.get());
    assertFalse(value.explanation().isEmpty());

    when(localRepository.hasDirectory(licensesDirectory)).thenReturn(true);
    when(localRepository.files(any(), any()))
        .thenReturn(Collections.singletonList(licensesDirectory.resolve(Paths.get("LICENSE"))));
    value = UseReuseDataProvider.hasReuseLicenses(PROJECT);
    assertEquals(HAS_REUSE_LICENSES, value.feature());
    assertFalse(value.isUnknown());
    assertTrue(value.get());
    assertTrue(value.explanation().isEmpty());
  }

  @Test
  public void testReuseInfoWithError() throws IOException {
    testReuseInfo(404, null,
        ValueHashSet.from(REGISTERED_IN_REUSE.unknown(), IS_REUSE_COMPLIANT.unknown()));
  }

  @Test
  public void testReuseInfoWithUnregisteredProject() throws IOException {
    testReuseInfo(200, "unregistered",
        ValueHashSet.from(REGISTERED_IN_REUSE.value(false), IS_REUSE_COMPLIANT.value(false)));
  }

  @Test
  public void testReuseInfoWithNonCompliantProject() throws IOException {
    testReuseInfo(200, "non-compliant",
        ValueHashSet.from(REGISTERED_IN_REUSE.value(true), IS_REUSE_COMPLIANT.value(false)));
  }

  @Test
  public void testReuseInfoWithCompliantProject() throws IOException {
    testReuseInfo(200, "compliant",
        ValueHashSet.from(REGISTERED_IN_REUSE.value(true), IS_REUSE_COMPLIANT.value(true)));
  }

  public void testReuseInfo(int responseCode, String status, ValueSet expectedValues)
      throws IOException {

    StatusLine statusLine = mock(StatusLine.class);
    when(statusLine.getStatusCode()).thenReturn(responseCode);

    CloseableHttpResponse response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine()).thenReturn(statusLine);

    CloseableHttpClient client = mock(CloseableHttpClient.class);
    if (status == "unregistered") {
      when(client.execute(any(HttpGet.class))).thenReturn(response).thenReturn(response)
          .thenThrow(new AssertionError("Maximum two GET requests were expected!"));
    } else {
      when(client.execute(any(HttpGet.class))).thenReturn(response)
          .thenThrow(new AssertionError("Only one GET request was expected!"));
    }

    HttpEntity entity = mock(HttpEntity.class);
    when(entity.getContent())
        .thenReturn(IOUtils.toInputStream(format("{ \"status\" : \"%s\"}", status), UTF_8));

    statusLine = mock(StatusLine.class);
    when(statusLine.getStatusCode()).thenReturn(200);

    response = mock(CloseableHttpResponse.class);
    when(response.getStatusLine()).thenReturn(statusLine);
    when(response.getEntity()).thenReturn(entity);

    if (status == "unregistered") {
      when(client.execute(any(HttpGet.class))).thenReturn(response).thenReturn(response)
          .thenThrow(new AssertionError("Maximum two GET requests were expected!"));
    } else {
      when(client.execute(any(HttpGet.class))).thenReturn(response)
          .thenThrow(new AssertionError("Only one GET request was expected!"));
    }

    UseReuseDataProvider provider = spy(new UseReuseDataProvider(fetcher));
    when(provider.httpClient()).thenReturn(client);

    ValueSet values = provider.fetchValuesFor(PROJECT);
    for (Value<?> expectedValue : expectedValues) {
      Value<?> value = values.of(expectedValue.feature())
          .orElseThrow(() -> new Error(
              format("Could not find an expected feature: %s", expectedValue.feature().name())));
      value.processIfKnown(v -> assertEquals(expectedValue.get(), value.get()));
      if (value.isUnknown() || value.get().equals(Boolean.FALSE)) {
        assertFalse(value.explanation().isEmpty());
      }
    }
  }

  static class HttpGetMatcher implements ArgumentMatcher<HttpGet> {

    private final String expectedUrl;

    public HttpGetMatcher(String expectedUrl) {
      this.expectedUrl = expectedUrl;
    }

    @Override
    public boolean matches(HttpGet actualHttpGet) {
      if (actualHttpGet == null) {
        return false;
      }
      return actualHttpGet.getURI().toString().equals(expectedUrl);
    }

  }

  @Test
  public void testReuseCompliantWithTrailingSlash() throws IOException {

    StatusLine statusLine = mock(StatusLine.class);
    when(statusLine.getStatusCode()).thenReturn(200);

    HttpEntity unregisteredEntity = mock(HttpEntity.class);
    when(unregisteredEntity.getContent())
        .thenReturn(IOUtils.toInputStream("{ \"status\" : \"unregistered\"}", UTF_8));

    CloseableHttpResponse unregisteredResponse = mock(CloseableHttpResponse.class);
    when(unregisteredResponse.getStatusLine()).thenReturn(statusLine);
    when(unregisteredResponse.getEntity()).thenReturn(unregisteredEntity);

    CloseableHttpClient client = mock(CloseableHttpClient.class);

    when(client.execute(argThat(new HttpGetMatcher(
        "https://api.reuse.software/status/github.com/org/test"))))
            .thenReturn(unregisteredResponse);

    HttpEntity compliantEntity = mock(HttpEntity.class);
    when(compliantEntity.getContent())
        .thenReturn(IOUtils.toInputStream("{ \"status\" : \"compliant\"}", UTF_8));

    CloseableHttpResponse compliantResponse = mock(CloseableHttpResponse.class);
    when(compliantResponse.getStatusLine()).thenReturn(statusLine);
    when(compliantResponse.getEntity()).thenReturn(compliantEntity);

    when(client.execute(argThat(new HttpGetMatcher(
        "https://api.reuse.software/status/github.com/org/test/"))))
            .thenReturn(compliantResponse);

    UseReuseDataProvider useReuseDataProvider = spy(new UseReuseDataProvider(fetcher));
    when(useReuseDataProvider.httpClient()).thenReturn(client);

    ValueSet retrievedValues = useReuseDataProvider.fetchValuesFor(PROJECT);
    Value<Boolean> isRegisteredValue = retrievedValues.of(REGISTERED_IN_REUSE)
        .orElseThrow(() -> new Error(
            format("Could not find an expected feature: %s", REGISTERED_IN_REUSE.name())));
    assertTrue(isRegisteredValue.get());
    Value<Boolean> isCompliantValue = retrievedValues.of(IS_REUSE_COMPLIANT)
        .orElseThrow(() -> new Error(
            format("Could not find an expected feature: %s", IS_REUSE_COMPLIANT.name())));
    assertTrue(isCompliantValue.get());

  }

  @Test
  public void testReuseRepositoryExceptions() throws IOException {

    UseReuseDataProvider provider = new UseReuseDataProvider(fetcher);
    provider.configure(IOUtils.toInputStream(
        "---\n"
            + "repositoryExceptions: https://github.com/org/test\n",
        "UTF-8"));
    ValueSet retrievedValues = provider.fetchValuesFor(PROJECT);

    Value<Boolean> usesReuseValue = retrievedValues.of(USES_REUSE)
        .orElseThrow(() -> new Error(
            format("Could not find an expected feature: %s", USES_REUSE.name())));
    assertTrue(usesReuseValue.get());
    Value<Boolean> readmeHasReuseValue = retrievedValues.of(README_HAS_REUSE_INFO)
        .orElseThrow(() -> new Error(
            format("Could not find an expected feature: %s", README_HAS_REUSE_INFO.name())));
    assertTrue(readmeHasReuseValue.get());
    Value<Boolean> hasReuseLicensesValue = retrievedValues.of(HAS_REUSE_LICENSES)
        .orElseThrow(() -> new Error(
            format("Could not find an expected feature: %s", HAS_REUSE_LICENSES.name())));
    assertTrue(hasReuseLicensesValue.get());
    Value<Boolean> isRegisteredValue = retrievedValues.of(REGISTERED_IN_REUSE)
        .orElseThrow(() -> new Error(
            format("Could not find an expected feature: %s", REGISTERED_IN_REUSE.name())));
    assertTrue(isRegisteredValue.get());
    Value<Boolean> isCompliantValue = retrievedValues.of(IS_REUSE_COMPLIANT)
        .orElseThrow(() -> new Error(
            format("Could not find an expected feature: %s", IS_REUSE_COMPLIANT.name())));
    assertTrue(isCompliantValue.get());

  }

}