package com.sap.sgs.phosphor.fosstars.model.qa;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
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

  /**
   * Check if the score produces expected score values defined by the test vectors.
   *
   * @return A list of failed test vectors.
   */
  public List<FailedTestVector> runImpl() {
    List<FailedTestVector> failedVectors = new ArrayList<>();

    int index = 0;
    for (TestVector vector : vectors) {
      ScoreValue scoreValue = score.calculate(vector.values());
      double actualScore = scoreValue.score();
      if (!vector.expectedScore().contains(actualScore)) {
        failedVectors.add(new FailedTestVector(
            vector,
            index,
            String.format("Expected a score in the interval %s but %s returned",
                vector.expectedScore(), actualScore)));
      }

      index++;
    }

    return failedVectors;
  }

}
