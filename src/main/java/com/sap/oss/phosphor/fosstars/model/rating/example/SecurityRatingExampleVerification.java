package com.sap.oss.phosphor.fosstars.model.rating.example;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample.AWESOME;
import static com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample.AWFUL;
import static com.sap.oss.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample.OKAY;

import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Label;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.qa.RatingVerification;
import com.sap.oss.phosphor.fosstars.model.qa.StandardTestVector;
import com.sap.oss.phosphor.fosstars.model.qa.TestVector;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import java.util.HashSet;
import java.util.Set;

/**
 * This class implements a verification procedure for {@link SecurityRatingExample}.
 * The class defines a set of test vectors, and provides methods
 * to verify a SecurityRatingExample against those test vectors.
 */
public class SecurityRatingExampleVerification extends RatingVerification {

  private static final Interval AWFUL_SCORE = DoubleInterval.closed(0, 1);
  private static final Interval BAD_SCORE = DoubleInterval.closed(1, 5);
  private static final Interval OKAY_SCORE = DoubleInterval.closed(5, 7);
  private static final Interval AWESOME_SCORE = DoubleInterval.closed(9, 10);

  /**
   * Test vectors for {@link SecurityRatingExample}.
   */
  public static final TestVectors TEST_VECTORS = new TestVectors(
      make(0, 0, false, false, AWFUL_SCORE, AWFUL),
      make(7, 1, false, true, BAD_SCORE, OKAY),
      make(100, 20, false, false, BAD_SCORE, OKAY),
      make(100, 20, true, false, OKAY_SCORE, OKAY),
      make(100, 20, false, true, OKAY_SCORE, OKAY),
      make(100, 20, true, true, AWESOME_SCORE, AWESOME)
  );

  public SecurityRatingExampleVerification(SecurityRatingExample rating) {
    super(rating, TEST_VECTORS);
  }

  /**
   * A factory method for a test vector.
   */
  private static TestVector make(int numberOfCommits, int numberOfContributors,
      boolean securityReviewDone, boolean staticAnalysisDone,
      Interval expectedScore, Label expectedLabel) {

    Set<Value<?>> values = new HashSet<>();
    values.add(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(numberOfCommits));
    values.add(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(numberOfContributors));
    values.add(SECURITY_REVIEW_DONE_EXAMPLE.value(securityReviewDone));
    values.add(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(staticAnalysisDone));
    return new StandardTestVector(values, expectedScore, expectedLabel, "unknown");
  }

}
