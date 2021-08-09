package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.Score.MAX;
import static com.sap.oss.phosphor.fosstars.model.Score.MIN;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS;
import static com.sap.oss.phosphor.fosstars.model.value.SecurityReview.NO_INFO_ABOUT_CHANGES;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReview;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviews;
import java.sql.Date;
import java.time.Instant;
import org.junit.Test;

public class SecurityReviewScoreTest {

  @Test
  public void testPointsFor() {
    Instant now = Instant.now();
    assertEquals(
        MAX,
        SecurityReviewScore.pointsFor(
            new SecurityReview(Date.from(now), 0.0),
            now),
        DELTA);
    assertEquals(
        MAX,
        SecurityReviewScore.pointsFor(
            new SecurityReview(Date.from(now), NO_INFO_ABOUT_CHANGES),
            now),
        DELTA);

    Instant lessThanYearAgo = now.minus(180, DAYS);
    assertEquals(
        9.0,
        SecurityReviewScore.pointsFor(
            new SecurityReview(Date.from(lessThanYearAgo), 0.1),
            now),
        DELTA);
    assertEquals(
        MAX,
        SecurityReviewScore.pointsFor(
            new SecurityReview(Date.from(lessThanYearAgo), NO_INFO_ABOUT_CHANGES),
            now),
        DELTA);

    Instant lastYear = now.minus(400, DAYS);
    assertEquals(
        8.0,
        SecurityReviewScore.pointsFor(
            new SecurityReview(Date.from(lastYear), 0.2),
            now),
        DELTA);
    assertEquals(
        5.0,
        SecurityReviewScore.pointsFor(
            new SecurityReview(Date.from(lastYear), NO_INFO_ABOUT_CHANGES),
            now),
        DELTA);

    Instant twoYearsAgo = now.minus(365 * 2 + 50, DAYS);
    assertEquals(
        7.0,
        SecurityReviewScore.pointsFor(
            new SecurityReview(Date.from(twoYearsAgo), 0.3),
            now),
        DELTA);
    assertEquals(
        3.33,
        SecurityReviewScore.pointsFor(
            new SecurityReview(Date.from(twoYearsAgo), NO_INFO_ABOUT_CHANGES),
            now),
        DELTA);

    Instant threeYearsAgo = now.minus(365 * 3 + 50, DAYS);
    assertEquals(
        5.0,
        SecurityReviewScore.pointsFor(
            new SecurityReview(Date.from(threeYearsAgo), 0.5),
            now),
        DELTA);
    assertEquals(
        2.5,
        SecurityReviewScore.pointsFor(
            new SecurityReview(Date.from(threeYearsAgo), NO_INFO_ABOUT_CHANGES),
            now),
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
                new SecurityReview(Date.from(lastYear), 0.1),
                new SecurityReview(Date.from(threeYearsAgo), 0.4))));

    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(MAX, scoreValue.get(), DELTA);
    assertEquals(1, scoreValue.usedValues().size());
    assertEquals(SECURITY_REVIEWS, scoreValue.usedValues().get(0).feature());

    scoreValue = score.calculate(
        SECURITY_REVIEWS.value(
            new SecurityReviews(
                new SecurityReview(Date.from(lastYear), NO_INFO_ABOUT_CHANGES),
                new SecurityReview(Date.from(threeYearsAgo), NO_INFO_ABOUT_CHANGES))));

    assertEquals(7.5, scoreValue.get(), DELTA);
  }

  @Test
  public void testCalculateWithUnknownReviews() {
    SecurityReviewScore score = new SecurityReviewScore();
    ScoreValue scoreValue = score.calculate(SECURITY_REVIEWS.unknown());
    assertTrue(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertEquals(MIN, scoreValue.confidence(), DELTA);
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