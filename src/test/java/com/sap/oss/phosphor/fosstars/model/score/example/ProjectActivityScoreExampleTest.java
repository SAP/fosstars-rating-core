package com.sap.oss.phosphor.fosstars.model.score.example;

import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures.NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.value.IntegerValue;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ProjectActivityScoreExampleTest {

  private static Set<Value<?>> makeValues(
      int numberOfCommitsLastMonth, int numberOfContributorsLastMonth) {

    Set<Value<?>> values = new HashSet<>();
    values.add(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE.value(numberOfCommitsLastMonth));
    values.add(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE.value(numberOfContributorsLastMonth));
    return values;
  }

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

  @Test
  public void negativeCommitsNumber() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          ProjectActivityScoreExample score = PROJECT_ACTIVITY_SCORE_EXAMPLE;
          Set<Value<?>> values = new HashSet<>();
          values.add(new IntegerValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, -1));
          values.add(new IntegerValue(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 11));
          Score.INTERVAL.contains(score.calculate(values).get());
        });
  }

  @Test
  public void negativeContributorsNumber() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Set<Value<?>> values = new HashSet<>();
          values.add(new IntegerValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 11));
          values.add(new IntegerValue(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, -1));
          Score.INTERVAL.contains(PROJECT_ACTIVITY_SCORE_EXAMPLE.calculate(values).get());
        });
  }

  @Test
  public void noCommitsNumber() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Set<Value<?>> values = new HashSet<>();
          values.add(new IntegerValue(NUMBER_OF_CONTRIBUTORS_LAST_MONTH_EXAMPLE, 11));
          Score.INTERVAL.contains(PROJECT_ACTIVITY_SCORE_EXAMPLE.calculate(values).get());
        });
  }

  @Test
  public void noContributorsNumber() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Set<Value<?>> values = new HashSet<>();
          values.add(new IntegerValue(NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE, 11));
          Score.INTERVAL.contains(PROJECT_ACTIVITY_SCORE_EXAMPLE.calculate(values).get());
        });
  }
}
