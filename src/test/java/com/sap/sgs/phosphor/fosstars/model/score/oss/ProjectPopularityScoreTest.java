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
        PROJECT_POPULARITY, setOf(
            UnknownValue.of(NUMBER_OF_GITHUB_STARS),
            UnknownValue.of(NUMBER_OF_WATCHERS_ON_GITHUB)));

    assertScore(Score.MIN,
        PROJECT_POPULARITY, setOf(
            NUMBER_OF_GITHUB_STARS.value(0),
            NUMBER_OF_WATCHERS_ON_GITHUB.value(0)));

    assertScore(Score.MAX,
        PROJECT_POPULARITY, setOf(
            NUMBER_OF_GITHUB_STARS.value(Integer.MAX_VALUE),
            NUMBER_OF_WATCHERS_ON_GITHUB.value(Integer.MAX_VALUE)));

    // no watchers
    assertScore(0.001, PROJECT_POPULARITY, values(1, 0));
    assertScore(0.010, PROJECT_POPULARITY, values(10, 0));
    assertScore(0.100, PROJECT_POPULARITY, values(100, 0));
    assertScore(1.000, PROJECT_POPULARITY, values(1000, 0));
    assertScore(5.000, PROJECT_POPULARITY, values(5000, 0));
    assertScore(Score.MAX, PROJECT_POPULARITY, values(10000, 0));
    assertScore(Score.MAX, PROJECT_POPULARITY, values(15000, 0));

    // no stars
    assertScore(0.003, PROJECT_POPULARITY, values(0, 1));
    assertScore(0.033, PROJECT_POPULARITY, values(0, 10));
    assertScore(0.333, PROJECT_POPULARITY, values(0, 100));
    assertScore(3.333, PROJECT_POPULARITY, values(0, 1000));
    assertScore(6.666, PROJECT_POPULARITY, values(0, 2000));
    assertScore(8.333, PROJECT_POPULARITY, values(0, 2500));
    assertScore(Score.MAX, PROJECT_POPULARITY, values(0, 3000));
    assertScore(Score.MAX, PROJECT_POPULARITY, values(0, 5000));

    // both stars and watchers
    assertScore(0.133, PROJECT_POPULARITY, values(100, 10));
    assertScore(1.100, PROJECT_POPULARITY, values(100, 300));
    assertScore(2.666, PROJECT_POPULARITY, values(2000, 200));
    assertScore(3.833, PROJECT_POPULARITY, values(500, 1000));
    assertScore(4.000, PROJECT_POPULARITY, values(3000, 300));
    assertScore(4.333, PROJECT_POPULARITY, values(1000, 1000));
    assertScore(4.333, PROJECT_POPULARITY, values(1000, 1000));
    assertScore(6.000, PROJECT_POPULARITY, values(5000, 300));
    assertScore(6.000, PROJECT_POPULARITY, values(1000, 1500));
    assertScore(8.333, PROJECT_POPULARITY, values(5000, 1000));
    assertScore(8.666, PROJECT_POPULARITY, values(2000, 2000));
    assertScore(Score.MAX, PROJECT_POPULARITY, values(5000, 4000));
    assertScore(Score.MAX, PROJECT_POPULARITY, values(11000, 1000));
  }

  private static Set<Value> values(int stars, int watchers) {
    return setOf(
        NUMBER_OF_GITHUB_STARS.value(stars),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(watchers));
  }
}