package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.PositiveIntegerFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class IntegerValueTest {

  @Test
  public void testSerialization() throws IOException {
    IntegerValue integerValue = new IntegerValue(new PositiveIntegerFeature("test"), 10);
    byte[] bytes = Json.toBytes(integerValue);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    IntegerValue clone = Json.read(bytes, IntegerValue.class);
    assertNotNull(clone);
    assertEquals(integerValue, clone);
    assertEquals(integerValue.hashCode(), clone.hashCode());
  }
}