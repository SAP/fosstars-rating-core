package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.DoubleFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class DoubleValueTest {

  @Test
  public void testSerialization() throws IOException {
    DoubleValue value = new DoubleValue(new DoubleFeature("test"), 10.1);
    byte[] bytes = Json.toBytes(value);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    DoubleValue clone = Json.read(bytes, DoubleValue.class);
    assertNotNull(clone);
    assertEquals(value, clone);
    assertEquals(value.hashCode(), clone.hashCode());
  }
}