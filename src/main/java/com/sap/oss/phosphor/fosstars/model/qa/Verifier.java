package com.sap.oss.phosphor.fosstars.model.qa;

import java.util.List;

/**
 * This is an interface of a verifier which can verify scores and ratings.
 * The verification is based on test vectors.
 *
 * @see TestVector
 * @see TestVectorResult
 */
public interface Verifier {

  /**
   * Runs verification and returns a list of results.
   *
   * @return A list of {@link TestVectorResult}.
   */
  List<TestVectorResult> run();

  /**
   * Runs verification and throws a {@link VerificationFailedException} if the verification failed.
   *
   * @throws VerificationFailedException If the verification failed.
   */
  void verify() throws VerificationFailedException;
}
