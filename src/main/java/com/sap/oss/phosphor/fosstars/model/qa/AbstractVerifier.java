package com.sap.oss.phosphor.fosstars.model.qa;

import com.sap.oss.phosphor.fosstars.model.qa.TestVectorResult.Status;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractVerifier implements Verifier {

  /**
   * A logger.
   */
  private final Logger logger = LogManager.getLogger(getClass());

  /**
   * A list of test vectors.
   */
  final TestVectors vectors;

  /**
   * Initialize a verifier.
   *
   * @param vectors A list of test vectors.
   */
  AbstractVerifier(TestVectors vectors) {
    Objects.requireNonNull(vectors, "Vectors can't be null!");

    if (vectors.isEmpty()) {
      throw new IllegalArgumentException("No test vectors specified!");
    }

    this.vectors = vectors;
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

  /**
   * Verify a score value against a test vector.
   *
   * @param vector The test vector.
   * @param scoreValue The score value to be verified.
   * @param index An index of the test vector.
   * @return A result of the verification.
   */
  static TestVectorResult testResultFor(TestVector vector, ScoreValue scoreValue, int index) {

    // first, check if the test vector expects a not-applicable score value
    if (vector.expectsNotApplicableScore() && scoreValue.isNotApplicable()) {
      return new TestVectorResult(vector, index, scoreValue,
          Status.PASSED, "Ok, score is N/A as expected");
    }
    if (vector.expectsNotApplicableScore() && !scoreValue.isNotApplicable()) {
      return new TestVectorResult(vector, index, scoreValue,
          Status.FAILED, "Expected N/A score, but got a real score value");
    }

    // next, check if the test vector expects an unknown score value
    if (vector.expectsUnknownScore() && scoreValue.isUnknown()) {
      return new TestVectorResult(vector, index, scoreValue,
          Status.PASSED, "Ok, score is unknown as expected");
    }
    if (vector.expectsUnknownScore() && !scoreValue.isUnknown()) {
      return new TestVectorResult(vector, index, scoreValue,
          Status.FAILED, "Expected an unknown score, but got a real score value");
    }

    // now we know that the test vector expects a real score value
    // it means that the vector contains an expected interval

    // then, check if the score value is N/A
    if (scoreValue.isNotApplicable()) {
      String message = String.format(
          "Expected a score in interval %s got N/A", vector.expectedScore());
      return new TestVectorResult(vector, index, scoreValue, Status.FAILED, message);
    }

    // then, check if the score value is unknown
    if (scoreValue.isUnknown()) {
      String message = String.format(
          "Expected a score in interval %s got unknown", vector.expectedScore());
      return new TestVectorResult(vector, index, scoreValue, Status.FAILED, message);
    }

    // finally, check if the score value belongs to the expected interval
    if (!vector.expectedScore().contains(scoreValue.get())) {
      String message = String.format(
          "Expected a score in the interval %s but %s returned",
          vector.expectedScore(), scoreValue.get());
      return new TestVectorResult(vector, index, scoreValue, Status.FAILED, message);
    }

    return new TestVectorResult(vector, index, scoreValue,
        Status.PASSED, "Ok, got an expected score value");
  }
}
