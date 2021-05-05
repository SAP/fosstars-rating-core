package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.oss.ArtifactVersionFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.Test;

public class ArtifactVersionValueTest {

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    ArtifactVersionFeature feature = new ArtifactVersionFeature("test");
    ArtifactVersion versions = new ArtifactVersion("1.1.1", LocalDateTime.now());
    ArtifactVersionValue value = new ArtifactVersionValue(feature, versions);
    ArtifactVersionValue clone = Json.read(Json.toBytes(value), ArtifactVersionValue.class);
    assertTrue(value.equals(clone) && clone.equals(value));
    assertEquals(value.hashCode(), clone.hashCode());
    assertEquals(value.get().version(), clone.get().version());
    assertEquals(value.get().releaseDate(), clone.get().releaseDate());
    assertEquals(value.get().getSemanticVersion(), clone.get().getSemanticVersion());
  }
}