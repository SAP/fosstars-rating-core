package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.junit.Test;

public class OwaspEsapiTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeature() {
    assertSame(USES_OWASP_ESAPI, new OwaspEsapi(fetcher).supportedFeature());
  }

  @Test
  public void testWithoutOwaspEsapi() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithoutOwaspEsapiDependency.xml")) {

      OwaspEsapi provider = createProvider(is, "pom.xml");
      checkOwaspEsapi(provider, false);
    }
  }

  @Test
  public void testWithOwaspEsapiInDefaultDependencies() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspEsapiInDefaultDependencies.xml")) {

      OwaspEsapi provider = createProvider(is, "pom.xml");
      checkOwaspEsapi(provider, true);
    }
  }

  @Test
  public void testWithOwaspEsapiInProfileDependencies() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspEsapiInProfiledDependencies.xml")) {

      OwaspEsapi provider = createProvider(is, "pom.xml");
      checkOwaspEsapi(provider, true);
    }
  }

  private static void checkOwaspEsapi(OwaspEsapi provider, boolean expectedValue)
      throws IOException {

    GitHubProject project = new GitHubProject("org", "test");
    ValueSet values = new ValueHashSet();
    provider.update(project, values);

    assertEquals(1, values.size());
    Optional<Value> value = values.of(USES_OWASP_ESAPI);
    assertTrue(value.isPresent());
    Value useaOwaspEsapi = value.get();
    assertFalse(useaOwaspEsapi.isUnknown());
    assertEquals(expectedValue, useaOwaspEsapi.get());
  }

  private OwaspEsapi createProvider(InputStream is, String filename) throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.read(filename)).thenReturn(Optional.of(is));

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    OwaspEsapi provider = new OwaspEsapi(fetcher);
    provider.set(new GitHubProjectValueCache());

    return provider;
  }
}