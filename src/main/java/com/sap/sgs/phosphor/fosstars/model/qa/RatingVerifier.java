package com.sap.sgs.phosphor.fosstars.model.qa;

import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectorResult.Status;
import com.sap.sgs.phosphor.fosstars.model.value.RatingValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The verifier checks that a rating passes tests defined by test vectors.
 */
public class RatingVerifier extends AbstractVerifier {

  /**
   * A rating to be verified.
   */
  private final Rating rating;

  /**
   * Initializes a new verifier.
   *
   * @param rating A rating to be verified.
   * @param vectors A list of test vectors.
   */
  public RatingVerifier(Rating rating, List<TestVector> vectors) {
    super(vectors);

    Objects.requireNonNull(rating, "Rating can't be null!");
    this.rating = rating;
  }

  /**
   * Check if the rating produces expected scores and labels defined by the test vectors.
   *
   * @return A list of failed test vectors.
   */
  List<TestVectorResult> runImpl() {
    List<TestVectorResult> results = new ArrayList<>();

    int index = 0;
    for (TestVector vector : vectors) {
      RatingValue ratingValue = rating.calculate(vector.values());
      double score = ratingValue.score();
      Label actualLabel = ratingValue.label();

      if (!vector.containsExpected(score)) {
        results.add(new TestVectorResult(
            vector,
            index,
            score,
            Status.FAILED,
            String.format("Expected a score in the interval %s but %s returned",
                vector.expectedScore(), score)));
      } else if (vector.hasLabel() && vector.expectedLabel() != actualLabel) {
        results.add(new TestVectorResult(
            vector,
            index,
            score,
            Status.FAILED,
            String.format("Expected label '%s' but '%s' returned",
                vector.expectedLabel(), actualLabel)));
      } else {
        results.add(new TestVectorResult(vector, index++, score, Status.PASSED, "Ok"));
      }

      index++;
    }

    return results;
  }

}
