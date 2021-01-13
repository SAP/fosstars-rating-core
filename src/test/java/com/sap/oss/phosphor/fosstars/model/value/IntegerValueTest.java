package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.model.feature.PositiveIntegerFeature;
import java.io.IOException;
import org.junit.Test;

public class IntegerValueTest {

  @Test
  public void serialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    IntegerValue integerValue = new IntegerValue(new PositiveIntegerFeature("test"), 10);
    byte[] bytes = mapper.writeValueAsBytes(integerValue);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    IntegerValue clone = mapper.readValue(bytes, IntegerValue.class);
    assertNotNull(clone);
    assertEquals(integerValue, clone);
    assertEquals(integerValue.hashCode(), clone.hashCode());
  }
}