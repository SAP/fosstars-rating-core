package com.sap.sgs.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.feature.oss.LanguagesFeature;
import java.io.IOException;
import org.junit.Test;

public class LanguagesValueTest {

  @Test
  public void serializationAndDeserialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    LanguagesFeature feature = new LanguagesFeature("test");
    LanguagesValue value = new LanguagesValue(feature, new Languages(Language.JAVA, Language.C));
    LanguagesValue clone = mapper.readValue(mapper.writeValueAsBytes(value), LanguagesValue.class);
    assertTrue(value.equals(clone) && clone.equals(value));
    assertEquals(value.hashCode(), clone.hashCode());
  }
}