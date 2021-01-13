package com.sap.oss.phosphor.fosstars.model.feature.oss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Test;

public class LanguagesFeatureTest {

  @Test
  public void serializationAndDeserialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    LanguagesFeature feature = new LanguagesFeature("test");
    LanguagesFeature clone = mapper.readValue(
        mapper.writeValueAsBytes(feature), LanguagesFeature.class);
    assertEquals(feature, clone);
    assertTrue(feature.equals(clone) && clone.equals(feature));
    assertEquals(feature.hashCode(), clone.hashCode());
  }
}