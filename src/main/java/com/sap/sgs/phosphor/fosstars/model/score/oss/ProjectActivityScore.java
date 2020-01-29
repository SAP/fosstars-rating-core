package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.ScoreValue;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import org.apache.commons.math3.analysis.function.Logistic;

/**
 * The project activity score is currently based on two features:
 * - number of commits last three months
 * - number of contributors last three months
 *
 * The score uses the logistic function to transform the numbers to a score value.
 */
public class ProjectActivityScore extends FeatureBasedScore {

  /**
   * y(n) = 7.1 / (1 + e ^ (0.04 * (50 - x))) , where n is a number of commits last three months.
   *
   * y(n) belongs to the interval (0.8, 7.1) when n > 0
   *
   * Number of commits can contribute up to 7 to the overall project activity score.
   */
  static final Logistic LOGISTIC_FOR_NUMBER_OF_COMMITS
      = new Logistic(7.1, 50, 0.04, 1, 0, 1);

  /**
   * y(m) = 3.1 / (1 + e ^ (0.2 * (1 - m))) , where m is a number of contributors last three months.
   *
   * y(m) belongs to the interval (1.3, 3.1) when m > 0
   *
   * Number of contributors can contribute up to 3 to the overall project activity score.
   */
  static final Logistic LOGISTIC_FOR_NUMBER_OF_CONTRIBUTORS
      = new Logistic(3.1, 1, 0.2, 1, 0, 1);

  private static final Feature[] FEATURES = {
      NUMBER_OF_COMMITS_LAST_THREE_MONTHS,
      NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS
  };

  ProjectActivityScore() {
    super("Open-source project activity score", FEATURES);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Integer> n = findValue(values, NUMBER_OF_COMMITS_LAST_THREE_MONTHS,
        "Hey! You have to give me a number of commits!");
    Value<Integer> m = findValue(values, NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS,
        "Hey! You have to give me a number of contributors!");

    check(n, m);

    double score = commitsScore(n) + contributorsScore(m);
    if (score > MAX) {
      score = MAX;
    }

    return new ScoreValue(score, Confidence.make(n, m));
  }

  private double commitsScore(Value<Integer> numberOfCommits) {
    if (numberOfCommits.isUnknown()) {
      return 0;
    }

    int n = numberOfCommits.get();
    if (numberOfCommits.get() == 0) {
      return 0;
    }
    if (n < 0) {
      throw new IllegalArgumentException(
          "Hey! You are're not supposed to give me a negative number of commits!");
    }

    return LOGISTIC_FOR_NUMBER_OF_COMMITS.value(n);
  }

  private double contributorsScore(Value<Integer> numberOfContributors) {
    if (numberOfContributors.isUnknown()) {
      return 0;
    }

    int n = numberOfContributors.get();
    if (numberOfContributors.get() == 0) {
      return 0;
    }
    if (n < 0) {
      throw new IllegalArgumentException(
          "Hey! You are're not supposed to give me a negative number of contributors!");
    }

    return LOGISTIC_FOR_NUMBER_OF_CONTRIBUTORS.value(n);
  }

  private static void check(Value<Integer> commits, Value<Integer> contributors) {
    if (commits.isUnknown() || contributors.isUnknown()) {
      return;
    }

    int n = commits.get();
    int m = contributors.get();

    if (m > n) {
      throw new IllegalArgumentException("More commits than contributors! How is that possible?");
    }

    if (n > 0 && m == 0) {
      throw new IllegalArgumentException("Commits without contributor! How is that possible?");
    }
  }
}
