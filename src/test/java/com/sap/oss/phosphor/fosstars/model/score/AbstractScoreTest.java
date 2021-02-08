package com.sap.oss.phosphor.fosstars.model.score;

import static com.sap.oss.phosphor.fosstars.model.other.Utils.setOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.sap.oss.phosphor.fosstars.model.Feature;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.Value;
import com.sap.oss.phosphor.fosstars.model.feature.example.ExampleFeatures;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import java.util.Collections;
import java.util.Set;
import org.junit.Test;

public class AbstractScoreTest {

  @Test
  public void testEquals() {
    ScoreImpl one = new ScoreImpl("test", "Fine! I'll go build my own lunar lander ...");
    assertEquals(one, one);
    assertEquals(one.hashCode(), one.hashCode());

    ScoreImpl two = new ScoreImpl("test", "Fine! I'll go build my own lunar lander ...");
    assertEquals(one, two);
    assertEquals(one.hashCode(), two.hashCode());

    ScoreImpl three = new ScoreImpl("test", "Oh, no room for Bender, eh?");
    assertNotEquals(one, three);

    ScoreImpl four = new ScoreImpl("Bender", "Oh, no room for Bender, eh?");
    assertNotEquals(three, four);
  }

  private static class ScoreImpl extends AbstractScore {

    ScoreImpl(String name, String description) {
      super(name, description);
    }

    @Override
    public Set<Feature<?>> features() {
      return setOf(
          ExampleFeatures.STATIC_CODE_ANALYSIS_DONE_EXAMPLE,
          ExampleFeatures.NUMBER_OF_COMMITS_LAST_MONTH_EXAMPLE);
    }

    @Override
    public Set<Score> subScores() {
      return Collections.emptySet();
    }

    @Override
    public ScoreValue calculate(Value<?>... values) {
      return scoreValue(1.23);
    }
  }
}