package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_ESAPI;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_ENCODER;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_OWASP_JAVA_HTML_SANITIZER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
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
  public void testMavenWithoutOwaspEsapi() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithoutOwaspEsapiDependency.xml")) {

      OwaspSecurityLibraries provider = createProvider(is, "pom.xml");
      checkValue(provider, USES_OWASP_ESAPI, false);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, false);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, false);
    }
  }

  @Test
  public void testMavenWithOwaspEsapiInDefaultDependencies() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspEsapiInDefaultDependencies.xml")) {

      OwaspSecurityLibraries provider = createProvider(is, "pom.xml");
      checkValue(provider, USES_OWASP_ESAPI, true);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, false);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, false);
    }
  }

  @Test
  public void testMavenWithOwaspJavaEncoderInDefaultDependencies() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspJavaEncoderInDefaultDependencies.xml")) {

      OwaspSecurityLibraries provider = createProvider(is, "pom.xml");
      checkValue(provider, USES_OWASP_ESAPI, false);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, true);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, false);
    }
  }

  @Test
  public void testMavenWithOwaspJavaHtmlSanitizerInDefaultDependencies() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspJavaHtmlSanitizerInDefaultDependencies.xml")) {

      OwaspSecurityLibraries provider = createProvider(is, "pom.xml");
      checkValue(provider, USES_OWASP_ESAPI, false);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, false);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, true);
    }
  }

  @Test
  public void testMavenWithOwaspEsapiInProfileDependencies() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspEsapiInProfiledDependencies.xml")) {

      OwaspSecurityLibraries provider = createProvider(is, "pom.xml");
      checkValue(provider, USES_OWASP_ESAPI, true);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, false);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, false);
    }
  }

  @Test
  public void testGradleWithOwaspSecurityTools() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithOwaspSecurityTools.gradle")) {

      OwaspSecurityLibraries provider = createProvider(is, "build.gradle");
      checkValue(provider, USES_OWASP_ESAPI, true);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, true);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, true);
    }
  }

  @Test
  public void testGradleWithOwaspSecurityToolsInSubproject() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithOwaspSecurityTools.gradle")) {

      OwaspSecurityLibraries provider = createProvider(is, "subproject/build.gradle");
      checkValue(provider, USES_OWASP_ESAPI, true);
      checkValue(provider, USES_OWASP_JAVA_ENCODER, true);
      checkValue(provider, USES_OWASP_JAVA_HTML_SANITIZER, true);
    }
  }

  @Test
  public void testGradleWithoutOwaspSecurityTools() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithoutOwaspSecurityTools.gradle")) {

      OwaspSecurityLibraries provider = createProvider(is, "build.gradle");
      checkValue(provider, USES_OWASP_ESAPI, false);
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

    Optional<Value<Boolean>> something = values.of(feature);
    assertTrue(something.isPresent());
    Value<Boolean> value = something.get();
    assertFalse(value.isUnknown());
    assertEquals(expectedValue, value.get());
  }

  private OwaspSecurityLibraries createProvider(
      InputStream is, String filename) throws IOException {

    final LocalRepository repository = mock(LocalRepository.class);

    List<String> content = IOUtils.readLines(is);

    when(repository.read(filename))
        .thenReturn(Optional.of(IOUtils.toInputStream(String.join("\n", content))));
    when(repository.readLinesOf(Paths.get(filename)))
        .thenReturn(Optional.of(content));
    when(repository.files(any()))
        .thenReturn(Collections.singletonList(Paths.get(filename)));

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    OwaspSecurityLibraries provider = new OwaspSecurityLibraries(fetcher);
    provider.set(new SubjectValueCache());

    return provider;
  }
}