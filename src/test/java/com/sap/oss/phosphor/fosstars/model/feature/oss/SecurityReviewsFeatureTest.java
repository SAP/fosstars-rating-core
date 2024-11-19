package com.sap.oss.phosphor.fosstars.model.feature.oss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class SecurityReviewsFeatureTest {

  @Test
  public void testJsonSerialization() throws IOException {
    SecurityReviewsFeature feature = new SecurityReviewsFeature("test");
    SecurityReviewsFeature clone = Json.read(Json.toBytes(feature), SecurityReviewsFeature.class);
    assertTrue(feature.equals(clone) && clone.equals(feature));
    assertEquals(feature.hashCode(), clone.hashCode());
  }
}
