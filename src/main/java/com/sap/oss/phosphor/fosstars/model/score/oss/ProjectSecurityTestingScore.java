package com.sap.oss.phosphor.fosstars.model.score.oss;

import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;

/**
 * <p>The security testing score uses the following sub-scores.</p>
 * <ul>
 *  <li>{@link DependencyScanScore}</li>
 *  <li>{@link NoHttpToolScore}</li>
 *  <li>{@link MemorySafetyTestingScore}</li>
 *  <li>{@link StaticAnalysisScore}</li>
 *  <li>{@link FuzzingScore}</li>
 * </ul>
 * <p>There is plenty room for improvements.
 * The score can take into account a lot of other information.</p>
 */
public class ProjectSecurityTestingScore extends WeightedCompositeScore {

  /**
   * Initializes a new score.
   */
  ProjectSecurityTestingScore() {
    super("How well security testing is done for an open-source project",
        new DependencyScanScore(),
        new NoHttpToolScore(),
        new MemorySafetyTestingScore(),
        new StaticAnalysisScore(),
        new FuzzingScore());
  }
}
