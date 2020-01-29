package com.sap.sgs.phosphor.fosstars.model.value;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.junit.Test;

public class SecurityReviewsTest {

  @Test
  public void create() throws MalformedURLException {
    SecurityReview one = new SecurityReview(new URL("http://one/info"), new Date(3), "Фёдор Михайлович Достоевский");
    SecurityReview two = new SecurityReview(new URL("http://two/info"), new Date(3), "Николай Васильевич Гоголь");
    SecurityReview three = new SecurityReview(new URL("http://three/info"), new Date(3), "Рабиндранат Тагор");
    SecurityReviews attempts = new SecurityReviews(one, two);
    assertTrue(attempts.get().contains(one));
    assertTrue(attempts.get().contains(two));
    assertFalse(attempts.get().contains(three));
  }

  @Test(expected = IllegalArgumentException.class)
  public void duplicate() throws MalformedURLException {
    SecurityReview one = new SecurityReview(new URL("http://one/info"), new Date(3), "Венедикт Ерофеев");
    SecurityReview two = new SecurityReview(new URL("http://one/info"), new Date(3), "Венедикт Ерофеев");
    new SecurityReviews(one, two);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void unmodifiable() throws MalformedURLException {
    new SecurityReviews().get().add(new SecurityReview(new URL("http://one/info"), new Date(3), "Венедикт Ерофеев"));
  }

}