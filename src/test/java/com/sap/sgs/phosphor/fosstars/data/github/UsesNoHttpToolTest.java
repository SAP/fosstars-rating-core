package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.USES_NOHTTP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.TestGitHubDataFetcherHolder;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;

public class UsesNoHttpToolTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testMavenWithNoHTTP() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("MavenCheckStyleWithNoHTTP.xml")) {
      checkValue(createProvider(is, "pom.xml"), true);
    }
  }

  @Test
  public void testMavenWithoutNoHTTP() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("MavenCheckStyleWithoutNoHTTP.xml")) {
      checkValue(createProvider(is, "pom.xml"), false);
    }
  }

  @Test
  public void testGradleWithNoHTTP() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("GradleCheckStyleWithNoHTTP.gradle")) {
      checkValue(createProvider(is, "build.gradle"), true);
    }
  }

  @Test
  public void testGradleWithoutNoHTTP() throws IOException {
    try (InputStream is = getClass().getResourceAsStream("GradleCheckStyleWithoutNoHTTP.gradle")) {
      checkValue(createProvider(is, "build.gradle"), false);
    }
  }

  private UsesNoHttpTool createProvider(InputStream is, String filename) throws IOException {
    GHContent content = mock(GHContent.class);
    when(content.isFile()).thenReturn(true);
    when(content.read()).thenReturn(is);

    GHRepository repository = mock(GHRepository.class);
    when(repository.getFileContent(filename)).thenReturn(content);

    when(fetcher.github().getRepository(any())).thenReturn(repository);

    UsesNoHttpTool provider = new UsesNoHttpTool(fetcher);
    provider.set(new GitHubProjectValueCache());

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