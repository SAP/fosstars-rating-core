package com.sap.sgs.phosphor.fosstars.model.score.example;

import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;

/**
 * This is a sample score which represent a project activity. Only for demo purposes. The score is
 * based on NumberOfCommitsLastMonthExample and NumberOfContributorsLastMonthExample features.
 */
public class ProjectActivityScoreExample extends FeatureBasedScore {

  ProjectActivityScoreExample() {
    super("Project activity score (example)",
        NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Integer> numberOfCommitsLastMonth = findValue(values,
        NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE,
        "Couldn't find number of commits last month!");
    Value<Integer> numberOfContributorsLastMonth = findValue(values,
        NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE,
        "Couldn't find number of contributors last month!");

    return calculate(numberOfCommitsLastMonth.get(), numberOfContributorsLastMonth.get());
  }

  /**
   * The score function.
   */
  private ScoreValue calculate(int numberOfCommitsLastMonth, int numberOfContributorsLastMonth) {
    if (numberOfCommitsLastMonth < 0) {
      throw new IllegalArgumentException("Number of commits can't be negative!");
    }
    if (numberOfContributorsLastMonth < 0) {
      throw new IllegalArgumentException(
          "Number of contributors can't be negative!");
    }

    double score = 0.0;
    if (numberOfCommitsLastMonth > 0 && numberOfCommitsLastMonth <= 10) {
      score = 2.0;
    }
    if (numberOfCommitsLastMonth > 10 && numberOfCommitsLastMonth <= 30) {
      score = 3.0;
    }
    if (numberOfCommitsLastMonth > 30) {
      score = 5.0;
    }
    if (numberOfContributorsLastMonth > 0 && numberOfContributorsLastMonth <= 1) {
      score += 2.0;
    }
    if (numberOfContributorsLastMonth > 1 && numberOfContributorsLastMonth <= 5) {
      score += 3.0;
    }
    if (numberOfContributorsLastMonth > 5) {
      score += 5.0;
    }

    return new ScoreValue(score, Confidence.MAX);
  }

}
