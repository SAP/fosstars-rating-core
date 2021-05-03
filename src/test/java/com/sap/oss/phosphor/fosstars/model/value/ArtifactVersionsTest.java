package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import org.junit.Test;

public class ArtifactVersionsTest {

  @Test
  public void testSortedByReleaseDate() {
    ArtifactVersion v1 = new ArtifactVersion("1.0.0", LocalDate.now().minusDays(30));
    ArtifactVersion v2 = new ArtifactVersion("1.1.0", LocalDate.now().minusDays(20));
    ArtifactVersion v3 = new ArtifactVersion("1.2.0", LocalDate.now().minusDays(10));
    ArtifactVersion v4 = new ArtifactVersion("2.0.0", LocalDate.now());
    ArtifactVersions v = new ArtifactVersions(v2, v3, v1, v4);

    Collection<ArtifactVersion> sorted = v.sortByReleaseDate();
    ArtifactVersion[] expectedOrder = {v4, v3, v2, v1};
    int i = 0;
    for (ArtifactVersion version : sorted) {
      assertEquals(expectedOrder[i++], version);
    }
  }

  @Test
  public void testJsonSerialization() throws IOException {
    ArtifactVersions versions = new ArtifactVersions(
        new ArtifactVersion("1.0.0", LocalDate.now().minusDays(30)),
        new ArtifactVersion("1.1.0", LocalDate.now().minusDays(20)));
    ArtifactVersions clone = Json.read(Json.toBytes(versions), ArtifactVersions.class);
    assertTrue(versions.equals(clone) && clone.equals(versions));
    assertEquals(versions.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    ArtifactVersions versions = new ArtifactVersions(
        new ArtifactVersion("something", LocalDate.now().minusDays(30)),
        new ArtifactVersion("something else", LocalDate.now().minusDays(20)));
    ArtifactVersions clone = Yaml.read(Yaml.toBytes(versions), ArtifactVersions.class);
    assertTrue(versions.equals(clone) && clone.equals(versions));
    assertEquals(versions.hashCode(), clone.hashCode());
  }
}