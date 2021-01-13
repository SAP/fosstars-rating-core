package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;
import com.sap.oss.phosphor.fosstars.model.weight.ImmutableWeight;
import com.sap.oss.phosphor.fosstars.model.weight.ScoreWeights;

/**
 * <p>The score shows how an open-source project uses static analysis for security testing.</p>
 * <p>It's based on the following sub-scores:</p>
 * <ul>
 *   <li>{@link CodeqlScore}</li>
 *   <li>{@link LgtmScore}</li>
 *   <li>{@link FindSecBugsScore}</li>
 * </ul>
 * <p>The above sub-scores may not apply to all projects. The score considers only the sub-scores
 * that is applicable to a particular project.</p>
 */
public class StaticAnalysisScore extends WeightedCompositeScore {

  /**
   * Initializes weights for sub-scores.
   *
   * @return The weights of sub-scores.
   */
  private static ScoreWeights initWeights() {
    return ScoreWeights.empty()
        .set(CodeqlScore.class, new ImmutableWeight(1.0))
        .set(LgtmScore.class, new ImmutableWeight(1.0))
        .set(FindSecBugsScore.class, new ImmutableWeight(0.5));
  }

  /**
   * Initializes a new score.
   */
  public StaticAnalysisScore() {
    super("How a project uses static analysis for security testing",
        setOf(new CodeqlScore(), new LgtmScore(), new FindSecBugsScore()),
        initWeights());
  }
}
