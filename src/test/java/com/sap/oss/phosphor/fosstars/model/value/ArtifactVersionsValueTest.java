package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.oss.ArtifactVersionsFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.Test;

public class ArtifactVersionsValueTest {

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    ArtifactVersionsFeature feature = new ArtifactVersionsFeature("test");
    ArtifactVersions versions = new ArtifactVersions(
        new ArtifactVersion("1.1.1", LocalDate.now()));
    ArtifactVersionsValue value = new ArtifactVersionsValue(feature, versions);
    ArtifactVersionsValue clone = Json.read(Json.toBytes(value), ArtifactVersionsValue.class);
    assertTrue(value.equals(clone) && clone.equals(value));
    assertEquals(value.hashCode(), clone.hashCode());
  }
}