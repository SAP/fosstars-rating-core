package com.sap.oss.phosphor.fosstars.model.rating.oss;

import static com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScoreTest.allRulesPassed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating.OssRulesOfPlayLabel;
import com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssRulesOfPlayScore;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.UnknownValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import java.util.Set;
import org.junit.Test;

public class OssRulesOfPlayRatingTest {

  private static final OssRulesOfPlayRating RATING
      = RatingRepository.INSTANCE.rating(OssRulesOfPlayRating.class);

  private static final double DELTA = 0.01;

  @Test
  public void testEqualsAndHashCode() {
    OssRulesOfPlayRating rating = new OssRulesOfPlayRating();
    assertEquals(rating, rating);
    assertNotEquals(rating, null);
    OssRulesOfPlayRating anotherRating = new OssRulesOfPlayRating();
    assertTrue(rating.equals(anotherRating) && anotherRating.equals(rating));
    assertEquals(rating.hashCode(), anotherRating.hashCode());
  }

  @Test
  public void testRatingJsonSerialization() throws IOException {
    assertEquals(RATING, Json.read(Json.toBytes(RATING), OssRulesOfPlayRating.class));
  }

  @Test
  public void testRatingYamlSerialization() throws IOException {
    assertEquals(RATING, Yaml.read(Yaml.toBytes(RATING), OssRulesOfPlayRating.class));
  }

  @Test
  public void testRatingValueJsonSerialization() throws IOException {
    RatingValue ratingValue = RATING.calculate(allRulesPassed());
    assertEquals(ratingValue, Json.read(Json.toBytes(ratingValue), RatingValue.class));
  }

  @Test
  public void testRatingValueYamlSerialization() throws IOException {
    RatingValue ratingValue = RATING.calculate(allRulesPassed());
    assertEquals(ratingValue, Yaml.read(Json.toBytes(ratingValue), RatingValue.class));
  }

  @Test
  public void testFeatures() {
    assertFalse(RATING.allFeatures().isEmpty());
    assertEquals(RATING.allFeatures().size(), RATING.score().allFeatures().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLabelsWithWrongScoreValue() {
    RATING.label(new ScoreValue(ExampleScores.SECURITY_SCORE_EXAMPLE));
  }

  // the test cases below implement verification procedure for the rating
  // if necessary, they may be re-written using test vectors

  @Test(expected = IllegalArgumentException.class)
  public void testCalculateWithNoValues() {
    RATING.calculate();
  }

  @Test
  public void testCalculateWithUnknownValues() {
    Set<Value<?>> values = Utils.allUnknown(RATING.allFeatures());
    RatingValue ratingValue = RATING.calculate(values);
    assertFalse(ratingValue.scoreValue().isUnknown());
    assertFalse(ratingValue.scoreValue().isNotApplicable());
    assertEquals(Score.MAX, ratingValue.scoreValue().get(), DELTA);
    assertEquals(Confidence.MIN, ratingValue.confidence(), DELTA);
    assertEquals(OssRulesOfPlayLabel.UNCLEAR, ratingValue.label());
  }

  @Test
  public void testCalculateWithAllPassedRules() {
    RatingValue ratingValue = RATING.calculate(allRulesPassed());
    assertFalse(ratingValue.scoreValue().isUnknown());
    assertFalse(ratingValue.scoreValue().isNotApplicable());
    assertEquals(Score.MAX, ratingValue.scoreValue().get(), DELTA);
    assertEquals(Confidence.MAX, ratingValue.scoreValue().confidence(), DELTA);
    assertEquals(OssRulesOfPlayLabel.PASSED, ratingValue.label());
  }

  @Test
  public void testCalculateWithOneFailedRule() {
    for (Feature<?> feature : RATING.allFeatures()) {
      assertTrue(feature instanceof BooleanFeature);
      ValueSet values = allRulesPassed();
      double expectedScore = Score.MIN;
      OssRulesOfPlayLabel expectedLabel = OssRulesOfPlayLabel.FAILED;
      if (OssRulesOfPlayScore.EXPECTED_FALSE.contains(feature)) {
        values.update(new BooleanValue((BooleanFeature) feature, true));
      } else if (OssRulesOfPlayScore.EXPECTED_TRUE.contains(feature)) {
        values.update(new BooleanValue((BooleanFeature) feature, false));
      } else if (OssRulesOfPlayScore.RECOMMENDED_FALSE.contains(feature)) {
        values.update(new BooleanValue((BooleanFeature) feature, true));
        expectedScore = OssRulesOfPlayScore.SCORE_WITH_WARNING;
        expectedLabel = OssRulesOfPlayLabel.PASSED_WITH_WARNING;
      } else if (OssRulesOfPlayScore.RECOMMENDED_TRUE.contains(feature)) {
        values.update(new BooleanValue((BooleanFeature) feature, false));
        expectedScore = OssRulesOfPlayScore.SCORE_WITH_WARNING;
        expectedLabel = OssRulesOfPlayLabel.PASSED_WITH_WARNING;
      } else {
        fail("Unexpected feature: " + feature);
      }
      RatingValue ratingValue = RATING.calculate(values);
      ScoreValue scoreValue = ratingValue.scoreValue();
      assertFalse(scoreValue.isUnknown());
      assertFalse(scoreValue.isNotApplicable());
      assertEquals(expectedScore, scoreValue.get(), DELTA);
      assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
      assertEquals(expectedLabel, ratingValue.label());
    }
  }

  @Test
  public void testCalculateWithOneUnknownValue() {
    for (Feature<?> feature : RATING.allFeatures()) {
      assertTrue(feature instanceof BooleanFeature);
      ValueSet values = allRulesPassed().update(UnknownValue.of(feature));
      RatingValue ratingValue = RATING.calculate(values);
      ScoreValue scoreValue = ratingValue.scoreValue();
      assertFalse(scoreValue.isUnknown());
      assertFalse(scoreValue.isNotApplicable());
      assertEquals(Score.MAX, scoreValue.get(), DELTA);
      assertTrue(scoreValue.confidence() > Confidence.MIN);
      assertTrue(scoreValue.confidence() < Confidence.MAX);
      assertEquals(OssRulesOfPlayLabel.UNCLEAR, ratingValue.label());
    }
  }
}