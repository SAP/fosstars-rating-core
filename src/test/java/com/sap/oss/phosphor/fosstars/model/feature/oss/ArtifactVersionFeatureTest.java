package com.sap.oss.phosphor.fosstars.model.feature.oss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class ArtifactVersionFeatureTest {

  @Test
  public void testParseNotSupported() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> new ArtifactVersionFeature("name").parse("1.2.3"));
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    ArtifactVersionFeature feature = new ArtifactVersionFeature("ArtifactVersionFeature");
    ArtifactVersionFeature clone = Json.read(Json.toBytes(feature), ArtifactVersionFeature.class);
    assertTrue(feature.equals(clone) && clone.equals(feature));
    assertEquals(feature.hashCode(), clone.hashCode());
  }
}
