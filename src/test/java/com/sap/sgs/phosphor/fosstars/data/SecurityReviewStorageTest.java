package com.sap.sgs.phosphor.fosstars.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sap.sgs.phosphor.fosstars.data.json.SecurityReviewStorage;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReview;
import com.sap.sgs.phosphor.fosstars.model.value.SecurityReviews;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Test;

public class SecurityReviewStorageTest {

  @Test
  public void testSpringSecurityOAuth() throws IOException, ParseException {
    SecurityReviewStorage storage = SecurityReviewStorage.load();
    assertNotNull(storage);
    SecurityReviews reviews = storage.get("https://github.com/spring-projects/spring-security-oauth");
    assertNotNull(reviews);
    assertEquals(1, reviews.get().size());
    SecurityReview review = reviews.get().iterator().next();
    assertNotNull(review);
    assertEquals("Artem Smotrakov, Phosphor (SAP)", review.who);
    assertNull(review.link);
    Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-06");

    // take into account only date but not time
    long delta = 4 * 60 * 60 * 1000L; // 4 hours
    assertTrue(Math.abs(expectedDate.getTime() - review.when.getTime()) < delta);
  }

  @Test
  public void testUnknownProject() throws IOException {
    SecurityReviewStorage storage = SecurityReviewStorage.load();
    assertNotNull(storage);
    SecurityReviews reviews = storage.get("https://github.com/black/horse");
    assertNotNull(reviews);
    assertTrue(reviews.get().isEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testHttpUrl() throws IOException {
    SecurityReviewStorage storage = SecurityReviewStorage.load();
    assertNotNull(storage);
    storage.get("http://github.com/spring-projects/spring-security-oauth");
  }

}