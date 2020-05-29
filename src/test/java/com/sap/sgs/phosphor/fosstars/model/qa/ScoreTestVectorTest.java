package com.sap.sgs.phosphor.fosstars.model.qa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.sap.sgs.phosphor.fosstars.model.Interval;
import com.sap.sgs.phosphor.fosstars.model.Score;
import com.sap.sgs.phosphor.fosstars.model.Value;
import com.sap.sgs.phosphor.fosstars.model.math.DoubleInterval;
import com.sap.sgs.phosphor.fosstars.model.score.example.ExampleScores;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class ScoreTestVectorTest {

  @Test(expected = UnsupportedOperationException.class)
  public void testValues() {
    Map<Class<? extends Score>, Double> values = new HashMap<>();
    values.put(ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE.getClass(), 5.3);
    values.put(ExampleScores.SECURITY_TESTING_SCORE_EXAMPLE.getClass(), 4.0);

    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    ScoreTestVector vector = new ScoreTestVector(
        values, expectedScore, null, "test", false);

    vector.values();
  }

  @Test
  public void testValuesFor() {
    Map<Class<? extends Score>, Double> values = new HashMap<>();
    values.put(ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE.getClass(), 5.3);
    values.put(ExampleScores.SECURITY_TESTING_SCORE_EXAMPLE.getClass(), 4.0);

    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    ScoreTestVector vector = new ScoreTestVector(
        values, expectedScore, null, "test", false);

    Set<Value> set = vector.valuesFor(ExampleScores.SECURITY_SCORE_EXAMPLE);
    assertNotNull(set);
    assertEquals(2, set.size());
    assertTrue(set.contains(ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE.value(5.3)));
    assertTrue(set.contains(ExampleScores.SECURITY_TESTING_SCORE_EXAMPLE.value(4.0)));
  }

  @Test
  public void testYamlSerializeAndDeserialize() throws IOException {
    Map<Class<? extends Score>, Double> values = new HashMap<>();
    values.put(ExampleScores.PROJECT_ACTIVITY_SCORE_EXAMPLE.getClass(), 5.3);
    values.put(ExampleScores.SECURITY_TESTING_SCORE_EXAMPLE.getClass(), 4.0);

    Interval expectedScore = DoubleInterval.init().from(4.0).to(6.4).closed().make();
    ScoreTestVector vector = new ScoreTestVector(
        values, expectedScore, null, "test", false);

    YAMLFactory factory = new YAMLFactory();
    factory.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
    ObjectMapper mapper = new ObjectMapper(factory);

    byte[] bytes = mapper.writeValueAsBytes(vector);
    assertNotNull(bytes);
    assertTrue(bytes.length > 0);

    ScoreTestVector clone = mapper.readValue(bytes, ScoreTestVector.class);
    assertEquals(vector, clone);
    assertEquals(vector.hashCode(), clone.hashCode());
  }

}