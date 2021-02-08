package com.sap.oss.phosphor.fosstars.model.score.example;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.SECURITY_REVIEW_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

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
  public ScoreValue calculate(Value<?>... values) {
    Value<Boolean> securityReviewDone = findValue(values, SECURITY_REVIEW_DONE_EXAMPLE,
        "Couldn't find security review status!");
    Value<Boolean> staticCodeAnalysisDone = findValue(values, STATIC_CODE_ANALYSIS_DONE_EXAMPLE,
        "Couldn't find static code analysis status!");

    double points = 0.0;
    if (securityReviewDone.get()) {
      points = 5.0;
    }
    if (staticCodeAnalysisDone.get()) {
      points += 5.0;
    }
    return scoreValue(points, securityReviewDone, staticCodeAnalysisDone);
  }

}
