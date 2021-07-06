package com.sap.oss.phosphor.fosstars.model.qa;

import com.sap.oss.phosphor.fosstars.model.Rating;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectorResult.Status;
import com.sap.oss.phosphor.fosstars.model.value.RatingValue;
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
  public RatingVerifier(Rating rating, TestVectors vectors) {
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
      RatingValue ratingValue = rating.calculate(vector.valuesFor(rating));
      results.add(testResultFor(vector, ratingValue, index++));
    }

    return results;
  }

  /**
   * Verifies a rating value against a test vector.
   *
   * @param vector The test vector.
   * @param ratingValue The rating value.
   * @param index An index of the test vector.
   * @return A result of the verification.
   */
  private static TestVectorResult testResultFor(
      TestVector vector, RatingValue ratingValue, int index) {

    TestVectorResult result = testResultFor(vector, ratingValue.scoreValue(), index);
    if (result.status == Status.FAILED) {
      return result;
    }

    if (unexpectedLabel(vector, ratingValue)) {
      return new TestVectorResult(
          vector,
          index,
          ratingValue.scoreValue(),
          Status.FAILED,
          String.format("Expected label '%s' but '%s' returned",
              vector.expectedLabel(), ratingValue.label()));
    }

    return result;
  }

  /**
   * Checks if a rating value has an unexpected label.
   *
   * @param vector A test vector that contains an expected label (if any).
   * @param ratingValue The rating value to be verified.
   * @return True if the rating value has an unexpected label, false otherwise.
   */
  private static boolean unexpectedLabel(TestVector vector, RatingValue ratingValue) {
    if (!vector.hasLabel()) {
      return false;
    }

    return !vector.expectedLabel().equals(ratingValue.label());
  }

}
