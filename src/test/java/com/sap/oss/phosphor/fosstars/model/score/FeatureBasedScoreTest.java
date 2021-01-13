package com.sap.oss.phosphor.fosstars.model.score;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import org.junit.Test;

public class FeatureBasedScoreTest {

  @Test(expected = IllegalArgumentException.class)
  public void testWithScore() {
    new TestScore("test", ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE);
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