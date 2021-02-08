package com.sap.oss.phosphor.fosstars.model.qa;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample.AWESOME;
import static com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample.AWFUL;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class RatingVerificationTest {

  @Test
  public void successfulVerification() throws VerificationFailedException {
    SecurityRatingExample rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);

    Set<Value<?>> values = new HashSet<>();
    values.add(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(0));
    values.add(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(0));
    values.add(SECURITY_REVIEW_DONE_EXAMPLE.value(false));
    values.add(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(false));
    StandardTestVector vector = new StandardTestVector(
        values, DoubleInterval.init().from(0).to(1).closed().make(), AWFUL, "unknown");

    RatingVerification verification = new RatingVerification(
        rating, new TestVectors(vector));

    verification.run();
  }

  @Test(expected = VerificationFailedException.class)
  public void failedVerification() throws VerificationFailedException {
    SecurityRatingExample rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);

    Set<Value<?>> values = new HashSet<>();
    values.add(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(0));
    values.add(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(0));
    values.add(SECURITY_REVIEW_DONE_EXAMPLE.value(false));
    values.add(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(false));
    StandardTestVector vector = new StandardTestVector(
        values, DoubleInterval.init().from(8).to(10).closed().make(), AWESOME, "unknown");

    RatingVerification verification = new RatingVerification(
        rating, new TestVectors(vector));

    verification.run();
  }
}