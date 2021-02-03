package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class LgtmGradeTest {

  @Test
  public void testSerializeAndDeserialize() throws IOException {
    byte[] bytes = Json.toBytes(LgtmGrade.E);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    Object clone = Json.read(bytes, LgtmGrade.class);
    assertNotNull(clone);
    assertEquals(LgtmGrade.E, clone);
    assertEquals(LgtmGrade.E.hashCode(), clone.hashCode());
  }

}