package com.sap.sgs.phosphor.fosstars.model.rating.example;

import com.sap.sgs.phosphor.fosstars.model.RatingRepository;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.tuning.MonteCarloWeightsOptimization;
import java.io.IOException;
import java.util.List;

/**
 * This class looks for weights for SecurityRatingExample to make it pass test vectors. The class
 * uses Monte Carlo method. In other words, the class just randomly tries to find correct weights.
 */
public class SecurityRatingExampleMonteCarlo extends MonteCarloWeightsOptimization {

  private static final String PATH =
      "src/main/resources/com/sap/sgs/phosphor/fosstars/model/rating/example/SecurityRatingExample_1_1.json";

  public SecurityRatingExampleMonteCarlo(SecurityRatingExample rating, List<TestVector> vectors, String path) {
    super(rating, vectors, path);
  }

  public static void main(String... args) throws IOException, VerificationFailedException {
    SecurityRatingExample rating = RatingRepository.INSTANCE.get(SecurityRatingExample.class);
    new MonteCarloWeightsOptimization(rating, SecurityRatingExampleVerification.TEST_VECTORS, PATH).run();
  }
}
