package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.tuning.WeightsOptimizationWithCMAES;
import java.io.IOException;
import java.util.List;

public class OssSecurityScoreTuningWithCMAES extends WeightsOptimizationWithCMAES {

  private static final String PATH =
      "src/main/resources/com/sap/sgs/phosphor/fosstars/model/score/oss/OssSecurityScore_1_0.json";

  /**
   * Initializes a {@link OssSecurityScoreTuningWithCMAES}.
   *
   * @param score A score to be tuned.
   * @param vectors A list of test vectors.
   * @param path A path to a file where a tuned score should be stored.
   */
  OssSecurityScoreTuningWithCMAES(OssSecurityScore score, List<TestVector> vectors, String path) {
    super(score, vectors, path);
  }

  /**
   * Runs the tuning with CMA-ES method for the {@link OssSecurityScore}.
   *
   * @param args Command-line parameters.
   * @throws IOException If something went wrong.
   * @throws VerificationFailedException If verification for the {@link OssSecurityScore} failed.
   */
  public static void main(String... args) throws IOException, VerificationFailedException {
    OssSecurityScore score = new OssSecurityScore();
    OssSecurityScore.Verification verification = OssSecurityScore.Verification.createFor(score);
    new OssSecurityScoreTuningWithCMAES(score, verification.vectors(), PATH).run();
  }
}
