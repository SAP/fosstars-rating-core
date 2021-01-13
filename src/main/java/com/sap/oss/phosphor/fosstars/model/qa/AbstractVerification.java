package com.sap.oss.phosphor.fosstars.model.qa;

import java.util.Objects;

/**
 * This is a base class for verification procedures for both ratings and scores.
 */
public abstract class AbstractVerification {

  /**
   * A list of test vectors.
   */
  protected final TestVectors vectors;

  /**
   * Initializes a {@link AbstractVerification} with a list of test vectors.
   *
   * @param vectors A list of test vectors.
   */
  AbstractVerification(TestVectors vectors) {
    this.vectors = check(vectors);
  }

  /**
   * Get the test vectors.
   *
   * @return The test vectors.
   */
  public TestVectors vectors() {
    return vectors;
  }

  /**
   * Checks if a list of test vectors is not empty and doesn't contain duplicate entries.
   *
   * @param vectors A list of test vectors to check.
   * @return The same list of test vectors if the check passed.
   */
  private static TestVectors check(TestVectors vectors) {
    Objects.requireNonNull(vectors, "Test vectors can't be null!");

    if (vectors.isEmpty()) {
      throw new IllegalArgumentException(
          "Hey! You are not supposed to give me an empty list of test vectors!");
    }

    return vectors;
  }

}
