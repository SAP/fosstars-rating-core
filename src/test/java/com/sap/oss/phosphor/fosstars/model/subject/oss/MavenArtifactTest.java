package com.sap.oss.phosphor.fosstars.model.subject.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class MavenArtifactTest {

  @Test
  public void testJsonSerialization() throws IOException {
    String group = "apache";
    String artifact = "nifi";
    GitHubOrganization apache = new GitHubOrganization(group);
    GitHubProject project = new GitHubProject(apache, artifact);

    MavenArtifact mavenArtifact = new MavenArtifact(group, artifact, project);
    byte[] bytes = Json.toBytes(mavenArtifact);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    MavenArtifact clone = Json.read(bytes, MavenArtifact.class);
    assertNotNull(clone);
    assertEquals(mavenArtifact, clone);
    assertEquals(mavenArtifact.hashCode(), clone.hashCode());
    assertEquals(mavenArtifact.artifact(), clone.artifact());
    assertEquals(mavenArtifact.group(), clone.group());
    assertTrue(mavenArtifact.project().isPresent());
    assertEquals(mavenArtifact.project().get(), clone.project().get());
  }
}
