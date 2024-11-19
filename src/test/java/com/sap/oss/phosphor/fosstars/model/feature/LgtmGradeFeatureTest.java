package com.sap.oss.phosphor.fosstars.model.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class LgtmGradeFeatureTest {

  @Test
  public void serializeAndDeserialize() throws IOException {
    LgtmGradeFeature feature = new LgtmGradeFeature("feature");
    byte[] bytes = Json.toBytes(feature);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    Object clone = Json.read(bytes, LgtmGradeFeature.class);
    assertNotNull(clone);
    assertEquals(feature, clone);
    assertEquals(feature.hashCode(), clone.hashCode());
  }
}
