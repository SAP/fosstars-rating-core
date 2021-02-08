package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>The project activity score evaluates how active an open-source project is.
 * It is currently based on two features.</p>
 * <ul>
 *  <li>Number of commits in the last three months</li>
 *  <li>Number of contributors in the last three months</li>
 * </ul>
 * <p>Here is how the score evaluates a number of commits in the last 3 month.
 * First, the score supposes that it's good if a project receives more than 5 commits in a week.
 * Assuming that 3 months is around 13 weeks, the score supposes that a project is active
 * if it receives more than 65 commits in the last 3 months.
 * In this case the score value is set to 10 (max).
 * If a project received 0 commits in the last 3 months, then the score value is set to 0 (min).
 * If a number of commits is between 0 and 65,
 * then the score uses a linear function to calculate the score value.</p>
 * <p>Here is how the score takes into account a number of contributors in the last 3 months.
 * If it's just 1 contributor, then the score value doesn't change.
 * If it was more than 1 contributor, then the score uses an additional factor for the score value.
 * </p>
 * <ul>
 *   <li>If it's 2 contributors, the factor is 0.05</li>
 *   <li>If it's 3-4 contributors, the factor is 0.1</li>
 *   <li>If it's more than 4 contributors, the factor is 0.2</li>
 * </ul>
 */
public class ProjectActivityScore extends FeatureBasedScore {

  private static final int GOOD_NUMBER_OF_COMMITS = 65;

  private static final Map<Integer, Double> CONTRIBUTOR_FACTOR = new TreeMap<>();

  static {
    CONTRIBUTOR_FACTOR.put(2, 0.05);
    CONTRIBUTOR_FACTOR.put(3, 0.1);
    CONTRIBUTOR_FACTOR.put(5, 0.2);
  }

  /**
   * A description of the score.
   */
  private static final String DESCRIPTION =
      "The score evaluates how active a project is. "
          + "It's based on number of commits and contributors in the last 3 months.";

  /**
   * Initializes a new score.
   */
  ProjectActivityScore() {
    super("Open-source project activity score", DESCRIPTION,
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS, NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS);
  }

  @Override
  public ScoreValue calculate(Value<?>... values) {
    Value<Integer> commits = findValue(values, NUMBER_OF_COMMITS_LAST_THREE_MONTHS,
        "Hey! You have to give me a number of commits!");
    Value<Integer> contributors = findValue(values, NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS,
        "Hey! You have to give me a number of contributors!");

    ScoreValue scoreValue = scoreValue(MIN, commits, contributors);

    // if the number of commits is unknown, then we can't calculate the score value
    // set the confidence to min, even if we know the number of contributors
    if (commits.isUnknown()) {
      return scoreValue
          .makeUnknown()
          .withMinConfidence()
          .explain("A score value couldn't be calculated because the number of commits is unknown");
    }

    if (commits.isNotApplicable()) {
      return scoreValue
          .withMinConfidence()
          .explain("A score value couldn't be calculated because the number of commits "
              + "is marked as N/A for some reason. Please check the data.");
    }

    if (commits.get() < 0) {
      return scoreValue
          .withMinConfidence()
          .explain("A score value couldn't be calculated because the number of commits "
                  + "is negative (%d). Please check the data.", commits.get());
    }

    // otherwise, calculate a sub-score based on the number of commits
    double value;
    if (commits.get() >= GOOD_NUMBER_OF_COMMITS) {
      value = MAX;
    } else {
      value = (MAX / GOOD_NUMBER_OF_COMMITS) * commits.get();
    }
    scoreValue.set(value)
        .explain("%d commits in the last 3 months results to %.2f points",
            commits.get(), value);

    // if the number of contributors is not available, then return what we have
    if (contributors.isNotApplicable()) {
      return scoreValue
          .confidence(Confidence.MAX / 2)
          .explain("A number of contributors is marked as N/A for some reason. "
              + "That reduces the overall confidence. Please check the data.");
    }
    if (contributors.isUnknown()) {
      return scoreValue
          .confidence(Confidence.MAX / 2)
          .explain("A number of contributors is unknown. "
              + "That reduces the overall score and confidence");
    }

    // it looks strange if a project has commits but no contributors
    if (commits.get() > 0 && contributors.get() <= 0) {
      return scoreValue
          .confidence(Confidence.MAX / 2)
          .explain("It looks strange that the project had commits (%d) "
                  + "but the number of contributors (%d). That reduces the overall confidence. "
                  + "Please check the data.",
              commits.get(), contributors.get());
    }

    // it also looks strange if a project has contributors but no commits
    if (commits.get() == 0 && contributors.get() > 0) {
      return scoreValue
          .confidence(Confidence.MAX / 2)
          .explain("It looks strange that the project has contributors (%d) but no commits. "
              + "That reduces the overall confidence. Please check the data.", contributors.get());
    }

    // otherwise, calculate a factor based on the number of contributors
    double factor = 0.0;
    for (Map.Entry<Integer, Double> entry : CONTRIBUTOR_FACTOR.entrySet()) {
      if (contributors.get() >= entry.getKey()) {
        factor = entry.getValue();
      } else {
        break;
      }
    }
    if (factor > 0) {
      double newValue = value + value * factor;
      scoreValue
          .set(newValue)
          .explain("%d contributors increase the score value from %.2f to %.2f",
              contributors.get(), value, newValue);
    }

    return scoreValue;
  }
}
