package com.sap.sgs.phosphor.fosstars.model.score.oss;

import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerifier;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.qa.VerificationFailedException;
import com.sap.sgs.phosphor.fosstars.model.tuning.TuningWithCMAES;
import java.io.IOException;

/**
 * This class tunes weights in the open-source security rating to make it pass the test vectors.
 */
public class OssSecurityScoreTuningWithCMAES extends TuningWithCMAES {

  /**
   * A path to weights for the open-source security rating.
   */
  private static final String PATH =
      "src/main/resources/com/sap/sgs/phosphor/fosstars/model/score/oss/"
          + "OssSecurityScoreWeights.json";

  /**
   * Initializes a {@link OssSecurityScoreTuningWithCMAES}.
   *
   * @param score A score to be tuned.
   * @param vectors A list of test vectors.
   */
  OssSecurityScoreTuningWithCMAES(OssSecurityScore score, TestVectors vectors) {
    super(score, new ScoreVerifier(score, vectors));
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
    new OssSecurityScoreTuningWithCMAES(score, verification.vectors()).run();
    score.weights().storeToJson(PATH);
  }
}
