package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.tuning.MonteCarloWeightsOptimization;
import java.io.IOException;
import java.util.List;

/**
 * This class looks for weights for {@link OssSecurityScore} to make it pass test vectors.
 * The class uses Monte Carlo method.
 * In other words, the class just randomly tries to find correct weights.
 */
public class OssSecurityScoreTuningWithMonteCarlo extends MonteCarloWeightsOptimization {

  private static final String PATH =
      "src/main/resources/com/sap/sgs/phosphor/fosstars/model/score/oss/OssSecurityScore_1_0.json";


  /**
   * Initializes a {@link OssSecurityScoreTuningWithMonteCarlo}.
   *
   * @param score A score to be tuned.
   * @param vectors A list of test vectors.
   * @param path A path to a file where a tuned rating should be stored.
   */
  OssSecurityScoreTuningWithMonteCarlo(
      OssSecurityScore score, List<TestVector> vectors, String path) {
    super(score, vectors, path);
  }

  /**
   * Runs the tuning with Monte Carlo method for the {@link OssSecurityScore}.
   */
  public static void main(String... args) throws VerificationFailedException, IOException {
    OssSecurityScore score = new OssSecurityScore();
    OssSecurityScore.Verification verification = OssSecurityScore.Verification.createFor(score);
    new MonteCarloWeightsOptimization(score, verification.vectors(), PATH).run();
  }
}
