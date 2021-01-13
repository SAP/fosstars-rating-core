package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import java.io.IOException;
import org.junit.Test;

public class BooleanValueTest {

  @Test
  public void serialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    BooleanValue booleanValue = new BooleanValue(new BooleanFeature("test"), true);
    byte[] bytes = mapper.writeValueAsBytes(booleanValue);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    BooleanValue clone = mapper.readValue(bytes, BooleanValue.class);
    assertNotNull(clone);
    assertEquals(booleanValue, clone);
    assertEquals(booleanValue.hashCode(), clone.hashCode());
  }
}