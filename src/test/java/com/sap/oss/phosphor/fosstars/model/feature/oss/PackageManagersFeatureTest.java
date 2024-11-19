package com.sap.oss.phosphor.fosstars.model.feature.oss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class PackageManagersFeatureTest {

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    PackageManagersFeature feature = new PackageManagersFeature("test");
    PackageManagersFeature clone = Json.read(Json.toBytes(feature), PackageManagersFeature.class);
    assertEquals(feature, clone);
    assertTrue(feature.equals(clone) && clone.equals(feature));
    assertEquals(feature.hashCode(), clone.hashCode());
  }
}
