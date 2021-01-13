package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Test;

public class LgtmGradeTest {

  @Test
  public void serializeAndDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    byte[] bytes = mapper.writeValueAsBytes(LgtmGrade.E);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    Object clone = mapper.readValue(bytes, LgtmGrade.class);
    assertNotNull(clone);
    assertEquals(LgtmGrade.E, clone);
    assertEquals(LgtmGrade.E.hashCode(), clone.hashCode());
  }

}