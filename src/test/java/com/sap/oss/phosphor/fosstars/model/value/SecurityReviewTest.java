package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.util.Date;
import org.junit.Test;

public class SecurityReviewTest {

  private static final Date TEST_REVIEW_DATE = new Date();

  @Test
  public void testWrongChanges() {
    for (Double value : new double[] { -1.0, 2.0 }) {
      try {
        new SecurityReview(TEST_REVIEW_DATE, value);
        fail("No exception thrown!");
      } catch (IllegalArgumentException e) {
        // expected
      }
    }
  }

  @Test
  public void testJsonSerialization() throws IOException {
    SecurityReview review = new SecurityReview(TEST_REVIEW_DATE, 0.0);
    SecurityReview clone = Json.read(Json.toBytes(review), SecurityReview.class);
    assertTrue(review.equals(clone) && clone.equals(review));
    assertEquals(review.hashCode(), clone.hashCode());

    review = new SecurityReview(TEST_REVIEW_DATE, null);
    clone = Json.read(Json.toBytes(review), SecurityReview.class);
    assertTrue(review.equals(clone) && clone.equals(review));
    assertEquals(review.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    SecurityReview review = new SecurityReview(TEST_REVIEW_DATE, 0.0);
    SecurityReview clone = Yaml.read(Json.toBytes(review), SecurityReview.class);
    assertTrue(review.equals(clone) && clone.equals(review));
    assertEquals(review.hashCode(), clone.hashCode());

    review = new SecurityReview(TEST_REVIEW_DATE, null);
    clone = Yaml.read(Json.toBytes(review), SecurityReview.class);
    assertTrue(review.equals(clone) && clone.equals(review));
    assertEquals(review.hashCode(), clone.hashCode());
  }
}