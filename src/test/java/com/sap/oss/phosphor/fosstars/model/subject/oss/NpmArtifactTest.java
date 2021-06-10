package com.sap.oss.phosphor.fosstars.model.subject.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class NpmArtifactTest {

  @Test
  public void testBasics() {
    GitHubProject project = new GitHubProject("org", "test");
    NpmArtifact pkg = new NpmArtifact("name", "1.2.3", project);
    assertEquals("name", pkg.identifier());
    assertEquals("pkg:npm/name@1.2.3", pkg.purl());
    assertTrue(pkg.version().isPresent());
    assertEquals("1.2.3", pkg.version().get());
    assertTrue(pkg.project().isPresent());
    assertEquals(project, pkg.project().get());

    pkg = new NpmArtifact("name", null, null);
    assertEquals("name", pkg.identifier());
    assertEquals("pkg:npm/name", pkg.purl());
    assertFalse(pkg.version().isPresent());
    assertFalse(pkg.project().isPresent());
  }

  @Test
  public void testJsonSerialization() throws IOException {
    String group = "apache";
    String artifact = "nifi";
    String version = "1.0";
    GitHubOrganization apache = new GitHubOrganization(group);
    GitHubProject project = new GitHubProject(apache, artifact);

    NpmArtifact npmArtifact = new NpmArtifact(artifact, version, project);
    byte[] bytes = Json.toBytes(npmArtifact);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    NpmArtifact clone = Json.read(bytes, NpmArtifact.class);
    assertNotNull(clone);
    assertEquals(npmArtifact, clone);
    assertEquals(npmArtifact.hashCode(), clone.hashCode());
    assertEquals(npmArtifact.identifier(), clone.identifier());
    assertTrue(npmArtifact.project().isPresent());
    assertEquals(npmArtifact.project().get(), clone.project().get());
    assertEquals(npmArtifact.version().get(), clone.version().get());
  }
}
