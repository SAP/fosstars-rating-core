package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.sgs.phosphor.fosstars.data.github.UsesOwaspDependencyCheck.USES_OWASP_DEPENDENCY_CHECK;
import static org.junit.Assert.assertEquals;
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

public class UsesOwaspDependencyCheckTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testMavenWithOwaspDependencyCheckInBuild() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInBuild.xml")) {

      checkValue(createProvider(is, "pom.xml"), true);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInBuildPluginManagement() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInBuildPluginManagement.xml")) {

      checkValue(createProvider(is, "pom.xml"), true);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInProfilesBuild() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInProfilesBuild.xml")) {

      checkValue(createProvider(is, "pom.xml"), true);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInProfilesReporting() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInProfilesReporting.xml")) {

      checkValue(createProvider(is, "pom.xml"), true);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInReporting() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInProfilesReporting.xml")) {

      checkValue(createProvider(is, "pom.xml"), true);
    }
  }

  @Test
  public void testMavenWithoutOwaspDependencyCheck() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithoutOwaspDependencyCheck.xml")) {

      checkValue(createProvider(is, "pom.xml"), false);
    }
  }

  @Test
  public void testGradleWithOwaspDependencyCheck() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithOwaspDependencyCheck.gradle")) {

      checkValue(createProvider(is, "build.gradle"), true);
    }
  }

  @Test
  public void testGradleWithoutOwaspDependencyCheck() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithoutOwaspDependencyCheck.gradle")) {

      checkValue(createProvider(is, "build.gradle"), false);
    }
  }

  private UsesOwaspDependencyCheck createProvider(InputStream is, String filename)
      throws IOException {

    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.read(filename)).thenReturn(Optional.of(is));

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    UsesOwaspDependencyCheck provider = new UsesOwaspDependencyCheck(fetcher);
    provider.set(new GitHubProjectValueCache());

    return provider;
  }

  private static void checkValue(UsesOwaspDependencyCheck provider, boolean expectedValue)
      throws IOException {

    GitHubProject project = new GitHubProject("org", "test");

    ValueSet values = new ValueHashSet();
    provider.update(project, values);

    assertEquals(1, values.size());
    assertTrue(values.has(USES_OWASP_DEPENDENCY_CHECK));
    assertTrue(values.of(USES_OWASP_DEPENDENCY_CHECK).isPresent());

    Value<Boolean> value = values.of(USES_OWASP_DEPENDENCY_CHECK).get();
    assertEquals(expectedValue, value.get());
  }

}