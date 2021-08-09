package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import org.junit.Test;

public class BooleanValueTest {

  private static final BooleanFeature FEATURE = new BooleanFeature("test");

  @Test
  public void testJsonSerialization() throws IOException {
    BooleanValue booleanValue = new BooleanValue(FEATURE, true);
    byte[] bytes = Json.toBytes(booleanValue);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    BooleanValue clone = Json.read(bytes, BooleanValue.class);
    assertNotNull(clone);
    assertEquals(booleanValue, clone);
    assertEquals(booleanValue.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    BooleanValue value = new BooleanValue(FEATURE, false);
    BooleanValue clone = Yaml.read(Yaml.toBytes(value), BooleanValue.class);
    assertTrue(value.equals(clone) && clone.equals(value));
    assertEquals(value.hashCode(), clone.hashCode());
  }

  @Test
  public void testYesNoValues() {
    assertTrue(FEATURE.yes().get());
    assertFalse(FEATURE.no().get());
  }
}