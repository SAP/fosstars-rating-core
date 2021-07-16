package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.oss.SecurityReviewsFeature;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Date;
import org.junit.Test;

public class SecurityReviewsValueTest {

  @Test
  public void testJsonSerialization() throws IOException {
    SecurityReview firstReview = new SecurityReview(new Date(1), 0.0);
    SecurityReview secondReview = new SecurityReview(new Date(2), 1.0);
    SecurityReviews reviews = new SecurityReviews(firstReview, secondReview);
    SecurityReviewsFeature feature = new SecurityReviewsFeature("feature");
    SecurityReviewsValue value = new SecurityReviewsValue(feature, reviews);

    SecurityReviewsValue clone = Json.read(Json.toBytes(value), SecurityReviewsValue.class);
    assertTrue(value.equals(clone) && clone.equals(value));
    assertEquals(value.hashCode(), clone.hashCode());
  }
}