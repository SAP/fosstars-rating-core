package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class VersionRangeTest {

  @Test
  public void testJsonSerialization() throws IOException {
    VersionRange versionRange = new VersionRange("10.0", "20.0", "30.0", null);
    byte[] bytes = Json.toBytes(versionRange);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    VersionRange clone = Json.read(bytes, VersionRange.class);
    assertNotNull(clone);
    assertEquals(versionRange, clone);
    assertEquals(versionRange.hashCode(), clone.hashCode());
  }
}