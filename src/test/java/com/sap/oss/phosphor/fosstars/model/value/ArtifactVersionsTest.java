package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Collection;
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
}