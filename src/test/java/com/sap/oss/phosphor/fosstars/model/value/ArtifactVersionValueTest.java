package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.oss.ArtifactVersionFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class ArtifactVersionValueTest {

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    ArtifactVersionFeature feature = new ArtifactVersionFeature("test");
    ArtifactVersionValue value = new ArtifactVersionValue(feature, "2.3.3");
    ArtifactVersionValue clone = Json.read(Json.toBytes(value), ArtifactVersionValue.class);
    assertTrue(value.equals(clone) && clone.equals(value));
    assertEquals(value.hashCode(), clone.hashCode());
  }
}