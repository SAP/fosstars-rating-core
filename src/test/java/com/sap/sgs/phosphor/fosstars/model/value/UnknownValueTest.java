package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.Value;
import java.io.IOException;
import org.junit.Test;

public class UnknownValueTest {

  @Test
  public void getFeature() {
    Value value = new UnknownValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    assertEquals(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, value.feature());
  }

  @Test
  public void isUnknown() {
    assertTrue(new UnknownValue<>(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isUnknown());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void get() {
    new UnknownValue<>(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get();
  }

  @Test
  public void serializationAndDeserialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    UnknownValue value = UnknownValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    UnknownValue clone = mapper.readValue(
        mapper.writeValueAsBytes(value), UnknownValue.class);
    assertEquals(value, clone);
    assertEquals(value.hashCode(), clone.hashCode());
  }
}