package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SIGNS_ARTIFACTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.oss.phosphor.fosstars.data.SubjectValueCache;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.junit.Test;

public class SignsJarArtifactsTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testSupportedFeature() {
    SignsJarArtifacts provider = new SignsJarArtifacts(fetcher);
    assertEquals(SIGNS_ARTIFACTS, provider.supportedFeature());
  }

  @Test
  public void testWithMavenGpgPlugin() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("MavenPomWithMavenGPG.xml")) {
      checkValue(createProvider(is, "pom.xml"), true);
    }
  }

  @Test
  public void testWithoutMavenGpgPlugin() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("MavenPomWithoutMavenGPG.xml")) {
      checkValue(createProvider(is, "pom.xml"), false);
    }
  }

  private SignsJarArtifacts createProvider(InputStream is, String filename) throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.read(filename)).thenReturn(Optional.of(is));

    GitHubProject project = new GitHubProject("org", "test");
    fetcher.addForTesting(project, repository);

    SignsJarArtifacts provider = new SignsJarArtifacts(fetcher);
    provider.set(new SubjectValueCache());

    return provider;
  }

  private static void checkValue(SignsJarArtifacts provider, boolean expectedValue)
      throws IOException {

    GitHubProject project = new GitHubProject("org", "test");

    ValueSet values = new ValueHashSet();
    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(SIGNS_ARTIFACTS));

    Optional<Value<Boolean>> something = values.of(SIGNS_ARTIFACTS);
    assertTrue(something.isPresent());

    Value<Boolean> actualValue = something.get();
    assertEquals(expectedValue, actualValue.get());
  }
}