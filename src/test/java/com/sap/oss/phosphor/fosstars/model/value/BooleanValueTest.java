package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class BooleanValueTest {

  @Test
  public void testSerialization() throws IOException {
    BooleanValue booleanValue = new BooleanValue(new BooleanFeature("test"), true);
    byte[] bytes = Json.toBytes(booleanValue);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    BooleanValue clone = Json.read(bytes, BooleanValue.class);
    assertNotNull(clone);
    assertEquals(booleanValue, clone);
    assertEquals(booleanValue.hashCode(), clone.hashCode());
  }
}