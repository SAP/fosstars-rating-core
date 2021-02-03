package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.OwaspDependencyCheckCvssThreshold;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class OwaspDependencyCheckCvssThresholdValueTest {

  private static final OwaspDependencyCheckCvssThreshold FEATURE
      = new OwaspDependencyCheckCvssThreshold();

  private static final double ACCURACY = 0.001;

  @Test
  public void testSpecifiedAndGet() {
    final double cvssScore = 1.23;

    OwaspDependencyCheckCvssThresholdValue value
        = new OwaspDependencyCheckCvssThresholdValue(FEATURE, cvssScore, true);
    assertTrue(value.specified());
    assertEquals(cvssScore, value.get(), ACCURACY);

    value = new OwaspDependencyCheckCvssThresholdValue(FEATURE, cvssScore, false);
    assertFalse(value.specified());

    value = FEATURE.value(cvssScore);
    assertTrue(value.specified());
    assertEquals(cvssScore, value.get(), ACCURACY);

    value = FEATURE.notSpecifiedValue();
    assertFalse(value.specified());
  }

  @Test
  public void testEqualsAndHashCode() {
    OwaspDependencyCheckCvssThresholdValue one
        = new OwaspDependencyCheckCvssThresholdValue(FEATURE, 1.23, true);
    assertEquals(one, one);

    OwaspDependencyCheckCvssThresholdValue two
        = new OwaspDependencyCheckCvssThresholdValue(FEATURE, 1.23, true);
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());

    OwaspDependencyCheckCvssThresholdValue three
        = new OwaspDependencyCheckCvssThresholdValue(FEATURE, 2.34, true);
    assertNotEquals(one, three);

    OwaspDependencyCheckCvssThresholdValue four
        = new OwaspDependencyCheckCvssThresholdValue(FEATURE, 1.23, false);
    assertNotEquals(one, four);

    assertNotEquals(three, four);
  }

  @Test
  public void testSerializationAndDeserialization() throws IOException {
    OwaspDependencyCheckCvssThresholdValue value = FEATURE.value(5.7);
    OwaspDependencyCheckCvssThresholdValue clone = Json.read(
        Json.toBytes(value), OwaspDependencyCheckCvssThresholdValue.class);
    assertEquals(value, clone);
    assertEquals(value.hashCode(), clone.hashCode());
  }
}