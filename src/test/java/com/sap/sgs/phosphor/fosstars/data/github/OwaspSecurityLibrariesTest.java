package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_ENCODER;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.junit.Test;

public class OwaspSecurityLibrariesTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeatures() {
    OwaspSecurityLibraries provider = new OwaspSecurityLibraries(fetcher);
    assertEquals(3, provider.supportedFeatures().size());
    assertTrue(provider.supportedFeatures().contains(USES_OWASP_ESAPI));
    assertTrue(provider.supportedFeatures().contains(USES_OWASP_JAVA_ENCODER));
    assertTrue(provider.supportedFeatures().contains(USES_OWASP_JAVA_HTML_SANITIZER));
  }

  @Test
  public void testWithoutOwaspEsapi() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithoutOwaspEsapiDependency.xml")) {

      OwaspSecurityLibraries provider = createProvider(is, "pom.xml");
      checkValue(provider, USES_OWASP_ESAPI, false);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, false);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, false);
    }
  }

  @Test
  public void testWithOwaspEsapiInDefaultDependencies() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspEsapiInDefaultDependencies.xml")) {

      OwaspSecurityLibraries provider = createProvider(is, "pom.xml");
      checkValue(provider, USES_OWASP_ESAPI, true);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, false);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, false);
    }
  }

  @Test
  public void testWithOwaspJavaEncoderInDefaultDependencies() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspJavaEncoderInDefaultDependencies.xml")) {

      OwaspSecurityLibraries provider = createProvider(is, "pom.xml");
      checkValue(provider, USES_OWASP_ESAPI, false);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, true);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, false);
    }
  }

  @Test
  public void testWithOwaspJavaHtmlSanitizerInDefaultDependencies() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspJavaHtmlSanitizerInDefaultDependencies.xml")) {

      OwaspSecurityLibraries provider = createProvider(is, "pom.xml");
      checkValue(provider, USES_OWASP_ESAPI, false);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, false);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, true);
    }
  }

  @Test
  public void testWithOwaspEsapiInProfileDependencies() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspEsapiInProfiledDependencies.xml")) {

      OwaspSecurityLibraries provider = createProvider(is, "pom.xml");
      checkValue(provider, USES_OWASP_ESAPI, true);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, false);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, false);
    }
  }

  private static void checkValue(
      OwaspSecurityLibraries provider, Feature<Boolean> feature, boolean expectedValue)
      throws IOException {

    GitHubProject project = new GitHubProject("org", "test");
    ValueSet values = new ValueHashSet();
    provider.update(project, values);

    Optional<Value> value = values.of(feature);
    assertTrue(value.isPresent());
    Value useaOwaspEsapi = value.get();
    assertFalse(useaOwaspEsapi.isUnknown());
    assertEquals(expectedValue, useaOwaspEsapi.get());
  }

  private OwaspSecurityLibraries createProvider(
      InputStream is, String filename) throws IOException {

    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.read(filename)).thenReturn(Optional.of(is));

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    OwaspSecurityLibraries provider = new OwaspSecurityLibraries(fetcher);
    provider.set(new GitHubProjectValueCache());

    return provider;
  }
}