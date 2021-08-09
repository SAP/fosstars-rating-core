package com.sap.oss.phosphor.fosstars.model.score.oss;

import static com.sap.oss.phosphor.fosstars.TestUtils.assertScore;
import static com.sap.oss.phosphor.fosstars.model.Score.MAX;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_GITHUB_STARS;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssFeatures.NUMBER_OF_WATCHERS_ON_GITHUB;
import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.other.Utils;
import java.util.Set;
import org.junit.Test;

public class ProjectPopularityScoreTest {

  private static final ProjectPopularityScore SCORE = new ProjectPopularityScore();

  @Test(expected = IllegalArgumentException.class)
  public void testWithNegativeStars() {
    SCORE.calculate(
        NUMBER_OF_GITHUB_STARS.value(-1),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(1),
        NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNegativeWatchers() {
    SCORE.calculate(
        NUMBER_OF_GITHUB_STARS.value(1),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(-1),
        NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutStars() {
    SCORE.calculate(
        NUMBER_OF_WATCHERS_ON_GITHUB.value(1),
        NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithoutWatchers() {
    SCORE.calculate(
        NUMBER_OF_GITHUB_STARS.value(1),
        NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(10));
  }

  @Test
  public void testWithAllUnknown() {
    assertTrue(SCORE.calculate(Utils.allUnknown(SCORE.allFeatures())).isUnknown());
  }

  @Test
  public void testCalculate() {
    assertScore(Score.MIN,
        SCORE, setOf(
            NUMBER_OF_GITHUB_STARS.value(0),
            NUMBER_OF_WATCHERS_ON_GITHUB.value(0),
            NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(0)));

    assertScore(MAX,
        SCORE, setOf(
            NUMBER_OF_GITHUB_STARS.value(Integer.MAX_VALUE),
            NUMBER_OF_WATCHERS_ON_GITHUB.value(Integer.MAX_VALUE),
            NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(Integer.MAX_VALUE)));

    // no watchers
    assertScore(0.03, SCORE, values(1, 0, 50));
    assertScore(0.04, SCORE, values(10, 0, 50));
    assertScore(0.13, SCORE, values(100, 0, 50));
    assertScore(1.03, SCORE, values(1000, 0, 50));
    assertScore(5.03, SCORE, values(5000, 0, 50));
    assertScore(MAX, SCORE, values(10000, 0, 50));
    assertScore(MAX, SCORE, values(15000, 0, 50));

    // no stars
    assertScore(0.003, SCORE, values(0, 1, 10));
    assertScore(0.033, SCORE, values(0, 10, 10));
    assertScore(0.333, SCORE, values(0, 100, 10));
    assertScore(3.333, SCORE, values(0, 1000, 10));
    assertScore(6.666, SCORE, values(0, 2000, 10));
    assertScore(8.333, SCORE, values(0, 2500, 10));
    assertScore(MAX, SCORE, values(0, 3000, 10));
    assertScore(MAX, SCORE, values(0, 5000, 10));

    // a project have stars, watchers and dependents
    assertScore(0.13, SCORE, values(100, 10, 10));
    assertScore(1.12, SCORE, values(100, 300, 30));
    assertScore(2.80, SCORE, values(2000, 200, 200));
    assertScore(4.16, SCORE, values(500, 1000, 500));
    assertScore(4.33, SCORE, values(3000, 300, 500));
    assertScore(5.66, SCORE, values(1000, 1000, 2000));
    assertScore(6.33, SCORE, values(1000, 1000, 3000));
    assertScore(8.66, SCORE, values(1000, 1500, 4000));
    assertScore(MAX, SCORE, values(5000, 300, 7000));
    assertScore(MAX, SCORE, values(5000, 1000, 6000));
    assertScore(MAX, SCORE, values(2000, 2000, 5000));
    assertScore(MAX, SCORE, values(5000, 4000, 10000));
    assertScore(MAX, SCORE, values(11000, 1000, 10000));
  }

  @Test
  public void testDescription() {
    assertNotNull(SCORE.description());
    assertFalse(SCORE.description().isEmpty());
  }

  private static Set<Value<?>> values(int stars, int watchers, int dependents) {
    return setOf(
        NUMBER_OF_GITHUB_STARS.value(stars),
        NUMBER_OF_WATCHERS_ON_GITHUB.value(watchers),
        NUMBER_OF_DEPENDENT_PROJECTS_ON_GITHUB.value(dependents));
  }
}