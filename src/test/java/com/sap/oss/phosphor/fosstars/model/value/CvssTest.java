package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.value.CVSS.MAX;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.MIN;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.Version.V2;
import static com.sap.oss.phosphor.fosstars.model.value.CVSS.Version.V3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CvssTest {

  private static final double ACCURACY = 0.001;

  @Test
  public void testWithValidValues() {
    final double value = 7.2;
    CVSS cvss = CVSS.v2(value);
    assertEquals(value, cvss.value(), ACCURACY);
    cvss = CVSS.v2(MIN);
    assertEquals(MIN, cvss.value(), ACCURACY);
    cvss = CVSS.v2(MAX);
    assertEquals(MAX, cvss.value(), ACCURACY);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNegativeValue() {
    new CVSS(V3, -1.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithTooBigValue() {
    new CVSS(V3, 11.0);
  }

  @Test
  public void testIsUnknown() {
    assertTrue(new CVSS(V3, null).isUnknown());
    assertFalse(new CVSS(V3, 5.1).isUnknown());
  }

  @Test
  public void testEqualsAndHashCode() {
    CVSS one = new CVSS(V3, 1.0);
    assertEquals(one, one);

    CVSS two = new CVSS(V3, 1.0);
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());

    CVSS three = new CVSS(V2, 1.0);
    assertNotEquals(one, three);

    CVSS four = new CVSS(V3, 2.0);
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
}