package com.sap.sgs.phosphor.fosstars.model.qa;

import com.sap.sgs.phosphor.fosstars.model.Label;
import com.sap.sgs.phosphor.fosstars.model.Rating;
import com.sap.sgs.phosphor.fosstars.model.RatingValue;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A verifier that checks that a rating passes tests defined by test vectors.
 */
public class RatingVerifier {

  private static final Logger LOGGER = LogManager.getLogger(RatingVerifier.class);

  /**
   * A rating to be verified.
   */
  private final Rating rating;

  /**
   * A list of test vectors.
   */
  private final List<TestVector> vectors;

  /**
   * @param rating A rating to be verified.
   * @param vectors A list of test vectors.
   */
  public RatingVerifier(Rating rating, List<TestVector> vectors) {
    Objects.requireNonNull(rating, "Rating can't be null!");
    Objects.requireNonNull(vectors, "Vectors can't be null!");

    if (vectors.isEmpty()) {
      throw new IllegalArgumentException("No test vectors specified!");
    }

    this.rating = rating;
    this.vectors = Collections.unmodifiableList(vectors);
  }

  /**
   * Check if the rating produces expected scores and labels defined by the test vectors.
   *
   * @return A list of failed test vectors.
   */
  public Set<FailedTestVector> failedVectors() {
    Set<FailedTestVector> failedVectors = new HashSet<>();

    int index = 0;
    for (TestVector vector : vectors) {
      RatingValue ratingValue = rating.calculate(vector.values());
      double actualScore = ratingValue.score();
      if (!vector.expectedScore().contains(actualScore)) {
        failedVectors.add(new FailedTestVector(
            vector,
            index,
            String.format("Expected a score in the interval %s but %s returned",
                vector.expectedScore(), actualScore)));
      }

      Label actualLabel = ratingValue.label();
      if (vector.hasLabel() && vector.expectedLabel() != actualLabel) {
        failedVectors.add(new FailedTestVector(
            vector,
            index,
            String.format("Expected label '%s' but '%s' returned",
                vector.expectedLabel(), actualLabel)));

      }

      index++;
    }

    return failedVectors;
  }

  /**
   * Check the rating against the test vectors, and throws a {@link VerificationFailedException}
   * if at least one test vector failed.
   *
   * @throws VerificationFailedException If at least one test vector failed.
   */
  public void run() throws VerificationFailedException {
    Set<FailedTestVector> failedVectors = failedVectors();
    for (FailedTestVector vector : failedVectors) {
      LOGGER.info("Test vector #{} failed", vector.index);
      LOGGER.info("    reason: {}", vector.reason);
      LOGGER.info("    alias: {}", vector.vector.alias());
    }

    if (!failedVectors.isEmpty()) {
      throw new VerificationFailedException();
    }
  }

}
