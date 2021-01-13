package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.model.feature.DoubleFeature;
import java.io.IOException;
import org.junit.Test;

public class DoubleValueTest {

  @Test
  public void serialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    DoubleValue value = new DoubleValue(new DoubleFeature("test"), 10.1);
    byte[] bytes = mapper.writeValueAsBytes(value);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    DoubleValue clone = mapper.readValue(bytes, DoubleValue.class);
    assertNotNull(clone);
    assertEquals(value, clone);
    assertEquals(value.hashCode(), clone.hashCode());
  }
}