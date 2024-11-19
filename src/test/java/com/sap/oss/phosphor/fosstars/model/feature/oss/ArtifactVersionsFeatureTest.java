package com.sap.oss.phosphor.fosstars.model.feature.oss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class ArtifactVersionsFeatureTest {

  @Test
  public void testParseNotSupported() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> new ArtifactVersionsFeature("name").parse("1.2.3"));
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    ArtifactVersionsFeature feature = new ArtifactVersionsFeature("ArtifactVersionFeature");
    ArtifactVersionsFeature clone = Json.read(Json.toBytes(feature), ArtifactVersionsFeature.class);
    assertEquals(feature, clone);
    assertTrue(feature.equals(clone) && clone.equals(feature));
    assertEquals(feature.hashCode(), clone.hashCode());
  }
}
