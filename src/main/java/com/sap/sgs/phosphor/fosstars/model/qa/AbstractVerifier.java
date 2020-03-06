package com.sap.sgs.phosphor.fosstars.model.qa;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractVerifier implements Verifier {

  /**
   * A list of test vectors.
   */
  final List<TestVector> vectors;

  /**
   * A logger.
   */
  final Logger logger = LogManager.getLogger(getClass());

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
  abstract List<TestVectorResult> runImpl();

  @Override
  public final List<TestVectorResult> run() {
    List<TestVectorResult> results = runImpl();
    for (TestVectorResult vector : results) {
      if (vector.failed()) {
        logger.info("Test vector #{} failed", vector.index);
        logger.info("    reason: {}", vector.message);
        logger.info("    alias:  {}", vector.vector.alias());
      }
    }
    return results;
  }

  @Override
  public void verify() throws VerificationFailedException {
    List<TestVectorResult> results = run();
    for (TestVectorResult result : results) {
      if (result.failed()) {
        throw new VerificationFailedException();
      }
    }
  }
}
