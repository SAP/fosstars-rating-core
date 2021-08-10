package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality.SDK;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.Functionality.SECURITY;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.FUNCTIONALITY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.value.EnumValue;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import org.junit.Test;

public class FunctionalityRiskLikelihoodFactorTest {

  private static final FunctionalityRiskLikelihoodFactor SCORE
      = new FunctionalityRiskLikelihoodFactor();

  @Test
  public void testJsonSerialization() throws IOException {
    FunctionalityRiskLikelihoodFactor clone
        = Json.read(Json.toBytes(SCORE), FunctionalityRiskLikelihoodFactor.class);
    assertTrue(SCORE.equals(clone) && clone.equals(SCORE));
    assertEquals(SCORE.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    FunctionalityRiskLikelihoodFactor clone
        = Yaml.read(Yaml.toBytes(SCORE), FunctionalityRiskLikelihoodFactor.class);
    assertEquals(clone, SCORE);
  }

  @Test
  public void testCalculate() {
    ScoreValue scoreValue = SCORE.calculate(
        new EnumValue<>(FUNCTIONALITY, SDK));
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(scoreValue.confidence(), Confidence.MAX, DELTA);

    assertTrue(SCORE.calculate(FUNCTIONALITY.unknown()).isUnknown());
  }

  @Test
  public void testScoreValueSerialization() throws IOException {
    ScoreValue scoreValue = SCORE.calculate(
        new EnumValue<>(FUNCTIONALITY, SECURITY));
    ScoreValue clone = Json.read(Json.toBytes(scoreValue), ScoreValue.class);
    assertTrue(scoreValue.equals(clone) && clone.equals(scoreValue));
    assertEquals(scoreValue.hashCode(), clone.hashCode());
  }
}