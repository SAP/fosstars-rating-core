package com.sap.oss.phosphor.fosstars.model.qa;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ScoreVerificationTest {

  @Test
  public void successfulVerification() throws VerificationFailedException {
    SecurityRatingExample rating = RatingRepository.INSTANCE.rating(SecurityRatingExample.class);

    Set<Value<?>> values = new HashSet<>();
    values.add(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(0));
    values.add(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(0));
    values.add(SECURITY_REVIEW_DONE_EXAMPLE.value(false));
    values.add(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(false));
    StandardTestVector vector =
        new StandardTestVector(
            values, DoubleInterval.init().from(0).to(1).closed().make(), null, "unknown");

    ScoreVerification verification = new ScoreVerification(rating.score(), new TestVectors(vector));

    verification.run();
  }

  @Test
  public void failedVerification() {
    assertThrows(
        VerificationFailedException.class,
        () -> {
          SecurityRatingExample rating =
              RatingRepository.INSTANCE.rating(SecurityRatingExample.class);

          Set<Value<?>> values = new HashSet<>();
          values.add(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(0));
          values.add(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(0));
          values.add(SECURITY_REVIEW_DONE_EXAMPLE.value(false));
          values.add(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(false));
          StandardTestVector vector =
              new StandardTestVector(
                  values, DoubleInterval.init().from(8).to(10).closed().make(), null, "unknown");

          ScoreVerification verification =
              new ScoreVerification(rating.score(), new TestVectors(vector));

          verification.run();
        });
  }
}
