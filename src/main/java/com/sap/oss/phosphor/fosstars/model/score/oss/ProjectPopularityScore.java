package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;

/**
 * <p>The project popularity score is currently based on two features.</p>
 * <ul>
 *   <li>Number of stars on GitHub</li>
 *   <li>Number of watchers on GitHub</li>
 *   <li>Number of projects that use the project</li>
 * </ul>
 */
public class ProjectPopularityScore extends FeatureBasedScore {

  /**
   * If a number of stars is more than this value, then the maximum score is returned.
   */
  private static final int BEST_STARS_AMOUNT = 10000;

  /**
   * If a number of watchers is more than this value, then the maximum score is returned.
   */
  private static final int BEST_WATCHERS_AMOUNT = 3000;

  /**
   * If a number of dependents is more than this value, then the maximum score is returned.
   */
  private static final int BEST_DEPENDENTS_AMOUNT = 15000;

  /**
   * A description of the score.
   */
  private static final String DESCRIPTION
      = "This scoring function is based on number of stars, watchers and dependent projects.";

  /**
   * Initializes a new score.
   */
  ProjectPopularityScore() {
    super("Open-source project popularity score", DESCRIPTION,
        NUMBER_OF_GITHUB_STARS,
        NUMBER_OF_WATCHERS_ON_GITHUB,
        NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Integer> stars = findValue(values, NUMBER_OF_GITHUB_STARS,
        "Hey! You have to give me a number of stars!");
    Value<Integer> watchers = findValue(values, NUMBER_OF_WATCHERS_ON_GITHUB,
        "Hey! You have to give me a number of watchers!");
    Value<Integer> dependents = findValue(values, NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB,
        "Hey! You have to give me a number of dependents!");

    if (allUnknown(stars, watchers, dependents)) {
      return scoreValue(MIN, stars, watchers, dependents).makeUnknown();
    }

    return scoreValue(MIN, stars, watchers, dependents)
        .increase(subScoreFor(stars, BEST_STARS_AMOUNT))
        .increase(subScoreFor(watchers, BEST_WATCHERS_AMOUNT))
        .increase(subScoreFor(dependents, BEST_DEPENDENTS_AMOUNT));
  }

  /**
   * Calculates a sub-score for a value.
   *
   * @param value A number of stars.
   * @param threshold If the value is greater than the threshold,
   *                  then the max score value is returned.
   * @throws IllegalArgumentException If the value is negative.
   */
  private static double subScoreFor(Value<Integer> value, int threshold) {
    if (value.isUnknown()) {
      return MIN;
    }

    int n = value.get();
    if (n < 0) {
      throw new IllegalArgumentException("Oops! Expected a non-negative value!");
    }

    if (n < threshold) {
      return MAX * n / threshold;
    }

    return MAX;
  }
}
