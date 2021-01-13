package com.sap.oss.phosphor.fosstars.model.rating.example;

import com.sap.oss.phosphor.fosstars.model.RatingRepository;
import com.sap.oss.phosphor.fosstars.model.qa.RatingVerifier;
import com.sap.oss.phosphor.fosstars.model.qa.TestVectors;
import com.sap.oss.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.oss.phosphor.fosstars.model.tuning.TuningWithCMAES;
import java.io.IOException;

/**
 * This class tunes a {@link SecurityRatingExample} to make it pass test vectors.
 */
public class SecurityRatingExampleTuningWithCMAES extends TuningWithCMAES {

  /**
   * A path where a tuned rating should be stored.
   */
  private static final String PATH =
      "src/main/resources/com/sap/oss/phosphor/fosstars/model/rating/example/"
          + "SecurityRatingExample_1_1.json";

  /**
   * Initializes a {@link SecurityRatingExampleTuningWithCMAES}.
   *
   * @param rating A rating to be tuned.
   * @param vectors A list of test vectors.
   */
  SecurityRatingExampleTuningWithCMAES(SecurityRatingExample rating, TestVectors vectors) {
    super(rating, new RatingVerifier(rating, vectors));
  }

  /**
   * Runs the tuning with CMA-ES method for the {@link SecurityRatingExample}.
   *
   * @param args Command-line parameters.
   * @throws IOException If something went wrong.
   * @throws VerificationFailedException
   *         If verification for the {@link SecurityRatingExample} failed.
   */
  public static void main(String... args) throws IOException, VerificationFailedException {
    SecurityRatingExample rating = new SecurityRatingExample();
    SecurityRatingExampleVerification verification = new SecurityRatingExampleVerification(rating);
    new SecurityRatingExampleTuningWithCMAES(rating, verification.vectors()).run();
    RatingRepository.INSTANCE.store(rating, PATH);
  }
}
