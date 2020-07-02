package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.findValue;

import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.math.MathHelper;
import com.sap.sgs.phosphor.fosstars.model.qa.ScoreVerification;
import com.sap.sgs.phosphor.fosstars.model.qa.TestVectors;
import com.sap.sgs.phosphor.fosstars.model.score.FeatureBasedScore;
import com.sap.sgs.phosphor.fosstars.model.value.ScoreValue;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

/**
 * <p>The project popularity score is currently based on two features.</p>
 * <ul>
 *   <li>Number of stars on GitHub</li>
 *   <li>Number of watchers on GitHub</li>
 * </ul>
 * <p>First, it calculates a stars sub-score. Next, it calculates a watchers sub-score.
 * It uses linear functions to transform the numbers to sub-scores.
 * Then, the sub-scores are added to each other.</p>
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

  /**
   * A description of the score.
   */
  private static final String DESCRIPTION;

  static {
    StringBuilder sb = new StringBuilder();

    sb.append("The score is based on number of stars and watchers.\n");

    Function<Integer, Double> starsSubScore = n -> starsSubScore(NUMBER_OF_GITHUB_STARS.value(n));
    sb.append("Here is how a number of stars contributes to the score:\n");
    sb.append(String.format("%d -> %2.2f (min), ", 0, starsSubScore.apply(0)));
    sb.append(String.format("%d -> %2.2f, %d -> %2.2f, ",
        MathHelper.invert(starsSubScore, 0, BEST_STARS_AMOUNT, 2.5, 0.01), 2.5,
        MathHelper.invert(starsSubScore, 0, BEST_STARS_AMOUNT, 5.0, 0.01), 5.0));
    sb.append(String.format("%d -> %2.2f (max)",
        BEST_STARS_AMOUNT, starsSubScore.apply(BEST_STARS_AMOUNT)));
    sb.append("\n");

    Function<Integer, Double> watchersSubScore
        = n -> watchersSubScore(NUMBER_OF_WATCHERS_ON_GITHUB.value(n));
    sb.append("Here is how a number of watchers contributes to the score:\n");
    sb.append(String.format("%d -> %2.2f (min), ",
        0, watchersSubScore.apply(0)));
    sb.append(String.format("%d -> %2.2f, %d -> %2.2f, ",
        MathHelper.invert(watchersSubScore, 0, BEST_WATCHERS_AMOUNT, 1.5, 0.01), 1.5,
        MathHelper.invert(watchersSubScore, 0, BEST_WATCHERS_AMOUNT, 2.5, 0.01), 2.5));
    sb.append(String.format("%d -> %2.2f (max)",
        BEST_WATCHERS_AMOUNT, watchersSubScore.apply(BEST_WATCHERS_AMOUNT)));

    DESCRIPTION = sb.toString();
  }

  /**
   * Initializes a new score.
   */
  ProjectPopularityScore() {
    super("Open-source project popularity score", DESCRIPTION,
        NUMBER_OF_GITHUB_STARS, NUMBER_OF_WATCHERS_ON_GITHUB);
  }

  @Override
  public ScoreValue calculate(Value... values) {
    Value<Integer> n = findValue(values, NUMBER_OF_GITHUB_STARS,
        "Hey! You have to give me a number of stars!");
    Value<Integer> m = findValue(values, NUMBER_OF_WATCHERS_ON_GITHUB,
        "Hey! You have to give me a number of watchers!");

    return scoreValue(starsSubScore(n) + watchersSubScore(m), n, m);
  }

  /**
   * Calculates a stars sub-score.
   *
   * @param stars A number of stars.
   * @throws IllegalArgumentException if a number of stars is negative.
   */
  private static double starsSubScore(Value<Integer> stars) {
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
  private static double watchersSubScore(Value<Integer> watchers) {
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
    private static final String TEST_VECTORS_YAML = "ProjectPopularityScoreTestVectors.yml";

    /**
     * Initializes a {@link Verification} for a {@link ProjectPopularityScore}.
     *
     * @param score A score to be verified.
     * @param vectors A list of test vectors.
     */
    public Verification(ProjectPopularityScore score, TestVectors vectors) {
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
      try (InputStream is = Verification.class.getResourceAsStream(TEST_VECTORS_YAML)) {
        return new Verification(score, TestVectors.loadFromYaml(is));
      }
    }
  }
}
