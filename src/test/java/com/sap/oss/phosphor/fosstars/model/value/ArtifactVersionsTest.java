package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;

public class ArtifactVersionsTest {

  @Test
  public void testSortedByReleaseDate() {
    ArtifactVersion v1 = new ArtifactVersion("1.0.0", LocalDateTime.now().minusDays(30));
    ArtifactVersion v2 = new ArtifactVersion("1.1.0", LocalDateTime.now().minusDays(20));
    ArtifactVersion v3 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(10));
    ArtifactVersion v4 = new ArtifactVersion("2.0.0", LocalDateTime.now());
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
        new ArtifactVersion("1.0.0", LocalDateTime.now().minusDays(30)),
        new ArtifactVersion("1.1.0", LocalDateTime.now().minusDays(20)));
    ArtifactVersions clone = Json.read(Json.toBytes(versions), ArtifactVersions.class);
    assertTrue(versions.equals(clone) && clone.equals(versions));
    assertEquals(versions.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    ArtifactVersions versions = new ArtifactVersions(
        new ArtifactVersion("something", LocalDateTime.now().minusDays(30)),
        new ArtifactVersion("something else", LocalDateTime.now().minusDays(20)));
    ArtifactVersions clone = Yaml.read(Yaml.toBytes(versions), ArtifactVersions.class);
    assertTrue(versions.equals(clone) && clone.equals(versions));
    assertEquals(versions.hashCode(), clone.hashCode());
  }

  @Test
  public void testGetRangeOfVersionsByMajor() {
    ArtifactVersion v1 = new ArtifactVersion("1.0.0", LocalDateTime.now().minusDays(30));
    ArtifactVersion v2 = new ArtifactVersion("1.1.0", LocalDateTime.now().minusDays(20));
    ArtifactVersion v3 = new ArtifactVersion("1.2.0", LocalDateTime.now().minusDays(10));
    ArtifactVersion v4 = new ArtifactVersion("2.0.0", LocalDateTime.now());
    ArtifactVersion v5 = new ArtifactVersion("2.0.1", LocalDateTime.now().plusDays(1));
    ArtifactVersion v6 = new ArtifactVersion("2.2.1.4-NANO", LocalDateTime.now().plusDays(3));
    ArtifactVersion v7 = new ArtifactVersion("1.2.0.1", LocalDateTime.now().plusDays(10));
    ArtifactVersion v8 = new ArtifactVersion("1.0.0-MIGHTY", LocalDateTime.now().plusDays(20));
    ArtifactVersion v9 = new ArtifactVersion("3.2", LocalDateTime.now().plusDays(30));
    ArtifactVersion v10 =
        new ArtifactVersion("1232.2134234.23423", LocalDateTime.now().plusDays(60));

    ArtifactVersions v = new ArtifactVersions(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10);

    ArtifactVersions filteredRange = v.filterArtifactsByMajorVersion("2.3.0");
    assertTrue(Arrays.asList(v4, v5, v6).containsAll(filteredRange.get()));

    filteredRange = v.filterArtifactsByMajorVersion("1.3.0");
    assertTrue(Arrays.asList(v1, v2, v3, v7, v8).containsAll(filteredRange.get()));
  }
}