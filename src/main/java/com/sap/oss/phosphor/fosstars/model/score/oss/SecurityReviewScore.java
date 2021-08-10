package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.SECURITY_REVIEWS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReview;
import com.sap.oss.phosphor.fosstars.model.value.SecurityReviews;
import java.time.Duration;
import java.time.Instant;

/**
 * This scoring function checks if and how security reviews have been done
 * for an open source project. The score is based on
 * {@link com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures#SECURITY_REVIEWS} feature.
 */
public class SecurityReviewScore extends FeatureBasedScore {

  /**
   * Create a new scoring function.
   */
  public SecurityReviewScore() {
    super("How security reviews have been done for an open source project", SECURITY_REVIEWS);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<SecurityReviews> reviews = findValue(values, SECURITY_REVIEWS,
        "Hey! You have to tell me about security reviews!");

    ScoreValue score = scoreValue(MIN, reviews);
    if (reviews.isUnknown()) {
      return score.makeUnknown().explain("No info about security reviews");
    }

    if (reviews.get().isEmpty()) {
      return score.explain("No security reviews have been done");
    }

    double value = MIN;
    Instant now = Instant.now();
    for (SecurityReview review : reviews.get()) {
      value += pointsFor(review, now);
    }

    return score.set(value);
  }

  /**
   * Calculate points for a security review.
   *
   * @param review The security review.
   * @param now Current time.
   * @return Points for the security review.
   */
  static double pointsFor(SecurityReview review, Instant now) {
    if (review.projectChanged().isPresent()) {
      return MAX * (1.0 - review.projectChanged().get());
    }
    long years = (Duration.between(review.date().toInstant(), now).toDays() / 365) + 1;
    return MAX / years;
  }
}
