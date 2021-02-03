package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.net.URL;
import org.junit.Test;

public class ReferenceTest {

  @Test
  public void testSerialization() throws IOException {
    Reference reference = new Reference("test", new URL("https://blog/post/1"));
    byte[] bytes = Json.toBytes(reference);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    Reference clone = Json.read(bytes, Reference.class);
    assertEquals(reference, clone);
  }

}