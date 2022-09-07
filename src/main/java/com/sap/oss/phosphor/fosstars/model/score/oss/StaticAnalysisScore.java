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
 *   <li>{@link BanditScore}</li>
 *   <li>{@link PylintScore}</li>
 *   <li>{@link MyPyScore}</li>
 *   <li>{@link GoSecScore}</li>
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
        .set(FindSecBugsScore.class, new ImmutableWeight(0.5))
        .set(BanditScore.class, new ImmutableWeight(0.5))
        .set(PylintScore.class, new ImmutableWeight(0.4))
        .set(MyPyScore.class, new ImmutableWeight(0.3))
        .set(GoSecScore.class, new ImmutableWeight(0.5));
  }

  /**
   * Initializes a new score.
   */
  public StaticAnalysisScore() {
    super("How a project uses static analysis for security testing",
        setOf(new CodeqlScore(), new LgtmScore(), new FindSecBugsScore(), new BanditScore(),
            new PylintScore(), new MyPyScore(), new GoSecScore()),
        initWeights());
  }
}
