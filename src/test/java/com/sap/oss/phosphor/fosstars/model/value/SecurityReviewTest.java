package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Date;
import org.junit.Test;

public class SecurityReviewTest {

  @Test
  public void testJsonSerialization() throws IOException {
    Date reviewDate = new Date();
    SecurityReview review = new SecurityReview(reviewDate);
    SecurityReview clone = Json.read(Json.toBytes(review), SecurityReview.class);
    assertTrue(review.equals(clone) && clone.equals(review));
    assertEquals(review.hashCode(), clone.hashCode());
  }
}