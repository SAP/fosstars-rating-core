package com.sap.oss.phosphor.fosstars.model.rating.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.HAS_SECURITY_POLICY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.USE_REUSE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.ValueSet;
import com.sap.oss.phosphor.fosstars.model.feature.BooleanFeature;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssRulesOfPlayRating.OssRulesOfPlayLabel;
import com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores;
import com.sap.oss.phosphor.fosstars.model.value.BooleanValue;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.UnknownValue;
import com.sap.oss.phosphor.fosstars.model.value.ValueHashSet;
import com.sap.oss.phosphor.fosstars.util.Json;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;

public class OssRulesOfPlayRatingTest {

  private static final OssRulesOfPlayRating RATING = new OssRulesOfPlayRating();

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
  public void testSerialization() throws IOException {
    assertEquals(RATING, Json.read(Json.toBytes(RATING), OssRulesOfPlayRating.class));
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

  @Test
  public void teslLabels() {
    assertEquals(
        RATING.label(new ScoreValue(RATING.score()).set(ScoreValue.MAX).confidence(Confidence.MAX)),
        OssRulesOfPlayLabel.PASS);
    assertEquals(
        RATING.label(new ScoreValue(RATING.score()).set(ScoreValue.MIN).confidence(Confidence.MAX)),
        OssRulesOfPlayLabel.FAIL);
    assertEquals(
        RATING.label(new ScoreValue(RATING.score()).set(8.0).confidence(Confidence.MAX)),
        OssRulesOfPlayLabel.FAIL);
    assertEquals(
        RATING.label(new ScoreValue(RATING.score()).set(ScoreValue.MIN).confidence(5.0)),
        OssRulesOfPlayLabel.UNCLEAR);
    assertEquals(
        RATING.label(new ScoreValue(RATING.score()).set(ScoreValue.MAX).confidence(5.0)),
        OssRulesOfPlayLabel.UNCLEAR);
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
  public void testCalculateWithAllTrueValues() {
    RatingValue ratingValue = RATING.calculate(
        USE_REUSE.value(true),
        HAS_SECURITY_POLICY.value(true),
        HAS_OPEN_PULL_REQUEST_FROM_DEPENDABOT.value(true));
    assertFalse(ratingValue.scoreValue().isUnknown());
    assertFalse(ratingValue.scoreValue().isNotApplicable());
    assertEquals(Score.MAX, ratingValue.scoreValue().get(), DELTA);
    assertEquals(Confidence.MAX, ratingValue.scoreValue().confidence(), DELTA);
    assertEquals(OssRulesOfPlayLabel.PASS, ratingValue.label());
  }

  @Test
  public void testCalculateWithOneFalseValue() {
    for (Feature<?> feature : RATING.allFeatures()) {
      assertTrue(feature instanceof BooleanFeature);
      ValueSet values = allTrueValues().update(new BooleanValue((BooleanFeature) feature, false));
      RatingValue ratingValue = RATING.calculate(values);
      ScoreValue scoreValue = ratingValue.scoreValue();
      assertFalse(scoreValue.isUnknown());
      assertFalse(scoreValue.isNotApplicable());
      assertEquals(Score.MIN, scoreValue.get(), DELTA);
      assertEquals(Confidence.MAX, scoreValue.confidence(), DELTA);
      assertEquals(OssRulesOfPlayLabel.FAIL, ratingValue.label());
    }
  }

  @Test
  public void testCalculateWithOneUnknownValue() {
    for (Feature<?> feature : RATING.allFeatures()) {
      assertTrue(feature instanceof BooleanFeature);
      ValueSet values = allTrueValues().update(UnknownValue.of(feature));
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

  private static ValueSet allTrueValues() {
    return new ValueHashSet(
        RATING.allFeatures().stream()
            .map(BooleanFeature.class::cast)
            .map(feature -> new BooleanValue(feature, true))
            .collect(Collectors.toSet()));
  }
}