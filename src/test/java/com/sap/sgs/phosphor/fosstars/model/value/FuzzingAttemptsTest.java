package com.sap.sgs.phosphor.fosstars.model.value;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.junit.Test;

public class FuzzingAttemptsTest {

  @Test
  public void create() throws MalformedURLException {
    FuzzingAttempt one = new FuzzingAttempt(new URL("http://one/info"), new Date(3), "Фёдор Михайлович Достоевский");
    FuzzingAttempt two = new FuzzingAttempt(new URL("http://two/info"), new Date(3), "Николай Васильевич Гоголь");
    FuzzingAttempt three = new FuzzingAttempt(new URL("http://three/info"), new Date(3), "Рабиндранат Тагор");
    FuzzingAttempts attempts = new FuzzingAttempts(one, two);
    assertTrue(attempts.get().contains(one));
    assertTrue(attempts.get().contains(two));
    assertFalse(attempts.get().contains(three));
  }

  @Test(expected = IllegalArgumentException.class)
  public void duplicate() throws MalformedURLException {
    FuzzingAttempt one = new FuzzingAttempt(new URL("http://one/info"), new Date(3), "Венедикт Ерофеев");
    FuzzingAttempt two = new FuzzingAttempt(new URL("http://one/info"), new Date(3), "Венедикт Ерофеев");
    new FuzzingAttempts(one, two);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void unmodifiable() throws MalformedURLException {
    new FuzzingAttempts().get().add(new FuzzingAttempt(new URL("http://one/info"), new Date(3), "Венедикт Ерофеев"));
  }

}