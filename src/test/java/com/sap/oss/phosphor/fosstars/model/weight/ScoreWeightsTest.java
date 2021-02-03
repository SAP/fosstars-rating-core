package com.sap.oss.phosphor.fosstars.model.weight;

import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE;
import static com.sap.oss.phosphor.fosstars.model.score.example.ExampleScores.SECURITY_TESTING_SCORE_EXAMPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import org.junit.Test;

public class ScoreWeightsTest {

  private static final double PRECISION = 0.01;

  @Test
  public void testGetAndSet() {
    ScoreWeights weights = ScoreWeights.createFor(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, SECURITY_TESTING_SCORE_EXAMPLE);
    assertEquals(2, weights.size());
    assertTrue(weights.of(PROJECT_ACTIVITY_SCORE_EXAMPLE).isPresent());
    assertTrue(weights.of(SECURITY_TESTING_SCORE_EXAMPLE).isPresent());
    assertEquals(
        ScoreWeights.DEFAULT_WEIGHT,
        weights.of(PROJECT_ACTIVITY_SCORE_EXAMPLE).get().value(),
        PRECISION);
    assertEquals(
        ScoreWeights.DEFAULT_WEIGHT,
        weights.of(SECURITY_TESTING_SCORE_EXAMPLE).get().value(),
        PRECISION);
    weights.set(PROJECT_ACTIVITY_SCORE_EXAMPLE, new MutableWeight(0.2));
    weights.set(SECURITY_TESTING_SCORE_EXAMPLE, new MutableWeight(0.7));
    assertEquals(
        0.2,
        weights.of(PROJECT_ACTIVITY_SCORE_EXAMPLE).get().value(),
        PRECISION);
    assertEquals(
        0.7,
        weights.of(SECURITY_TESTING_SCORE_EXAMPLE).get().value(),
        PRECISION);
  }

  @Test
  public void testDeserializationYaml() throws IOException {
    ScoreWeights weights = ScoreWeights.createFor(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, SECURITY_TESTING_SCORE_EXAMPLE);
    weights.set(PROJECT_ACTIVITY_SCORE_EXAMPLE, new MutableWeight(0.2));
    weights.set(SECURITY_TESTING_SCORE_EXAMPLE, new MutableWeight(0.7));

    ScoreWeights clone = Yaml.read(Yaml.toBytes(weights), ScoreWeights.class);
    assertTrue(weights.equals(clone) && clone.equals(weights));
    assertEquals(weights.hashCode(), clone.hashCode());
  }

  @Test
  public void testMakeImmutable() {
    ScoreWeights weights = ScoreWeights.createFor(
        PROJECT_ACTIVITY_SCORE_EXAMPLE, SECURITY_TESTING_SCORE_EXAMPLE);
    weights.makeImmutable();
    assertTrue(weights.isImmutable());
  }

}