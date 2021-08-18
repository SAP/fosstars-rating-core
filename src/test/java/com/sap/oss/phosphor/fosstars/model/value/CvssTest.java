package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.MAX;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.MIN;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.V2.Impact.COMPLETE;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.V2.Impact.PARTIAL;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.V3.Impact.HIGH;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.V3.Impact.LOW;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.V3.Impact.NONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import org.junit.Test;

public class CvssTest {

  @Test
  public void testWithValidValues() {
    final double value = 7.2;
    CVSS cvss = new CVSS.V2(value, COMPLETE, PARTIAL, PARTIAL);
    assertEquals(value, cvss.value(), DELTA);
    cvss = new CVSS.V2(MIN, PARTIAL, COMPLETE, PARTIAL);
    assertEquals(MIN, cvss.value(), DELTA);
    cvss = new CVSS.V2(MAX, COMPLETE, COMPLETE, COMPLETE);
    assertEquals(MAX, cvss.value(), DELTA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNegativeValue() {
    new CVSS.V3(-1.0, HIGH, LOW, NONE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithTooBigValue() {
    new CVSS.V3(11.0, HIGH, LOW, NONE);
  }

  @Test
  public void testEqualsAndHashCode() {
    CVSS one = new CVSS.V3(1.0, HIGH, LOW, NONE);
    assertEquals(one, one);

    CVSS two = new CVSS.V3(1.0, HIGH, LOW, NONE);
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());

    CVSS three = new CVSS.V2(1.0, PARTIAL, PARTIAL, COMPLETE);
    assertNotEquals(one, three);

    CVSS four = new CVSS.V3(2.0, HIGH, LOW, NONE);
    assertNotEquals(one, four);
  }

  @Test
  public void testCheckWithValidValues() {
    CVSS.check(MIN);
    CVSS.check(5.0);
    CVSS.check(MAX);
    CVSS.check(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckWithNegativeValue() {
    CVSS.check(-2.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckWithTooBigValue() {
    CVSS.check(42.0);
  }

  @Test
  public void testJsonSerialization() throws IOException {
    CVSS.V2 cvssV2 = new CVSS.V2(2.3, COMPLETE, PARTIAL, PARTIAL);
    assertEquals(cvssV2, Json.read(Json.toBytes(cvssV2), CVSS.V2.class));

    CVSS.V3 cvssV3 = new CVSS.V3(2.3, HIGH, LOW, NONE);
    assertEquals(cvssV3, Json.read(Json.toBytes(cvssV3), CVSS.V3.class));
  }
}