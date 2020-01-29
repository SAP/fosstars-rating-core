package com.sap.sgs.phosphor.fosstars.model.rating.example;

import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample.AWESOME;
import static com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample.AWFUL;
import static com.sap.sgs.phosphor.fosstars.model.rating.example.SecurityRatingExample.SecurityLabelExample.OKAY;

import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.qa.AbstractVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class implements a verification procedure for {@link SecurityRatingExample}. The class defines a set
 * of test vectors, and provides methods to verify a SecurityRatingExample against those test
 * vectors.
 */
public class SecurityRatingExampleVerification extends AbstractVerification {

  private static final Interval AWFUL_SCORE = DoubleInterval.init().from(0).to(1).closed().make();
  private static final Interval BAD_SCORE = DoubleInterval.init().from(1).to(5).closed().make();
  private static final Interval OKAY_SCORE = DoubleInterval.init().from(5).to(7).closed().make();
  private static final Interval AWESOME_SCORE = DoubleInterval.init().from(9).to(10).closed().make();

  /**
   * Test vectors for {@link SecurityRatingExample}.
   */
  public static final List<TestVector> TEST_VECTORS = Collections.unmodifiableList(Arrays.asList(
      make(0, 0, false, false, AWFUL_SCORE, AWFUL),
      make(7, 1, false, true, BAD_SCORE, OKAY),
      make(100, 20, false, false, BAD_SCORE, OKAY),
      make(100, 20, true, false, OKAY_SCORE, OKAY),
      make(100, 20, false, true, OKAY_SCORE, OKAY),
      make(100, 20, true, true, AWESOME_SCORE, AWESOME)
  ));

  public SecurityRatingExampleVerification(SecurityRatingExample rating) {
    super(rating, TEST_VECTORS);
  }

  /**
   * A factory method for a test vector.
   */
  private static TestVector make(int numberOfCommits, int numberOfContributors,
      boolean securityReviewDone, boolean staticAnalysisDone,
      Interval expectedScore, Label expectedLabel) {

    Set<Value> values = new HashSet<>();
    values.add(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(numberOfCommits));
    values.add(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(numberOfContributors));
    values.add(SECURITY_REVIEW_DONE_EXAMPLE.value(securityReviewDone));
    values.add(STATIC_CODE_ANALYSIS_DONE_EXAMPLE.value(staticAnalysisDone));
    return new TestVector(values, expectedScore, expectedLabel, "unknown");
  }

}
