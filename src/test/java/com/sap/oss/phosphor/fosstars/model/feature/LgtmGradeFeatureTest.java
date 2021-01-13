package com.sap.oss.phosphor.fosstars.model.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Test;

public class LgtmGradeFeatureTest {

  @Test
  public void serializeAndDeserialize() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    LgtmGradeFeature feature = new LgtmGradeFeature("feature");
    byte[] bytes = mapper.writeValueAsBytes(feature);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    Object clone = mapper.readValue(bytes, LgtmGradeFeature.class);
    assertNotNull(clone);
    assertEquals(feature, clone);
    assertEquals(feature.hashCode(), clone.hashCode());
  }

}