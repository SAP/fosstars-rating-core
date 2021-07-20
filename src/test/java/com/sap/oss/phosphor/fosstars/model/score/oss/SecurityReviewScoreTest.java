package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.Score.MAX;
import static com.sap.oss.phosphor.fosstars.model.Score.MIN;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReview;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviews;
import java.sql.Date;
import java.time.Instant;
import org.junit.Test;

public class SecurityReviewScoreTest {

  private static final double DELTA = 0.01;

  @Test
  public void testPointsFor() {
    Instant now = Instant.now();
    assertEquals(
        MAX,
        SecurityReviewScore.pointsFor(new SecurityReview(Date.from(now)), now),
        DELTA);

    Instant lessThanYearAgo = now.minus(180, DAYS);
    assertEquals(
        MAX,
        SecurityReviewScore.pointsFor(new SecurityReview(Date.from(lessThanYearAgo)), now),
        DELTA);

    Instant lastYear = now.minus(400, DAYS);
    assertEquals(
        5.0,
        SecurityReviewScore.pointsFor(new SecurityReview(Date.from(lastYear)), now),
        DELTA);

    Instant twoYearsAgo = now.minus(365 * 2 + 50, DAYS);
    assertEquals(
        3.33,
        SecurityReviewScore.pointsFor(new SecurityReview(Date.from(twoYearsAgo)), now),
        DELTA);

    Instant threeYearsAgo = now.minus(365 * 3 + 50, DAYS);
    assertEquals(
        2.5,
        SecurityReviewScore.pointsFor(new SecurityReview(Date.from(threeYearsAgo)), now),
        DELTA);
  }

  @Test
  public void testCalculate() {
    SecurityReviewScore score = new SecurityReviewScore();
    Instant now = Instant.now();
    Instant lastYear = now.minus(400, DAYS);
    Instant threeYearsAgo = now.minus(365 * 3 + 50, DAYS);

    ScoreValue scoreValue = score.calculate(
        SECURITY_REVIEWS.value(
            new SecurityReviews(
                new SecurityReview(Date.from(lastYear)),
                new SecurityReview(Date.from(threeYearsAgo)))));

    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(7.5, scoreValue.get(), DELTA);
    assertEquals(1, scoreValue.usedValues().size());
    assertEquals(SECURITY_REVIEWS, scoreValue.usedValues().get(0).feature());
  }

  @Test
  public void testCalculateWithUnknownReviews() {
    SecurityReviewScore score = new SecurityReviewScore();
    ScoreValue scoreValue = score.calculate(SECURITY_REVIEWS.unknown());
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(MIN, scoreValue.get(), DELTA);
  }

  @Test
  public void testCalculateWithNoReviews() {
    SecurityReviewScore score = new SecurityReviewScore();
    ScoreValue scoreValue = score.calculate(SECURITY_REVIEWS.value(new SecurityReviews()));
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(MIN, scoreValue.get(), DELTA);
  }
}