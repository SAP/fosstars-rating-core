package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import org.junit.Test;

public class VersionRangeTest {

  @Test
  public void testJsonSerialization() throws IOException {
    VersionRange range = new VersionRange("1.2", "2.3");
    VersionRange clone = Json.read(Json.toBytes(range), VersionRange.class);
    assertEquals(range, clone);
    assertEquals(range.hashCode(), clone.hashCode());

    range = new VersionRange("", "");
    clone = Json.read(Json.toBytes(range), VersionRange.class);
    assertEquals(range, clone);
    assertEquals(range.hashCode(), clone.hashCode());

    range = new VersionRange(null, null);
    clone = Json.read(Json.toBytes(range), VersionRange.class);
    assertEquals(range, clone);
    assertEquals(range.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    VersionRange range = new VersionRange("1.2.3", "1.3.0");
    VersionRange clone = Yaml.read(Yaml.toBytes(range), VersionRange.class);
    assertEquals(range, clone);

    range = new VersionRange("", "");
    clone = Yaml.read(Yaml.toBytes(range), VersionRange.class);
    assertEquals(range, clone);

    range = new VersionRange(null, null);
    clone = Yaml.read(Yaml.toBytes(range), VersionRange.class);
    assertEquals(range, clone);
  }

  @Test
  public void testWithNormalVersions() {
    VersionRange range = new VersionRange("1.2", "2.3");
    assertTrue(range.start().isPresent());
    assertTrue(range.end().isPresent());
  }

  @Test
  public void testWithNulls() {
    VersionRange range = new VersionRange(null, null);
    assertFalse(range.start().isPresent());
    assertFalse(range.end().isPresent());
  }

}