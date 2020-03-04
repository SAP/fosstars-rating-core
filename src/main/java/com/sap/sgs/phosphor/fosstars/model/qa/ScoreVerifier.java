package com.sap.sgs.phosphor.fosstars.model.qa;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectorResult.Status;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The verifier checks that a score passes tests defined by test vectors.
 */
public class ScoreVerifier extends AbstractVerifier {

  /**
   * A score to be verified.
   */
  private final Score score;

  /**
   * Initializes a new verifier.
   *
   * @param score A score to be verified.
   * @param vectors A list of test vectors.
   */
  public ScoreVerifier(Score score, List<TestVector> vectors) {
    super(vectors);

    Objects.requireNonNull(score, "Score can't be null!");
    this.score = score;
  }

  List<TestVectorResult> runImpl() {
    List<TestVectorResult> results = new ArrayList<>();

    int index = 0;
    for (TestVector vector : vectors) {
      ScoreValue scoreValue = score.calculate(vector.values());
      double actualScore = scoreValue.get();

      if (vector.expectedScore().contains(actualScore)) {
        results.add(new TestVectorResult(vector, index++, actualScore, Status.PASSED, "Ok"));
      } else {
        String message = String.format(
            "Expected a score in the interval %s but %s returned",
            vector.expectedScore(), actualScore);
        results.add(new TestVectorResult(vector, index++, actualScore, Status.FAILED, message));
      }
    }

    return results;
  }

}
