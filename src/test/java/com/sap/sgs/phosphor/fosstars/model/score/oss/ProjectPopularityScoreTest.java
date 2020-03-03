package com.sap.sgs.phosphor.fosstars.model.score.oss;

import static com.sap.sgs.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.sgs.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.sgs.phosphor.fosstars.model.other.Utils.setOf;

import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.value.UnknownValue;
import java.util.Set;
import org.junit.Test;

public class ProjectPopularityScoreTest {

  private static final ProjectPopularityScore PROJECT_POPULARITY = new ProjectPopularityScore();
  private static final double delta = 0.001;

  @Test(expected = IllegalArgumentException.class)
  public void negativeStars() {
    PROJECT_POPULARITY.calculate(
        NUMBER_OF_GITHUB_STARS.value(-1), NUMBER_OF_WATCHERS_ON_GITHUB.value(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeWatchers() {
    PROJECT_POPULARITY.calculate(
        NUMBER_OF_GITHUB_STARS.value(1), NUMBER_OF_WATCHERS_ON_GITHUB.value(-1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noStars() {
    PROJECT_POPULARITY.calculate(NUMBER_OF_WATCHERS_ON_GITHUB.value(1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void noWatchers() {
    PROJECT_POPULARITY.calculate(NUMBER_OF_GITHUB_STARS.value(1));
  }

  @Test
  public void calculate() {
    assertScore(Score.MIN,
        PROJECT_POPULARITY.calculate(
            UnknownValue.of(NUMBER_OF_GITHUB_STARS),
            UnknownValue.of(NUMBER_OF_WATCHERS_ON_GITHUB)));

    assertScore(Score.MIN,
        PROJECT_POPULARITY.calculate(
            NUMBER_OF_GITHUB_STARS.value(0),
            NUMBER_OF_WATCHERS_ON_GITHUB.value(0)));

    assertScore(Score.MAX,
        PROJECT_POPULARITY.calculate(
            NUMBER_OF_GITHUB_STARS.value(Integer.MAX_VALUE),
            NUMBER_OF_WATCHERS_ON_GITHUB.value(Integer.MAX_VALUE)));

    // no watchers
    assertScore(0.001, PROJECT_POPULARITY.calculate(values(1, 0)), delta);
    assertScore(0.010, PROJECT_POPULARITY.calculate(values(10, 0)), delta);
    assertScore(0.100, PROJECT_POPULARITY.calculate(values(100, 0)), delta);
    assertScore(1.000, PROJECT_POPULARITY.calculate(values(1000, 0)), delta);
    assertScore(5.000, PROJECT_POPULARITY.calculate(values(5000, 0)), delta);
    assertScore(Score.MAX, PROJECT_POPULARITY.calculate(values(10000, 0)), delta);
    assertScore(Score.MAX, PROJECT_POPULARITY.calculate(values(15000, 0)), delta);

    // no stars
    assertScore(0.003, PROJECT_POPULARITY.calculate(values(0, 1)), delta);
    assertScore(0.033, PROJECT_POPULARITY.calculate(values(0, 10)), delta);
    assertScore(0.333, PROJECT_POPULARITY.calculate(values(0, 100)), delta);
    assertScore(3.333, PROJECT_POPULARITY.calculate(values(0, 1000)), delta);
    assertScore(6.666, PROJECT_POPULARITY.calculate(values(0, 2000)), delta);
    assertScore(8.333, PROJECT_POPULARITY.calculate(values(0, 2500)), delta);
    assertScore(Score.MAX, PROJECT_POPULARITY.calculate(values(0, 3000)), delta);
    assertScore(Score.MAX, PROJECT_POPULARITY.calculate(values(0, 5000)), delta);

    // both stars and watchers
    assertScore(0.133, PROJECT_POPULARITY.calculate(values(100, 10)), delta);
    assertScore(1.100, PROJECT_POPULARITY.calculate(values(100, 300)), delta);
    assertScore(2.666, PROJECT_POPULARITY.calculate(values(2000, 200)), delta);
    assertScore(3.833, PROJECT_POPULARITY.calculate(values(500, 1000)), delta);
    assertScore(4.000, PROJECT_POPULARITY.calculate(values(3000, 300)), delta);
    assertScore(4.333, PROJECT_POPULARITY.calculate(values(1000, 1000)), delta);
    assertScore(4.333, PROJECT_POPULARITY.calculate(values(1000, 1000)), delta);
    assertScore(6.000, PROJECT_POPULARITY.calculate(values(5000, 300)), delta);
    assertScore(6.000, PROJECT_POPULARITY.calculate(values(1000, 1500)), delta);
    assertScore(8.333, PROJECT_POPULARITY.calculate(values(5000, 1000)), delta);
    assertScore(8.666, PROJECT_POPULARITY.calculate(values(2000, 2000)), delta);
    assertScore(Score.MAX, PROJECT_POPULARITY.calculate(values(5000, 4000)), delta);
    assertScore(Score.MAX, PROJECT_POPULARITY.calculate(values(11000, 1000)), delta);
  }

  private static Set<Value> values(int stars, int watchers) {
    return setOf(
        NUMBER_OF_GITHUB_STARS.value(stars),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(watchers));
  }
}