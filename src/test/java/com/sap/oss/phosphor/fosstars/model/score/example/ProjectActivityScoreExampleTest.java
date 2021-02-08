package com.sap.oss.phosphor.fosstars.model.score.example;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.IntegerValue;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class ProjectActivityScoreExampleTest {

  @Test
  public void name() {
    assertNotNull(PROJECT_ACTIVITY_SCORE_EXAMPLE.name());
  }

  @Test
  public void equalsAndHashCode() {
    ProjectActivityScoreExample one = PROJECT_ACTIVITY_SCORE_EXAMPLE;
    ProjectActivityScoreExample two = PROJECT_ACTIVITY_SCORE_EXAMPLE;
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());
  }

  @Test
  public void calculate() {
    Set<Value<?>> values = makeValues(1, 2);
    assertTrue(Score.INTERVAL.contains(PROJECT_ACTIVITY_SCORE_EXAMPLE.calculate(values).get()));
    values = makeValues(0, 0);
    assertTrue(Score.INTERVAL.contains(PROJECT_ACTIVITY_SCORE_EXAMPLE.calculate(values).get()));
    values = makeValues(Integer.MAX_VALUE, Integer.MAX_VALUE);
    assertTrue(Score.INTERVAL.contains(PROJECT_ACTIVITY_SCORE_EXAMPLE.calculate(values).get()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeCommitsNumber() {
    ProjectActivityScoreExample score = PROJECT_ACTIVITY_SCORE_EXAMPLE;
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, -1));
    values.add(new IntegerValue(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 11));
    Score.INTERVAL.contains(score.calculate(values).get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void negativeContributorsNumber() {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 11));
    values.add(new IntegerValue(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, -1));
    Score.INTERVAL.contains(PROJECT_ACTIVITY_SCORE_EXAMPLE.calculate(values).get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void noCommitsNumber() {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 11));
    Score.INTERVAL.contains(PROJECT_ACTIVITY_SCORE_EXAMPLE.calculate(values).get());
  }

  @Test(expected = IllegalArgumentException.class)
  public void noContributorsNumber() {
    Set<Value<?>> values = new HashSet<>();
    values.add(new IntegerValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 11));
    Score.INTERVAL.contains(PROJECT_ACTIVITY_SCORE_EXAMPLE.calculate(values).get());
  }

  private static Set<Value<?>> makeValues(
      int numberOfCommitsLastMonth, int numberOfContributorsLastMonth) {

    Set<Value<?>> values = new HashSet<>();
    values.add(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(numberOfCommitsLastMonth));
    values.add(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(numberOfContributorsLastMonth));
    return values;
  }

}