package com.sap.sgs.phosphor.fosstars.model.rating.oss;

import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.tuning.MonteCarloWeightsOptimization;
import java.io.IOException;
import java.util.List;

/**
 * This class looks for weights for {@link OssSecurityRating} to make it pass test vectors.
 * The class uses Monte Carlo method.
 * In other words, the class just randomly tries to find correct weights.
 */
public class OssSecurityRatingMonteCarlo extends MonteCarloWeightsOptimization {

  private static final String PATH =
      "src/main/resources/com/sap/sgs/phosphor/fosstars/model/rating/oss/OssSecurityRating_1_0.json";

  public OssSecurityRatingMonteCarlo(OssSecurityRating rating, List<TestVector> vectors, String path) {
    super(rating, vectors, path);
  }

  public static void main(String... args) throws IOException, VerificationFailedException {
    OssSecurityRating rating = RatingRepository.INSTANCE.get(OssSecurityRating.class);
    OssSecurityRatingVerification verification = OssSecurityRatingVerification.createFor(rating);
    new MonteCarloWeightsOptimization(rating, verification.vectors(), PATH).run();
  }
}
