package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.oss.PackageManagersFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class PackageManagersValueTest {

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    PackageManagersFeature feature = new PackageManagersFeature("test");
    PackageManagersValue value = new PackageManagersValue(
        feature, new PackageManagers(PackageManager.MAVEN, PackageManager.OTHER));
    PackageManagersValue clone = Json.read(Json.toBytes(value), PackageManagersValue.class);
    assertTrue(value.equals(clone) && clone.equals(value));
    assertEquals(value.hashCode(), clone.hashCode());
  }
}