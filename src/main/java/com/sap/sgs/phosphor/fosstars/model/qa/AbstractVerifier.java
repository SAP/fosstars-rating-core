package com.sap.sgs.phosphor.fosstars.model.qa;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractVerifier {

  /**
   * A list of test vectors.
   */
  final List<TestVector> vectors;

  /**
   * A logger.
   */
  private final Logger logger = LogManager.getLogger(getClass());

  /**
   * Initialize a verifier.
   *
   * @param vectors A list of test vectors.
   */
  AbstractVerifier(List<TestVector> vectors) {
    Objects.requireNonNull(vectors, "Vectors can't be null!");

    if (vectors.isEmpty()) {
      throw new IllegalArgumentException("No test vectors specified!");
    }

    this.vectors = Collections.unmodifiableList(vectors);
  }

  /**
   * Check the rating against the test vectors and returns a list of failed test vectors.
   *
   * @return A list of failed test vectors.
   */
  abstract List<FailedTestVector> runImpl();

  /**
   * Check the rating against the test vectors, and throws a {@link VerificationFailedException} if
   * at least one test vector failed.
   *
   * @throws VerificationFailedException If at least one test vector failed.
   */
  public final void run() throws VerificationFailedException {
    List<FailedTestVector> failedVectors = runImpl();
    for (FailedTestVector vector : failedVectors) {
      logger.info("Test vector #{} failed", vector.index);
      logger.info("    reason: {}", vector.reason);
      logger.info("    alias:  {}", vector.vector.alias());
    }

    if (!failedVectors.isEmpty()) {
      throw new VerificationFailedException();
    }
  }
}
