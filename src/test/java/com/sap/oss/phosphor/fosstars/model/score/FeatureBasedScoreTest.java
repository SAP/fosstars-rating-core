package com.sap.oss.phosphor.fosstars.model.score;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.jupiter.api.Test;

public class FeatureBasedScoreTest {

  @Test
  public void testWithScore() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new TestScore("test", ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE);
        });
  }

  private static class TestScore extends FeatureBasedScore {

    TestScore(String name, Feature... features) {
      super(name, features);
    }

    @Override
    public ScoreValue calculate(Value... values) {
      throw new UnsupportedOperationException();
    }
  }
}
