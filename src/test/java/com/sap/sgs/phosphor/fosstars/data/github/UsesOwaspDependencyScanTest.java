package com.sap.sgs.phosphor.fosstars.data.github;

import static com.sap.sgs.phosphor.fosstars.data.github.TestGitHubDataFetcherHolder.TestGitHubDataFetcher.addForTesting;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.OWASP_DEPENDENCY_CHECK_USAGE;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.MANDATORY;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.NOT_USED;
import static com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage.OPTIONAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.ValueSet;
import com.sap.sgs.phosphor.fosstars.model.value.CVSS;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckCvssThresholdValue;
import com.sap.sgs.phosphor.fosstars.model.value.OwaspDependencyCheckUsage;
import com.sap.sgs.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProject;
import com.sap.sgs.phosphor.fosstars.tool.github.GitHubProjectValueCache;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class UsesOwaspDependencyScanTest extends TestGitHubDataFetcherHolder {

  private static final Double NOT_SPECIFIED = null;

  @Test
  public void testMavenWithOwaspDependencyCheckInBuild() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInBuild.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsage(MANDATORY, values);
      checkThreshold(NOT_SPECIFIED, values);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInBuildPluginManagement() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInBuildPluginManagement.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsage(OPTIONAL, values);
      checkThreshold(NOT_SPECIFIED, values);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInProfilesBuild() throws IOException {
    try (InputStream is =
        getClass().getResourceAsStream("MavenWithOwaspDependencyCheckInProfilesBuild.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsage(OPTIONAL, values);
      checkThreshold(7.0, values);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInProfilesReporting() throws IOException {
    try (InputStream is =
        getClass().getResourceAsStream("MavenWithOwaspDependencyCheckInProfilesReporting.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsage(OPTIONAL, values);
      checkThreshold(0.0, values);
    }
  }

  @Test
  public void testMavenWithOwaspDependencyCheckInReporting() throws IOException {
    try (InputStream is =
        getClass().getResourceAsStream("MavenWithOwaspDependencyCheckInReporting.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsage(MANDATORY, values);
      checkThreshold(1.3, values);
    }
  }

  @Test
  public void testMavenWithoutOwaspDependencyCheck() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithoutOwaspDependencyCheck.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsage(NOT_USED, values);
      checkThreshold(NOT_SPECIFIED, values);
    }
  }
  
  @Test
  public void testMavenWitOwaspDependencyCheckBuildAndProfile() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("MavenWithOwaspDependencyCheckInBuildAndProfile.xml")) {

      ValueSet values = values(createProvider(is, "pom.xml"));
      checkUsage(MANDATORY, values);
      checkThreshold(6.0, values);
    }
  }

  @Test
  public void testGradleWithMandatoryOwaspDependencyCheck() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithOwaspDependencyCheck.gradle")) {

      ValueSet values = values(createProvider(is, "build.gradle"));
      checkUsage(MANDATORY, values);
      checkThreshold(NOT_SPECIFIED, values);
    }
  }

  @Test
  public void testGradleWithOptionalOwaspDependencyCheck() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithOwaspDependencyCheck.gradle")) {

      ValueSet values = values(createProvider(is, "other/build.gradle"));
      checkUsage(OPTIONAL, values);
      checkThreshold(NOT_SPECIFIED, values);
    }
  }

  @Test
  public void testGradleWithoutOwaspDependencyCheck() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithoutOwaspDependencyCheck.gradle")) {

      ValueSet values = values(createProvider(is, "build.gradle"));
      checkUsage(NOT_USED, values);
      checkThreshold(NOT_SPECIFIED, values);
    }
  }

  @Test
  public void testGradleWithMandatoryOwaspDependencyCheckWithFailBuildOnCvss() throws IOException {
    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithOwaspDependencyCheckWithFailBuildOnCvss.gradle")) {

      ValueSet values = values(createProvider(is, "build.gradle"));
      checkUsage(MANDATORY, values);
      checkThreshold(5.3, values);
    }
  }

  @Test
  public void testGradleWithMandatoryOwaspDependencyCheckWithFailBuildOnAnyIssueTrue()
      throws IOException {

    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithOwaspDependencyCheckWithFailBuildOnAnyIssueTrue.gradle")) {

      ValueSet values = values(createProvider(is, "build.gradle"));
      checkUsage(MANDATORY, values);
      checkThreshold(CVSS.MIN, values);
    }
  }

  @Test
  public void testGradleWithMandatoryOwaspDependencyCheckWithBuildOnAnyIssueFalse()
      throws IOException {

    try (InputStream is = getClass()
        .getResourceAsStream("GradleWithOwaspDependencyCheckWithFailBuildOnAnyIssueFalse.gradle")) {

      ValueSet values = values(createProvider(is, "build.gradle"));
      checkUsage(MANDATORY, values);
      checkThreshold(NOT_SPECIFIED, values);
    }
  }

  private UsesOwaspDependencyScan createProvider(InputStream is, String filename)
      throws IOException {

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

    UsesOwaspDependencyScan provider = new UsesOwaspDependencyScan(fetcher);
    provider.set(new GitHubProjectValueCache());

    return provider;
  }

  private static ValueSet values(UsesOwaspDependencyScan provider) throws IOException {
    GitHubProject project = new GitHubProject("org", "test");

    ValueSet values = new ValueHashSet();
    provider.update(project, values);

    Set<Feature> features = provider.supportedFeatures();
    assertEquals(features.size(), values.size());
    assertTrue(values.containsAll(features));
    return values;
  }

  private static void checkUsage(OwaspDependencyCheckUsage expected, ValueSet values) {
    Optional<Value> value = values.of(OWASP_DEPENDENCY_CHECK_USAGE);
    assertTrue(value.isPresent());
    Value usage = value.get();
    assertEquals(expected, usage.get());
  }

  private static void checkThreshold(Double expected, ValueSet values) {
    Optional<Value> value = values.of(OWASP_DEPENDENCY_CHECK_FAIL_CVSS_THRESHOLD);
    assertTrue(value.isPresent());
    Value n = value.get();
    assertTrue(n instanceof OwaspDependencyCheckCvssThresholdValue);
    OwaspDependencyCheckCvssThresholdValue threshold = (OwaspDependencyCheckCvssThresholdValue) n;

    if (expected == null) {
      assertFalse(threshold.specified());
    } else {
      assertTrue(threshold.specified());
      assertEquals(expected, threshold.get());
    }
  }
}