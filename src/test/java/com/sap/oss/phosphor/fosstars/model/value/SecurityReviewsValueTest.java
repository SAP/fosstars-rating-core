package com.sap.oss.phosphor.fosstars.model.value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.oss.SecurityReviewsFeature;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Date;
import org.junit.Test;

public class SecurityReviewsValueTest {

  @Test
  public void testJsonSerialization() throws IOException {
    GitHubProject project = new GitHubProject("org", "test");
    SecurityReview firstReview = new SecurityReview(project, new Date(1));
    SecurityReview secondReview = new SecurityReview(project, new Date(2));
    SecurityReviews reviews = new SecurityReviews(firstReview, secondReview);
    SecurityReviewsFeature feature = new SecurityReviewsFeature("feature");
    SecurityReviewsValue value = new SecurityReviewsValue(feature, reviews);

    SecurityReviewsValue clone = Json.read(Json.toBytes(value), SecurityReviewsValue.class);
    assertTrue(value.equals(clone) && clone.equals(value));
    assertEquals(value.hashCode(), clone.hashCode());
  }
}