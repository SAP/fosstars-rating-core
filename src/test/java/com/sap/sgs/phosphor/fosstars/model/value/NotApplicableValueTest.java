package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.junit.Test;

public class NotApplicableValueTest {

  @Test
  public void isUnknown() {
    assertFalse(NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isUnknown());
  }

  @Test
  public void isNotApplicable() {
    assertTrue(NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).isNotApplicable());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void get() {
    NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).get();
  }

  @Test
  public void equalsAndHashCode() {
    NotApplicableValue one = NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    NotApplicableValue two = NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);

    assertEquals(one, one);
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());

    NotApplicableValue three = NotApplicableValue.of(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE);
    assertNotEquals(one, three);

    UnknownValue four = UnknownValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    assertNotEquals(one, four);
  }

  @Test
  public void toStringIsNotEmpty() {
    assertFalse(NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE).toString().isEmpty());
  }

  @Test
  public void serializationAndDeserialization() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    NotApplicableValue value = NotApplicableValue.of(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    NotApplicableValue clone = mapper.readValue(
        mapper.writeValueAsBytes(value), NotApplicableValue.class);
    assertEquals(value, clone);
    assertEquals(value.hashCode(), clone.hashCode());
  }
}