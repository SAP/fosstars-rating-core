package com.sap.sgs.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.junit.Test;

public class SecurityReviewTest {

  @Test
  public void equalsAndHashCode() throws MalformedURLException {
    SecurityReview one = new SecurityReview(new URL("http://one/info"), new Date(3), "Rulon Oboev");
    SecurityReview two = new SecurityReview(new URL("http://one/info"), new Date(3), "Rulon Oboev");
    SecurityReview three = new SecurityReview(new URL("http://three/info"), new Date(), "Rulon Oboev");
    SecurityReview four = new SecurityReview(new URL("http://one/info"), new Date(1), "Rulon Oboev");
    SecurityReview five = new SecurityReview(new URL("http://one/info"), new Date(), "Ashot");

    assertEquals(one, one);
    assertEquals(one, two);
    assertNotEquals(one, null);
    assertNotEquals(one, three);
    assertNotEquals(one, four);
    assertNotEquals(one, five);

    assertEquals(one.hashCode(), one.hashCode());
    assertEquals(one.hashCode(), two.hashCode());
    assertNotEquals(one.hashCode(), three.hashCode());
    assertNotEquals(one.hashCode(), four.hashCode());
    assertNotEquals(one.hashCode(), five.hashCode());
  }

  @Test
  public void fields() throws MalformedURLException {
    SecurityReview one = new SecurityReview(new URL("http://one/info"), new Date(3), "Rulon Oboev");
    assertEquals(new URL("http://one/info"), one.link);
    assertEquals(new Date(3), one.when);
    assertEquals("Rulon Oboev", one.who);
  }
}