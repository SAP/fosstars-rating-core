package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class UnknownValueTest {

  @Test
  public void testGetFeature() {
    Value<Integer> value = new UnknownValue<>(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    assertEquals(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, value.feature());
  }

  @Test
  public void testIsUnknown() {
    assertTrue(new UnknownValue<>(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isUnknown());
  }

  @Test(expected = IllegalStateException.class)
  public void testGet() {
    new UnknownValue<>(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get();
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    UnknownValue<Integer> value = UnknownValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    UnknownValue<?> clone = Json.read(Json.toBytes(value), UnknownValue.class);
    assertEquals(value, clone);
    assertEquals(value.hashCode(), clone.hashCode());
  }

  @Test
  public void testOrElse() {
    assertEquals(42, new UnknownValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).orElse(42));
  }
}