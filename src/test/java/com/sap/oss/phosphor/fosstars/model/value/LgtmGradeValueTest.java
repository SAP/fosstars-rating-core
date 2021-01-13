package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.oss.phosphor.fosstars.model.feature.LgtmGradeFeature;
import java.io.IOException;
import org.junit.Test;

public class LgtmGradeValueTest {

  @Test
  public void serializeAndDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    LgtmGradeFeature feature = new LgtmGradeFeature("feature");
    LgtmGradeValue a = feature.value(LgtmGrade.A_PLUS);
    byte[] bytes = mapper.writeValueAsBytes(a);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    Object clone = mapper.readValue(bytes, LgtmGradeValue.class);
    assertNotNull(clone);
    assertEquals(a, clone);
    assertEquals(a.hashCode(), clone.hashCode());
  }

}