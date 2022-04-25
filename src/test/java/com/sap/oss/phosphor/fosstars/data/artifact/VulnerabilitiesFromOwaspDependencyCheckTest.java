package com.sap.oss.phosphor.fosstars.data.artifact;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.VULNERABILITIES_IN_ARTIFACT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.sap.oss.phosphor.fosstars.data.owasp.model.OwaspDependencyCheckEntry;
import com.sap.oss.phosphor.fosstars.model.subject.oss.MavenArtifact;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.model.value.Vulnerabilities;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.Test;

public class VulnerabilitiesFromOwaspDependencyCheckTest {

  private static final MavenArtifact MAVEN_ARTIFACT =
      new MavenArtifact("com.fasterxml.jackson.core", "jackson-databind", "2.9.1", null);

  @Test
  public void testVulnerabilitiesAvailable() throws IOException {
    VulnerabilitiesFromOwaspDependencyCheck provider =
        new VulnerabilitiesFromOwaspDependencyCheck();
    provider = spy(provider);

    String content =
        getClass().getResource("VulnerabilitiesFromOwaspDependencyCheck.json").getFile();
    Optional<OwaspDependencyCheckEntry> entry =
        Optional.of(Json.mapper().readValue(new File(content), OwaspDependencyCheckEntry.class));

    doReturn(entry).when(provider).scan(MAVEN_ARTIFACT);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(MAVEN_ARTIFACT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(VULNERABILITIES_IN_ARTIFACT));
    assertTrue(values.of(VULNERABILITIES_IN_ARTIFACT).isPresent());
    assertFalse(values.of(VULNERABILITIES_IN_ARTIFACT).get().isUnknown());

    Vulnerabilities vulnerabilities = values.of(VULNERABILITIES_IN_ARTIFACT).get().get();
    assertEquals(3, vulnerabilities.size());

    Set<String> vulnerabilityIds = new HashSet<>();
    vulnerabilities.entries().forEach((vulnerability) -> vulnerabilityIds.add(vulnerability.id()));
    assertTrue(vulnerabilityIds.contains("CVE-2018-11307"));
  }

  @Test
  public void testVulnerabilitiesNotAvailable() throws IOException {
    VulnerabilitiesFromOwaspDependencyCheck provider =
        new VulnerabilitiesFromOwaspDependencyCheck();
    provider = spy(provider);

    String content =
        getClass().getResource("VulnerabilitiesFromOwaspNoDependencies.json").getFile();
    Optional<OwaspDependencyCheckEntry> entry =
        Optional.of(Json.mapper().readValue(new File(content), OwaspDependencyCheckEntry.class));
    doReturn(entry).when(provider).scan(MAVEN_ARTIFACT);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(MAVEN_ARTIFACT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(VULNERABILITIES_IN_ARTIFACT));
    assertTrue(values.of(VULNERABILITIES_IN_ARTIFACT).isPresent());
    assertTrue(values.of(VULNERABILITIES_IN_ARTIFACT).get().isUnknown());
  }

  @Test
  public void testVulnerabilitiesNoDependencies() throws IOException {
    VulnerabilitiesFromOwaspDependencyCheck provider =
        new VulnerabilitiesFromOwaspDependencyCheck();
    provider = spy(provider);

    String content =
        getClass().getResource("VulnerabilitiesFromOwaspNoDependencies.json").getFile();
    Optional<OwaspDependencyCheckEntry> entry =
        Optional.of(Json.mapper().readValue(new File(content), OwaspDependencyCheckEntry.class));

    doReturn(entry).when(provider).scan(MAVEN_ARTIFACT);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(MAVEN_ARTIFACT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(VULNERABILITIES_IN_ARTIFACT));
    assertTrue(values.of(VULNERABILITIES_IN_ARTIFACT).isPresent());
    assertTrue(values.of(VULNERABILITIES_IN_ARTIFACT).get().isUnknown());
  }

  @Test(expected = IOException.class)
  public void tesArtifactHasNoVersion() throws IOException {
    VulnerabilitiesFromOwaspDependencyCheck provider =
        new VulnerabilitiesFromOwaspDependencyCheck();

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(new MavenArtifact("group", "artifact", null, null), values);
  }

  @Test
  public void testDependencyHasNoVulnerabilities() throws IOException {
    VulnerabilitiesFromOwaspDependencyCheck provider =
        new VulnerabilitiesFromOwaspDependencyCheck();
    provider = spy(provider);

    String content = getClass().getResource("OwaspDependencyHasNoVulnerabilities.json").getFile();
    Optional<OwaspDependencyCheckEntry> entry =
        Optional.of(Json.mapper().readValue(new File(content), OwaspDependencyCheckEntry.class));

    doReturn(entry).when(provider).scan(MAVEN_ARTIFACT);

    ValueHashSet values = new ValueHashSet();
    assertEquals(0, values.size());

    provider.update(MAVEN_ARTIFACT, values);

    assertEquals(1, values.size());
    assertTrue(values.has(VULNERABILITIES_IN_ARTIFACT));
    assertTrue(values.of(VULNERABILITIES_IN_ARTIFACT).isPresent());
    assertFalse(values.of(VULNERABILITIES_IN_ARTIFACT).get().isUnknown());
    assertEquals(0, values.of(VULNERABILITIES_IN_ARTIFACT).get().get().size());
  }
}