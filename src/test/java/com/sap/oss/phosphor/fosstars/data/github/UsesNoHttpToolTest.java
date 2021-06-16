package com.sap.oss.phosphor.fosstars.data.github;

import static com.sap.oss.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
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

public class UsesNoHttpToolTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testMavenWithNoHttp() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("MavenCheckStyleWithNoHttp.xml")) {
      checkValue(createProvider(is, "pom.xml"), true);
    }
  }

  @Test
  public void testMavenWithNoHttpInProfilesBuild() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenCheckStyleWithNoHttpInProfilesBuild.xml")) {

      checkValue(createProvider(is, "pom.xml"), true);
    }
  }

  @Test
  public void testMavenWithoutNoHTTP() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("MavenCheckStyleWithoutNoHttp.xml")) {
      checkValue(createProvider(is, "pom.xml"), false);
    }
  }

  @Test
  public void testGradleWithNoHTTP() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("GradleCheckStyleWithNoHttp.gradle")) {
      checkValue(createProvider(is, "build.gradle"), true);
    }
  }

  @Test
  public void testGradleWithoutNoHTTP() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("GradleCheckStyleWithoutNoHttp.gradle")) {
      checkValue(createProvider(is, "build.gradle"), false);
    }
  }

  private UsesNoHttpTool createProvider(InputStream is, String filename) throws IOException {
    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.read(filename)).thenReturn(Optional.of(is));

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    UsesNoHttpTool provider = new UsesNoHttpTool(fetcher);
    provider.set(new SubjectValueCache());

    return provider;
  }

  private static void checkValue(UsesNoHttpTool provider, boolean expectedValue)
      throws IOException {

    GitHubProject project = new GitHubProject("org", "test");

    ValueSet values = new ValueHashSet();
    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_NOHTTP));
    assertTrue(values.of(USES_NOHTTP).isPresent());

    Value<Boolean> value = values.of(USES_NOHTTP).get();
    assertEquals(expectedValue, value.get());
  }
}