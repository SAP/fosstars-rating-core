package com.sap.oss.phosphor.fosstars.model.qa;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
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
  public ScoreVerifier(Score score, TestVectors vectors) {
    super(vectors);

    Objects.requireNonNull(score, "Score can't be null!");
    this.score = score;
  }

  List<TestVectorResult> runImpl() {
    List<TestVectorResult> results = new ArrayList<>();

    int index = 0;
    for (TestVector vector : vectors) {
      ScoreValue scoreValue = score.calculate(vector.valuesFor(score));
      results.add(testResultFor(vector, scoreValue, index++));
    }

    return results;
  }

}
