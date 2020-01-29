package com.sap.sgs.phosphor.fosstars.model.score.example;

import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;

/**
 * This is a sample score which represents a status of security testing. The score is based on
 * SecurityReviewDoneExample and StaticCodeAnalysisDoneExample. Only for demo purposes.
 */
public class SecurityTestingScoreExample extends FeatureBasedScore {

  SecurityTestingScoreExample() {
    super("Security testing score (example)",
        SECURITY_REVIEW_DONE_EXAMPLE, STATIC_CODE_ANALYSIS_DONE_EXAMPLE);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Boolean> securityReviewDone = findValue(values, SECURITY_REVIEW_DONE_EXAMPLE,
        "Couldn't find security review status!");
    Value<Boolean> staticCodeAnalysisDone = findValue(values, STATIC_CODE_ANALYSIS_DONE_EXAMPLE,
        "Couldn't find static code analysis status!");

    return calculate(securityReviewDone.get(), staticCodeAnalysisDone.get());
  }

  /**
   * The score function.
   */
  private ScoreValue calculate(boolean securityReviewDone, boolean staticCodeAnalysisDone) {
    double score = 0.0;
    if (securityReviewDone) {
      score = 5.0;
    }
    if (staticCodeAnalysisDone) {
      score += 5.0;
    }
    return new ScoreValue(score, Confidence.MAX);
  }

}
