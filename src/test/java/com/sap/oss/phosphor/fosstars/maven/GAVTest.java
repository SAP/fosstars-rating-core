package com.sap.oss.phosphor.fosstars.maven;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
        new GAV("com.test.group", "something", null), GAV.parse("com.test.group:something"));
  }

  @Test
  public void testParseTooBig() {
    assertThrows(
        IllegalArgumentException.class,
        () -> GAV.parse("com.test.group:artifact:10.0.0:something"));
  }

  @Test
  public void testParseTooSmall() {
    assertThrows(IllegalArgumentException.class, () -> GAV.parse("com.test.group"));
  }
}
