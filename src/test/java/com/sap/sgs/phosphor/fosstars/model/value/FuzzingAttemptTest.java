package com.sap.sgs.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.junit.Test;

public class FuzzingAttemptTest {

  @Test
  public void equalsAndHashCode() throws MalformedURLException {
    FuzzingAttempt one = new FuzzingAttempt(new URL("http://one/info"), new Date(3), "Rulon Oboev");
    FuzzingAttempt two = new FuzzingAttempt(new URL("http://one/info"), new Date(3), "Rulon Oboev");
    FuzzingAttempt three = new FuzzingAttempt(new URL("http://three/info"), new Date(), "Rulon Oboev");
    FuzzingAttempt four = new FuzzingAttempt(new URL("http://one/info"), new Date(1), "Rulon Oboev");
    FuzzingAttempt five = new FuzzingAttempt(new URL("http://one/info"), new Date(), "Ashot");

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
    FuzzingAttempt one = new FuzzingAttempt(new URL("http://one/info"), new Date(3), "Rulon Oboev");
    assertEquals(new URL("http://one/info"), one.link);
    assertEquals(new Date(3), one.when);
    assertEquals("Rulon Oboev", one.who);
  }
}