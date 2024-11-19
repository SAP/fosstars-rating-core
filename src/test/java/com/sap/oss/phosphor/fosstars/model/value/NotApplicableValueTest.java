package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class NotApplicableValueTest {

  @Test
  public void testIsUnknown() {
    assertFalse(NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isUnknown());
  }

  @Test
  public void testIsNotApplicable() {
    assertTrue(NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isNotApplicable());
  }

  @Test
  public void testGet() {
    assertThrows(
        UnsupportedOperationException.class,
        () -> NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get());
  }

  @Test
  public void testEqualsAndHashCode() {
    NotApplicableValue<Integer> one = NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    NotApplicableValue<Integer> two = NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);

    assertEquals(one, one);
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());

    NotApplicableValue<Integer> three =
        NotApplicableValue.of(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE);
    assertNotEquals(one, three);

    UnknownValue<Integer> four = UnknownValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    assertNotEquals(one, four);
  }

  @Test
  public void testToStringIsNotEmpty() {
    assertFalse(NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).toString().isEmpty());
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    NotApplicableValue<Integer> value = NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    NotApplicableValue<?> clone = Json.read(Json.toBytes(value), NotApplicableValue.class);
    assertEquals(value, clone);
    assertEquals(value.hashCode(), clone.hashCode());
  }
}
