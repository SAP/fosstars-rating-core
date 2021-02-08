package com.sap.oss.phosphor.fosstars.model.score.example;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.oss.phosphor.fosstars.model.Interval;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a sample score which represent a project activity. Only for demo purposes. The score is
 * based on NumberOfCommitsLastMonthExample and NumberOfContributorsLastMonthExample features.
 */
public class ProjectActivityScoreExample extends FeatureBasedScore {

  private static final Map<Interval, Double> NUMBER_OF_COMMITS_TO_POINTS = new HashMap<>();

  static {
    NUMBER_OF_COMMITS_TO_POINTS.put(
        DoubleInterval.init().from(0).to(10).openLeft().closedRight().make(), 2.0);
    NUMBER_OF_COMMITS_TO_POINTS.put(
        DoubleInterval.init().from(10).to(30).openLeft().closedRight().make(), 3.0);
    NUMBER_OF_COMMITS_TO_POINTS.put(
        DoubleInterval.init().from(30).openLeft().positiveInfinity().make(), 5.0);
  }

  private static final Map<Interval, Double> NUMBER_OF_CONTRIBUTORS_TO_POINTS = new HashMap<>();

  static {
    NUMBER_OF_CONTRIBUTORS_TO_POINTS.put(
        DoubleInterval.init().from(0).to(1).openLeft().closedRight().make(), 2.0);
    NUMBER_OF_CONTRIBUTORS_TO_POINTS.put(
        DoubleInterval.init().from(1).to(5).openLeft().closedRight().make(), 3.0);
    NUMBER_OF_CONTRIBUTORS_TO_POINTS.put(
        DoubleInterval.init().from(5).openLeft().positiveInfinity().make(), 5.0);
  }

  ProjectActivityScoreExample() {
    super("Project activity score (example)",
        NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Integer> numberOfCommitsLastMonthValue = findValue(values,
        NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE,
        "Couldn't find number of commits last month!");
    Value<Integer> numberOfContributorsLastMonthValue = findValue(values,
        NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE,
        "Couldn't find number of contributors last month!");

    int numberOfCommitsLastMonth = numberOfCommitsLastMonthValue.get();
    int numberOfContributorsLastMonth = numberOfContributorsLastMonthValue.get();

    if (numberOfCommitsLastMonth < 0) {
      throw new IllegalArgumentException("Number of commits can't be negative!");
    }
    if (numberOfContributorsLastMonth < 0) {
      throw new IllegalArgumentException(
          "Number of contributors can't be negative!");
    }

    double points = 0.0;
    for (Map.Entry<Interval, Double> entry : NUMBER_OF_COMMITS_TO_POINTS.entrySet()) {
      if (entry.getKey().contains(numberOfCommitsLastMonth)) {
        points += entry.getValue();
        break;
      }
    }
    for (Map.Entry<Interval, Double> entry : NUMBER_OF_CONTRIBUTORS_TO_POINTS.entrySet()) {
      if (entry.getKey().contains(numberOfContributorsLastMonth)) {
        points += entry.getValue();
        break;
      }
    }

    return scoreValue(points, numberOfCommitsLastMonthValue, numberOfContributorsLastMonthValue);
  }

}
