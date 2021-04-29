package com.sap.oss.phosphor.fosstars.model.subject.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class NpmArtifactTest {

  @Test
  public void testJsonSerialization() throws IOException {
    String group = "apache";
    String artifact = "nifi";
    GitHubOrganization apache = new GitHubOrganization(group);
    GitHubProject project = new GitHubProject(apache, artifact);

    NpmArtifact npmArtifact = new NpmArtifact(artifact, project);
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
  }
}
