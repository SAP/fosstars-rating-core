package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_COMMITS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Confidence;
import com.sap.sgs.phosphor.fosstars.model.Feature;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.math3.analysis.function.Logistic;

/**
 * The project activity score is currently based on two features:
 * <ul>
 *  <li>number of commits last three months</li>
 *  <li>number of contributors last three months</li>
 * </ul>
 * The score uses the logistic function to transform the numbers to a score value.
 */
public class ProjectActivityScore extends FeatureBasedScore {

  /**
   * y(n) = 7.1 / (1 + e ^ (0.04 * (50 - x))) , where n is a number of commits last three months.
   * y(n) belongs to the interval (0.8, 7.1) when n > 0
   * Number of commits can contribute up to 7 to the overall project activity score.
   */
  static final Logistic LOGISTIC_FOR_NUMBER_OF_COMMITS
      = new Logistic(7.1, 50, 0.04, 1, 0, 1);

  /**
   * y(m) = 3.1 / (1 + e ^ (0.2 * (1 - m))) , where m is a number of contributors last three months.
   * y(m) belongs to the interval (1.3, 3.1) when m > 0
   * Number of contributors can contribute up to 3 to the overall project activity score.
   */
  static final Logistic LOGISTIC_FOR_NUMBER_OF_CONTRIBUTORS
      = new Logistic(3.1, 1, 0.2, 1, 0, 1);

  /**
   * A list of features which are used in the score.
   */
  private static final Feature[] FEATURES = {
      NUMBER_OF_COMMITS_LAST_THREE_MONTHS,
      NUMBER_OF_CONTRIBUTORS_LAST_THREE_MONTHS
  };

  /**
   * Initializes a new score.
   */
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

    return new ScoreValue(this, score, Confidence.make(n, m));
  }

  /**
   * Calculates a sub-score based on a number of commits.
   *
   * @param numberOfCommits The number of commits.
   * @return The sub-score.
   */
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

  /**
   * Calculates a sub-score based on a number of contributors.
   *
   * @param numberOfContributors The number of contributors.
   * @return The sub-score value.
   */
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

    if (m > n) {
      throw new IllegalArgumentException("More commits than contributors! How is that possible?");
    }

    if (n > 0 && m == 0) {
      throw new IllegalArgumentException("Commits without contributor! How is that possible?");
    }
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
    private static final String DEFAULT_TEST_VECTORS_CSV = "ProjectActivityScoreTestVectors.csv";

    /**
     * Initializes a {@link Verification} for a {@link ProjectActivityScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(ProjectActivityScore score, List<TestVector> vectors) {
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
      try (InputStream is = Verification.class.getResourceAsStream(DEFAULT_TEST_VECTORS_CSV)) {

        return new Verification(
            score, loadTestVectorsFromCsvResource(score.features(), is));
      }
    }
  }

}
