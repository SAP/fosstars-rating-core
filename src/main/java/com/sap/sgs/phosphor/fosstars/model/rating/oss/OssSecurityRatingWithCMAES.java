package com.sap.sgs.phosphor.fosstars.model.rating.oss;

import com.sap.sgs.phosphor.fosstars.model.Version;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.tuning.WeightsOptimizationWithCMAES;
import java.io.IOException;
import java.util.List;

public class OssSecurityRatingWithCMAES extends WeightsOptimizationWithCMAES {

  private static final String PATH =
      "src/main/resources/com/sap/sgs/phosphor/fosstars/model/rating/oss/"
          + "OssSecurityRating_1_0.json";

  /**
   * Initializes a {@link OssSecurityRatingWithCMAES}.
   *
   * @param rating A rating to be tuned.
   * @param vectors A list of test vectors.
   * @param path A path to a file where a tuned rating should be stored.
   */
  OssSecurityRatingWithCMAES(OssSecurityRating rating, List<TestVector> vectors, String path) {
    super(rating, vectors, path);
  }

  /**
   * Runs the tuning with CMA-ES method for the {@link OssSecurityRating}.
   */
  public static void main(String... args) throws IOException, VerificationFailedException {
    OssSecurityRating rating = new OssSecurityRating(Version.OSS_SECURITY_RATING_1_0);
    OssSecurityRatingVerification verification = OssSecurityRatingVerification.createFor(rating);
    new OssSecurityRatingWithCMAES(rating, verification.vectors(), PATH).run();
  }
}
