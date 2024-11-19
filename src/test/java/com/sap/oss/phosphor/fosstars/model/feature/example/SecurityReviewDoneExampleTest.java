package com.sap.oss.phosphor.fosstars.model.feature.example;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import org.junit.jupiter.api.Test;

public class SecurityReviewDoneExampleTest {

  @Test
  public void name() {
    assertNotNull(SECURITY_REVIEW_DONE_EXAMPLE.name());
  }

  @Test
  public void equalsAndHashCode() {
    Feature<Boolean> one = SECURITY_REVIEW_DONE_EXAMPLE;
    Feature<Boolean> two = SECURITY_REVIEW_DONE_EXAMPLE;
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void value() {
    assertEquals(Boolean.TRUE, SECURITY_REVIEW_DONE_EXAMPLE.value(true).get());
    assertEquals(Boolean.FALSE, SECURITY_REVIEW_DONE_EXAMPLE.value(false).get());
    assertFalse(SECURITY_REVIEW_DONE_EXAMPLE.value(false).isUnknown());
  }

  @Test
  public void unknown() {
    Value<Boolean> value = SECURITY_REVIEW_DONE_EXAMPLE.unknown();
    assertTrue(value.isUnknown());
  }
}
