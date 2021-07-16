package com.sap.oss.phosphor.fosstars.model.value;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Date;
import org.junit.Test;

public class SecurityReviewsTest {

  @Test
  public void testClone() {
    SecurityReview firstReview = new SecurityReview(new Date(1), 0.0);
    SecurityReview secondReview = new SecurityReview(new Date(2), 1.0);
    SecurityReviews securityReviews = new SecurityReviews(firstReview, secondReview);
    assertEquals(2, securityReviews.size());
    assertTrue(securityReviews.contains(firstReview));
    assertTrue(securityReviews.contains(secondReview));

    SecurityReviews clone = new SecurityReviews(securityReviews);
    assertTrue(securityReviews.equals(clone) && clone.equals(securityReviews));
    assertEquals(securityReviews.hashCode(), clone.hashCode());
    assertTrue(clone.containsAll(asList(firstReview, secondReview)));
  }

  @Test
  public void testJsonSerialization() throws IOException {
    SecurityReview firstReview = new SecurityReview(new Date(1), 0.0);
    SecurityReview secondReview = new SecurityReview(new Date(2), 1.0);
    SecurityReviews securityReviews = new SecurityReviews(firstReview, secondReview);

    SecurityReviews clone = Json.read(Json.toBytes(securityReviews), SecurityReviews.class);
    assertEquals(securityReviews, clone);
  }
}