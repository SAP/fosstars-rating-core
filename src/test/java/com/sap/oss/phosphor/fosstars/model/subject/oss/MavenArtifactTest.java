package com.sap.oss.phosphor.fosstars.model.subject.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class MavenArtifactTest {

  @Test
  public void testBasics() {
    GitHubProject project = new GitHubProject("org", "test");

    MavenArtifact jar = new MavenArtifact("com.sap.oss.phosphor", "artifact", "1.2.3", project);
    assertEquals("com.sap.oss.phosphor", jar.group());
    assertEquals("artifact", jar.artifact());
    assertTrue(jar.version().isPresent());
    assertEquals("1.2.3", jar.version().get());
    assertEquals("pkg:maven/com.sap.oss.phosphor/artifact@1.2.3", jar.purl());
    assertTrue(jar.project().isPresent());
    assertEquals(project, jar.project().get());

    MavenArtifact jarWithoutProject
        = new MavenArtifact("com.sap.oss.phosphor", "artifact", "1.2.3", null);
    assertEquals("com.sap.oss.phosphor", jarWithoutProject.group());
    assertEquals("artifact", jarWithoutProject.artifact());
    assertTrue(jarWithoutProject.version().isPresent());
    assertEquals("1.2.3", jarWithoutProject.version().get());
    assertEquals("pkg:maven/com.sap.oss.phosphor/artifact@1.2.3", jarWithoutProject.purl());
    assertFalse(jarWithoutProject.project().isPresent());

    MavenArtifact jarWithoutVersion
        = new MavenArtifact("com.sap.oss.phosphor", "artifact", null, null);
    assertEquals("com.sap.oss.phosphor", jarWithoutVersion.group());
    assertEquals("artifact", jarWithoutVersion.artifact());
    assertFalse(jarWithoutVersion.version().isPresent());
    assertEquals("pkg:maven/com.sap.oss.phosphor/artifact", jarWithoutVersion.purl());
  }

  @Test
  public void testJsonSerialization() throws IOException {
    String group = "apache";
    String artifact = "nifi";
    String version = "1.0.0";
    GitHubOrganization apache = new GitHubOrganization(group);
    GitHubProject project = new GitHubProject(apache, artifact);

    MavenArtifact mavenArtifact = new MavenArtifact(group, artifact, version, project);
    byte[] bytes = Json.toBytes(mavenArtifact);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    MavenArtifact clone = Json.read(bytes, MavenArtifact.class);
    assertNotNull(clone);
    assertEquals(mavenArtifact, clone);
    assertEquals(mavenArtifact.hashCode(), clone.hashCode());
    assertEquals(mavenArtifact.artifact(), clone.artifact());
    assertEquals(mavenArtifact.version(), clone.version());
    assertEquals(mavenArtifact.group(), clone.group());
    assertTrue(mavenArtifact.project().isPresent());
    assertEquals(mavenArtifact.project().get(), clone.project().get());
  }
}
