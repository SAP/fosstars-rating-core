package com.sap.oss.phosphor.fosstars.model.qa;

import com.sap.oss.phosphor.fosstars.model.Score;
import java.util.Objects;

/**
 * This class implements a verification procedure for a {@link Score}.
 * The class loads test vectors and provides methods to verify a {@link Score}
 * against the loaded test vectors.
 */
public class ScoreVerification extends AbstractVerification {

  /**
   * A score to be verified.
   */
  private final Score score;

  /**
   * Initializes a {@link ScoreVerification} for a {@link Score}.
   *
   * @param score A score to be verified.
   * @param vectors A list of test vectors.
   */
  public ScoreVerification(Score score, TestVectors vectors) {
    super(vectors);
    Objects.requireNonNull(score, "Score can't be null!");
    this.score = score;
  }

  /**
   * Verify the score. The method throws a {@link VerificationFailedException}
   * if the verification failed.
   *
   * @throws VerificationFailedException If the verification failed.
   */
  public final void run() throws VerificationFailedException {
    new ScoreVerifier(score, vectors).verify();
  }

}
