package com.sap.oss.phosphor.fosstars.model.rating.oss;

import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.BAD;
import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.GOOD;
import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.MODERATE;
import static com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.SecurityLabel.UNCLEAR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import com.sap.oss.phosphor.fosstars.model.Parameter;
import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.other.ImmutabilityChecker;
import com.sap.oss.phosphor.fosstars.model.other.MakeImmutable;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import com.sap.oss.phosphor.fosstars.model.rating.oss.OssSecurityRating.Thresholds;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScore;
import com.sap.oss.phosphor.fosstars.model.score.oss.OssSecurityScoreTest;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Set;
import org.junit.Test;

public class OssSecurityRatingTest {

  @Test
  public void testCalculate() {
    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    Set<Value<?>> values = OssSecurityScoreTest.defaultValues();
    RatingValue ratingValue = rating.calculate(values);
    assertTrue(Score.INTERVAL.contains(ratingValue.score()));
    assertNotNull(ratingValue.label());
    assertNotEquals(UNCLEAR, ratingValue.label());
  }

  @Test
  public void testCalculateWitAllUnknown() {
    Rating rating = RatingRepository.INSTANCE.rating(OssSecurityRating.class);
    Set<Value<?>> values = Utils.allUnknown(rating.allFeatures());
    RatingValue ratingValue = rating.calculate(values);
    assertTrue(ratingValue.scoreValue().isUnknown());
    assertEquals(UNCLEAR, ratingValue.label());
  }

  @Test
  public void testEqualsAndHashCode() {
    OssSecurityRating one = new OssSecurityRating(new OssSecurityScore(), Thresholds.DEFAULT);
    OssSecurityRating two = new OssSecurityRating(new OssSecurityScore(), Thresholds.DEFAULT);

    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void testMakeImmutableWithVisitor() {
    OssSecurityRating r = new OssSecurityRating(new OssSecurityScore(), Thresholds.DEFAULT);

    // first, check that the underlying score is mutable
    assertFalse(r.score().isImmutable());
    for (Parameter parameter : r.score().parameters()) {
      assertFalse(parameter.isImmutable());
    }
    ImmutabilityChecker checker = new ImmutabilityChecker();
    r.accept(checker);
    assertFalse(checker.allImmutable());

    // next, make it immutable
    r.accept(new MakeImmutable());

    // then, check that the underlying score became immutable
    assertTrue(r.score().isImmutable());
    for (Parameter parameter : r.score().parameters()) {
      assertTrue(parameter.isImmutable());
    }
    checker = new ImmutabilityChecker();
    r.accept(checker);
    assertTrue(checker.allImmutable());
  }

  @Test
  public void testLabels() {
    OssSecurityScore score = mock(OssSecurityScore.class);
    OssSecurityRating rating = new OssSecurityRating(score, new Thresholds(1.0, 9.0, 7.0));
    assertEquals(BAD, rating.label(new ScoreValue(score).set(0.5).confidence(8.0)));
    assertEquals(MODERATE, rating.label(new ScoreValue(score).set(1.5).confidence(7.1)));
    assertEquals(GOOD, rating.label(new ScoreValue(score).set(9.5).confidence(9.0)));
    assertEquals(UNCLEAR, rating.label(new ScoreValue(score).set(4.5).confidence(6.0)));
  }
}