package com.sap.oss.phosphor.fosstars.model.feature.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

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