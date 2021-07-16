package com.sap.oss.phosphor.fosstars.model.value;

import static com.sap.oss.phosphor.fosstars.model.Subject.castAndCopy;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.feature.oss.SecurityReviewsFeature;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample;
import com.sap.oss.phosphor.fosstars.model.subject.oss.GitHubProject;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import org.junit.Test;

public class SecurityReviewsValueTest {

  @Test
  public void testJsonSerialization() throws IOException {
    GitHubProject project = new GitHubProject("org", "test");
    SecurityReview firstReview = new SecurityReview(copy(project), new Date(1));
    SecurityReview secondReview = new SecurityReview(copy(project), new Date(2));
    SecurityReviews reviews = new SecurityReviews(firstReview, secondReview);
    SecurityReviewsFeature feature = new SecurityReviewsFeature("feature");
    SecurityReviewsValue value = new SecurityReviewsValue(feature, reviews);

    SecurityReviewsValue clone = Json.read(Json.toBytes(value), SecurityReviewsValue.class);
    assertTrue(value.equals(clone) && clone.equals(value));
    assertEquals(value.hashCode(), clone.hashCode());
  }

  @Test
  public void testNoInfiniteRecursionLoopExists() throws IOException {
    GitHubProject project = new GitHubProject("org", "test");

    ScoreValue scoreValue = new ScoreValue(
            PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 0.8, 9.0, Collections.emptyList());
    RatingValue ratingValue = new RatingValue(scoreValue, SecurityLabelExample.OKAY);
    project.set(ratingValue);

    SecurityReview firstReview = new SecurityReview(copy(project), new Date(1));
    SecurityReviews reviews = new SecurityReviews(firstReview);
    SecurityReviewsFeature feature = new SecurityReviewsFeature("feature");
    SecurityReviewsValue value = new SecurityReviewsValue(feature, reviews);
    scoreValue.usedValues(value);

    SecurityReviewsValue clone = Json.read(Json.toBytes(value), SecurityReviewsValue.class);
    assertTrue(value.equals(clone) && clone.equals(value));
    assertEquals(value.hashCode(), clone.hashCode());
  }

  private static GitHubProject copy(GitHubProject project) throws IOException {
    return castAndCopy(project, GitHubProject.class);
  }
}