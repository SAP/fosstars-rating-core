package com.sap.oss.phosphor.fosstars.model.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class StringFeatureTest {

  @Test
  public void parse() {
    Value<String> value = new StringFeature("name").parse("1.2.3");
    assertEquals("1.2.3", value.get());
  }

  @Test
  public void serializationAndDeserialization() throws IOException {
    StringFeature feature = new StringFeature("ArtifactVersionFeature");
    StringFeature clone = Json.read(Json.toBytes(feature), StringFeature.class);
    assertEquals(feature, clone);
    assertTrue(feature.equals(clone) && clone.equals(feature));
    assertEquals(feature.hashCode(), clone.hashCode());
  }
}