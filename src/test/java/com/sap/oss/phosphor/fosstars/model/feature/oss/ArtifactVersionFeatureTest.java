package com.sap.oss.phosphor.fosstars.model.feature.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class ArtifactVersionFeatureTest {

  @Test
  public void testParseValidInput() {
    Value<String> value = new ArtifactVersionFeature("name").parse("1.2.3");
    assertEquals("1.2.3", value.get());
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    ArtifactVersionFeature feature = new ArtifactVersionFeature("ArtifactVersionFeature");
    ArtifactVersionFeature clone = Json.read(Json.toBytes(feature), ArtifactVersionFeature.class);
    assertEquals(feature, clone);
    assertTrue(feature.equals(clone) && clone.equals(feature));
    assertEquals(feature.hashCode(), clone.hashCode());
  }
}