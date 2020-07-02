package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.math.MathHelper;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Logistic;

/**
 * <p>The project activity score is currently based on two features.</p>
 * <ul>
 *  <li>Number of commits in the last three months.</li>
 *  <li>Number of contributors in the last three months.</li>
 * </ul>
 * <p>The score uses the logistic function to transform the numbers to a score value.</p>
 */
public class ProjectActivityScore extends FeatureBasedScore {

  /**
   * The maximum sub-score based on a number of commits in the last three month.
   */
  private static final double MAX_COMMITS_SUB_SCORE = 5.1;

  /**
   * A logistic function which is used to convert a number of commits to a sub-score:
   * <pre>y(n) = MAX_COMMITS_SUB_SCORE / (1 + e ^ (0.02 * (200 - n)))</pre>,
   * where n is a number of commits in the last three months.
   * The function belongs to the interval [0.0, MAX_COMMITS_SUB_SCORE) when n >= 0.
   */
  private static final Logistic LOGISTIC_FOR_NUMBER_OF_COMMITS
      = logistic(MAX_COMMITS_SUB_SCORE, 0.02, 200);

  /**
   * The maximum sub-score based on a number of contributors in the last three months.
   */
  private static final double MAX_CONTRIBUTORS_SUB_SCORE = 5.1;

  /**
   * A shift on the Y-axis for the logistic function for the number of contributors.
   */
  private static final double MAX_CONTRIBUTORS_SUB_SCORE_SHIFT = -1;

  /**
   * A logistic function which is used to convert a number of contributors to a sub-score:
   * <pre>y(m) = A / (1 + e ^ (0.4 * (4 - m)))</pre>,
   * where m is a number of contributors in the last three months,
   * and A = MAX_CONTRIBUTORS_SUB_SCORE - MAX_CONTRIBUTORS_SUB_SCORE_SHIFT.
   * The function belongs to the interval [0.0, MAX_CONTRIBUTORS_SUB_SCORE) when m >= 0.
   */
  private static final Logistic LOGISTIC_FOR_NUMBER_OF_CONTRIBUTORS
      = logistic(MAX_CONTRIBUTORS_SUB_SCORE - MAX_CONTRIBUTORS_SUB_SCORE_SHIFT, 0.4, 4);

  /**
   * A final function which is used to convert a number of contributors to a sub-score:
   * <pre>y(m) = LOGISTIC_FOR_NUMBER_OF_CONTRIBUTORS(m) - MAX_CONTRIBUTORS_SUB_SCORE_SHIFT</pre>.
   */
  private static final UnivariateFunction NUMBER_OF_CONTRIBUTORS_SUB_SCORE =
      m -> LOGISTIC_FOR_NUMBER_OF_CONTRIBUTORS.value(m) + MAX_CONTRIBUTORS_SUB_SCORE_SHIFT;

  /**
   * Returns  logistic function y(x) = a / (1 + e ^ k(b - x)).
   *
   * @return The logistic function.
   */
  private static Logistic logistic(double a, double k, double b) {
    return new Logistic(a, b, k, 1, 0, 1);
  }

  /**
   * A description of the score.
   */
  private static final String DESCRIPTION;

  static {
    StringBuilder sb = new StringBuilder();

    sb.append("The score is based on number of commits and contributors.\n");
    sb.append(String.format(
        "Here is how the number of commits contributes to the score (up to %2.2f):\n",
        MAX_COMMITS_SUB_SCORE));
    sb.append(printInvertedValues(
        LOGISTIC_FOR_NUMBER_OF_COMMITS::value, 0, 1000,
        0.1, MAX_COMMITS_SUB_SCORE * 0.5, MAX_COMMITS_SUB_SCORE * 0.9));

    sb.append("\n");
    sb.append(String.format(
        "Here is how the number of contributors contributes to the score (up to %2.2f):\n",
        MAX_CONTRIBUTORS_SUB_SCORE));
    sb.append(printInvertedValues(
        NUMBER_OF_CONTRIBUTORS_SUB_SCORE::value, 0, 200,
        0.1, MAX_CONTRIBUTORS_SUB_SCORE * 0.5, MAX_CONTRIBUTORS_SUB_SCORE * 0.9));

    DESCRIPTION = sb.toString();
  }

  /**
   * Initializes a new score.
   */
  ProjectActivityScore() {
    super("Open-source project activity score", DESCRIPTION,
        NUMBER_OF_COMMITS_LAST_THREE_MONTHS, NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Integer> n = findValue(values, NUMBER_OF_COMMITS_LAST_THREE_MONTHS,
        "Hey! You have to give me a number of commits!");
    Value<Integer> m = findValue(values, NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS,
        "Hey! You have to give me a number of contributors!");

    check(n, m);

    return scoreValue(commitsPoints(n) + contributorsPoints(m), n, m);
  }

  /**
   * Calculates a sub-score based on a number of commits.
   *
   * @param numberOfCommits The number of commits.
   * @return The sub-score.
   */
  private static double commitsPoints(Value<Integer> numberOfCommits) {
    if (numberOfCommits.isUnknown()) {
      return 0.0;
    }

    int n = numberOfCommits.get();
    if (n < 0) {
      throw new IllegalArgumentException(
          "Hey! You are're not supposed to give me a negative number of commits!");
    }
    if (n == 0) {
      return 0.0;
    }

    return LOGISTIC_FOR_NUMBER_OF_COMMITS.value(n);
  }

  /**
   * Calculates a sub-score based on a number of contributors.
   *
   * @param numberOfContributors The number of contributors.
   * @return The sub-score value.
   */
  private static double contributorsPoints(Value<Integer> numberOfContributors) {
    if (numberOfContributors.isUnknown()) {
      return 0.0;
    }

    int n = numberOfContributors.get();
    if (n < 0) {
      throw new IllegalArgumentException(
          "Hey! You are're not supposed to give me a negative number of contributors!");
    }
    if (n == 0) {
      return 0;
    }

    return LOGISTIC_FOR_NUMBER_OF_CONTRIBUTORS.value(n);
  }

  /**
   * Checks a number of contributors and commits.
   *
   * @param commits The number of commits.
   * @param contributors The number of contributors.
   * @throws IllegalArgumentException If the numbers are not correct.
   */
  private static void check(Value<Integer> commits, Value<Integer> contributors) {
    if (commits.isUnknown() || contributors.isUnknown()) {
      return;
    }

    int n = commits.get();
    int m = contributors.get();

    if (m > 0 && n == 0) {
      throw new IllegalArgumentException("Contributor without commits! How is that possible?");
    }

    if (n > 0 && m == 0) {
      throw new IllegalArgumentException("Commits without contributor! How is that possible?");
    }
  }

  /**
   * Inverts a number of values of a function on a specified interval and prints them out.
   *
   * @param func The function to be inverted.
   * @param a A beginning of the interval.
   * @param b A end of the interval.
   * @param values The values of the function to be inverted.
   * @return A string with inverted values.
   */
  private static String printInvertedValues(
      Function<Integer, Double> func, int a, int b, double... values) {

    List<String> strings = new ArrayList<>();
    for (double value : values) {
      strings.add(String.format("%d -> %2.2f",
          MathHelper.invert(func, a, b, value, 0.01), value));
    }
    return String.join(", ", strings);
  }

  /**
   * This class implements a verification procedure for {@link ProjectActivityScore}.
   * The class loads test vectors, and provides methods to verify a {@link ProjectActivityScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String TEST_VECTORS_YAML = "ProjectActivityScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link ProjectActivityScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(ProjectActivityScore score, TestVectors vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link Verification}.
     */
    static Verification createFor(ProjectActivityScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }

}
