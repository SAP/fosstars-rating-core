package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import org.junit.Test;

public class UnknownValueTest {

  @Test
  public void getFeature() {
    Feature feature = NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
    Value value = new UnknownValue(feature);
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
}