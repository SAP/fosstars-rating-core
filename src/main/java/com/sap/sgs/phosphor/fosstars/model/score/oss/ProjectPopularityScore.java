package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVector;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * The project popularity score is currently based on two features:
 * <ul>
 *   <li>number of stars on GitHub</li>
 *   <li>number of watchers on GitHub</li>
 * </ul>
 * First, it calculates a stars sub-score. Next, it calculates a watchers sub-score.
 * It uses linear functions to transform the numbers to sub-scores.
 * Then, the sub-scores are added to each other.
 */
public class ProjectPopularityScore extends FeatureBasedScore {

  /**
   * If a number of stars is more than this value, then the maximum score is returned.
   */
  private static final int BEST_STARS_AMOUNT = 10000;

  /**
   * Defines the linear growth for the stars sub-score
   * in case a number of stars is less than bestStarsAmount.
   */
  private static final double STARS_SCORE_FACTOR = MAX / BEST_STARS_AMOUNT;

  /**
   * If a number of watchers is more than this value, then the maximum score is returned.
   */
  private static final int BEST_WATCHERS_AMOUNT = 3000;

  /**
   * Defines the linear growth for the watchers sub-score
   * in case a number of watchers is less than bestWatchersAmount.
   */
  private static final double WATCHERS_SCORE_FACTOR = MAX / BEST_WATCHERS_AMOUNT;

  ProjectPopularityScore() {
    super("Open-source project popularity score",
        NUMBER_OF_GITHUB_STARS, NUMBER_OF_WATCHERS_ON_GITHUB);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Integer> n = findValue(values, NUMBER_OF_GITHUB_STARS,
        "Hey! You have to give me a number of stars!");
    Value<Integer> m = findValue(values, NUMBER_OF_WATCHERS_ON_GITHUB,
        "Hey! You have to give me a number of watchers!");

    return scoreValue(starsScore(n) + watchersScore(m), n, m);
  }

  /**
   * Calculates a stars sub-score.
   *
   * @param stars A number of stars.
   * @throws IllegalArgumentException if a number of stars is negative.
   */
  private double starsScore(Value<Integer> stars) {
    if (stars.isUnknown()) {
      return 0.0;
    }
    int n = stars.get();
    if (n < 0) {
      throw new IllegalArgumentException("Number of stars is negative! How can it be possible?");
    }

    if (n < BEST_STARS_AMOUNT) {
      return STARS_SCORE_FACTOR * n;
    }

    return MAX;
  }

  /**
   * Calculates a watchers sub-score.
   *
   * @param watchers A number of watchers.
   * @throws IllegalArgumentException if a number of watchers is negative.
   */
  private double watchersScore(Value<Integer> watchers) {
    if (watchers.isUnknown()) {
      return 0.0;
    }
    int m = watchers.get();
    if (m < 0) {
      throw new IllegalArgumentException("Number of watchers is negative! How can it be possible?");
    }

    if (m < BEST_WATCHERS_AMOUNT) {
      return WATCHERS_SCORE_FACTOR * m;
    }

    return MAX;
  }

  /**
   * This class implements a verification procedure for {@link ProjectPopularityScore}.
   * The class loads test vectors, and provides methods to verify a {@link ProjectPopularityScore}
   * against those test vectors.
   */
  public static class Verification extends ScoreVerification {

    /**
     * A name of a resource which contains the test vectors.
     */
    private static final String DEFAULT_TEST_VECTORS_CSV = "ProjectPopularityScoreTestVectors.csv";

    /**
     * Initializes a {@link Verification} for a {@link ProjectPopularityScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(ProjectPopularityScore score, List<TestVector> vectors) {
      super(score, vectors);
    }

    /**
     * Creates an instance of {@link Verification} for a specified score. The method loads test
     * vectors from a default resource.
     *
     * @param score The score to be verified.
     * @return An instance of {@link Verification}.
     */
    static Verification createFor(ProjectPopularityScore score) throws IOException {
      try (InputStream is = Verification.class.getResourceAsStream(DEFAULT_TEST_VECTORS_CSV)) {

        return new Verification(
            score, loadTestVectorsFromCsvResource(score.features(), is));
      }
    }
  }
}
