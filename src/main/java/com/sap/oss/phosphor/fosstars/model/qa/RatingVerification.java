package com.sap.oss.phosphor.fosstars.model.qa;

import com.sap.oss.phosphor.fosstars.model.Rating;
import java.util.Objects;

/**
 * This class implements a verification procedure for a {@link Rating}.
 * The class loads test vectors and provides methods to verify a {@link Rating}
 * against the loaded test vectors.
 */
public class RatingVerification extends AbstractVerification {

  /**
   * A rating to be verified.
   */
  private final Rating rating;

  /**
   * Initializes a {@link RatingVerification} for a {@link Rating}.
   *
   * @param rating A rating to be verified.
   * @param vectors A list of test vectors.
   */
  public RatingVerification(Rating rating, TestVectors vectors) {
    super(vectors);
    Objects.requireNonNull(rating, "Rating can't be null!");
    this.rating = rating;
  }

  /**
   * Verify the rating. The method throws a {@link VerificationFailedException}
   * if the verification failed.
   *
   * @throws VerificationFailedException If the verification failed.
   */
  public final void run() throws VerificationFailedException {
    new RatingVerifier(rating, vectors).verify();
  }

}
