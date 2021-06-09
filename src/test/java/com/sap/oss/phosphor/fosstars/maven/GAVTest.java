package com.sap.oss.phosphor.fosstars.maven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GAVTest {

  @Test
  public void testEqualsAndHashCode() {
    GAV one = new GAV("com.test.group", "something", "1.0.0");
    assertEquals(one, one);

    GAV two = new GAV("com.test.group", "something", "1.0.0");
    assertTrue(one.equals(two) && two.equals(one));
    assertEquals(one.hashCode(), two.hashCode());

    GAV anotherVersion = new GAV("com.test.group", "something", "2.0.0");
    assertNotEquals(one, anotherVersion);

    GAV noVersion = new GAV("com.test.group", "something", null);
    assertNotEquals(one, noVersion);
  }

  @Test
  public void testParse() {
    assertEquals(
        new GAV("com.test.group", "something", "1.0.0"),
        GAV.parse("com.test.group:something:1.0.0"));

    assertEquals(
        new GAV("com.test.group", "something", null),
        GAV.parse("com.test.group:something"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseTooBig() {
    GAV.parse("com.test.group:artifact:10.0.0:something");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseTooSmall() {
    GAV.parse("com.test.group");
  }

}