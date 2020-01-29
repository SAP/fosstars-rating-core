package com.sap.sgs.phosphor.fosstars.model.qa;

import com.sap.sgs.phosphor.fosstars.model.Rating;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * This is a base class for verification procedures for ratings.
 */
public abstract class AbstractVerification {

  /**
   * A rating to be verified.
   */
  protected final Rating rating;

  /**
   * A list of test vectors;
   */
  protected final List<TestVector> vectors;

  /**
   * @param rating A rating to be verified.
   */
  public AbstractVerification(Rating rating, List<TestVector> vectors) {
    Objects.requireNonNull(rating, "Rating can't be null!");
    Objects.requireNonNull(vectors, "Test vectors can't be null!");
    this.rating = rating;
    this.vectors = check(vectors);
  }

  public List<TestVector> vectors() {
    return new ArrayList<>(vectors);
  }

  /**
   * Verify the rating. The method throws a {@link VerificationFailedException} if verification failed.
   * @throws VerificationFailedException If verification failed.
   */
  public final void run() throws VerificationFailedException {
    new RatingVerifier(rating, vectors).run();
  }

  /**
   * Checks if a list of test vectors is not empty, and doesn't contain duplicate entries.
   *
   * @param vectors A list of test vectors to check.
   * @return The same list of test vectors if the check passed.
   */
  private static List<TestVector> check(List<TestVector> vectors) {
    if (vectors.isEmpty()) {
      throw new IllegalArgumentException(
          "Hey! You are not supposed to give me an empty list of test vectors!");
    }
    Set<TestVector> set = new HashSet<>(vectors);
    if (set.size() != vectors.size()) {
      throw new IllegalArgumentException("Hey! Looks like you gave me duplicate test vectors!");
    }
    return vectors;
  }

}
