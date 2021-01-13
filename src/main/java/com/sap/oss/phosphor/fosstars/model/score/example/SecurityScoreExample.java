package com.sap.oss.phosphor.fosstars.model.score.example;

import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.SECURITY_TESTING_SCORE_EXAMPLE;

import com.sap.oss.phosphor.fosstars.model.score.WeightedCompositeScore;

/**
 * This is a sample security score which is based on a weighed average of
 * SecurityTestingScoreExample and ProjectActivityScoreExample scores. Only for demo purposes.
 */
public class SecurityScoreExample extends WeightedCompositeScore {

  public SecurityScoreExample() {
    super("Security score (example)",
        SECURITY_TESTING_SCORE_EXAMPLE, PROJECT_ACTIVITY_SCORE_EXAMPLE);
  }

}
