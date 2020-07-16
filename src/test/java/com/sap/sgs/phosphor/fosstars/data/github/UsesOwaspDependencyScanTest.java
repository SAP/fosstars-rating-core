package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.NOT_USED;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.OPTIONAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import org.junit.Test;

public class UsesOwaspDependencyScanTest extends TestGitHubDataFetcherHolder {

  @Test
  public void testMavenWithOwaspDependencyCheckInBuild() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInBuild.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsageStatus(MANDATORY, values, OWASP_DEPENDENCY_CHECK_USAGE);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInBuildPluginManagement() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInBuildPluginManagement.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsageStatus(MANDATORY, values, OWASP_DEPENDENCY_CHECK_USAGE);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInProfilesBuild() throws IOException {
    try (InputStream is =
        getClass().getResourceAsStream("MavenWithOwaspDependencyCheckInProfilesBuild.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsageStatus(OPTIONAL, values, OWASP_DEPENDENCY_CHECK_USAGE);
      checkDoubleValue(7.0, values, OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInProfilesReporting() throws IOException {
    try (InputStream is =
        getClass().getResourceAsStream("MavenWithOwaspDependencyCheckInProfilesReporting.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsageStatus(OPTIONAL, values, OWASP_DEPENDENCY_CHECK_USAGE);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInReporting() throws IOException {
    try (InputStream is =
        getClass().getResourceAsStream("MavenWithOwaspDependencyCheckInReporting.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsageStatus(MANDATORY, values, OWASP_DEPENDENCY_CHECK_USAGE);
    }
  }

  @Test
  public void testMavenWithoutOwaspDependencyCheck() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithoutOwaspDependencyCheck.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsageStatus(NOT_USED, values, OWASP_DEPENDENCY_CHECK_USAGE);
    }
  }
  
  @Test
  public void testMavenWitOwaspDependencyCheckBuildAndProfile() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInBuildAndProfile.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsageStatus(MANDATORY, values, OWASP_DEPENDENCY_CHECK_USAGE);
      checkDoubleValue(6.0, values, OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD);
    }
  }

  private UsesOwaspDependencyScan createProvider(InputStream is, String filename)
      throws IOException {

    final LocalRepository repository = mock(LocalRepository.class);
    when(repository.read(filename)).thenReturn(Optional.of(is));

    GitHubProject project = new GitHubProject("org", "test");
    addForTesting(project, repository);

    UsesOwaspDependencyScan provider = new UsesOwaspDependencyScan(fetcher);
    provider.set(new GitHubProjectValueCache());

    return provider;
  }

  private static ValueSet values(UsesOwaspDependencyScan provider)
      throws IOException {

    GitHubProject project = new GitHubProject("org", "test");

    ValueSet values = new ValueHashSet();
    provider.update(project, values);

    Set<Feature> features = provider.supportedFeatures();
    assertEquals(features.size(), values.size());
    assertTrue(values.containsAll(features));
    return values;
  }

  private static void checkUsageStatus(OwaspDependencyCheckUsage expected, ValueSet values,
      Feature feature) {
    assertTrue(values.of(feature).isPresent());
    Value<Boolean> value = values.of(feature).get();
    assertEquals(expected, value.get());
  }

  private static void checkDoubleValue(Double expected, ValueSet values, Feature feature) {
    assertTrue(values.of(feature).isPresent());
    Value<Double> value = values.of(feature).get();
    assertEquals(expected, value.get());
  }
}