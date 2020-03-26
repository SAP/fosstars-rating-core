package com.sap.sgs.phosphor.fosstars.model.value;

import static com.sap.sgs.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.score.example.ExampleScores.SECURITY_TESTING_SCORE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample;
import java.io.IOException;
import java.util.Collections;
import org.junit.Test;

public class RatingValueTest {

  @Test
  public void smokeTest() {
    ScoreValue scoreValue = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 0.8, 9.0, Collections.emptyList());
    RatingValue ratingValue = new RatingValue(scoreValue, SecurityLabelExample.OKAY);
    assertEquals(scoreValue.get(), ratingValue.score(), 0.01);
    assertEquals(9.0, ratingValue.confidence(), 0.01);
    assertEquals(SecurityLabelExample.OKAY, ratingValue.label());
  }

  @Test
  public void equalsAndHashCode() {
    ScoreValue scoreValue = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 0.8, 9.0, Collections.emptyList());
    ScoreValue scoreValueClone = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 0.8, 9.0, Collections.emptyList());
    assertEquals(scoreValue, scoreValueClone);
    assertEquals(scoreValue.hashCode(), scoreValueClone.hashCode());

    RatingValue ratingValue = new RatingValue(scoreValue, SecurityLabelExample.OKAY);
    RatingValue ratingValueClone = new RatingValue(scoreValueClone, SecurityLabelExample.OKAY);
    assertEquals(ratingValue, ratingValueClone);

    ScoreValue[] scoreValues = {
        new ScoreValue(SECURITY_TESTING_SCORE_EXAMPLE, 5.1, 0.8, 9.0, Collections.emptyList()),
        new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE, 4.1, 0.8, 9.0, Collections.emptyList()),
        new ScoreValue(PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 0.8, 7.0, Collections.emptyList())
    };

    for (ScoreValue anotherScoreValue : scoreValues) {
      RatingValue anotherRatingValue = new RatingValue(
          anotherScoreValue, SecurityLabelExample.OKAY);
      assertNotEquals(ratingValue, anotherRatingValue);
      assertNotEquals(ratingValue.hashCode(), anotherRatingValue.hashCode());
    }

    RatingValue ratingValueAnotherLabel = new RatingValue(scoreValue, SecurityLabelExample.AWFUL);
    assertNotEquals(ratingValue, ratingValueAnotherLabel);
    assertNotEquals(ratingValue.hashCode(), ratingValueAnotherLabel.hashCode());
  }

  @Test
  public void serializeAndDeserialize() throws IOException {
    ScoreValue scoreValue = new ScoreValue(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, 5.1, 0.8, 9.0, Collections.emptyList());
    RatingValue ratingValue = new RatingValue(scoreValue, SecurityLabelExample.OKAY);
    ObjectMapper mapper = new ObjectMapper();
    byte[] bytes = mapper.writeValueAsBytes(ratingValue);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);
    RatingValue clone = mapper.readValue(bytes, RatingValue.class);
    assertNotNull(clone);
    assertEquals(ratingValue, clone);
    assertEquals(ratingValue.hashCode(), clone.hashCode());
  }

}