package com.sap.oss.phosphor.fosstars.model.feature.example;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import org.junit.Test;

public class StaticCodeAnalysisDoneExampleTest {

  @Test
  public void name() {
    assertNotNull(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.name());
  }

  @Test
  public void equalsAndHashCode() {
    Feature<Boolean> one = STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
    Feature<Boolean> two = STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void value() {
    assertEquals(Boolean.TRUE, STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(true).get());
    assertEquals(Boolean.FALSE, STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(false).get());
    assertFalse(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(true).isUnknown());
  }

  @Test
  public void unknown() {
    Value<Boolean> value = STATIC_CODE_ANALYSIS_DONE_EXAMPLE.unknown();
    assertTrue(value.isUnknown());
  }

}