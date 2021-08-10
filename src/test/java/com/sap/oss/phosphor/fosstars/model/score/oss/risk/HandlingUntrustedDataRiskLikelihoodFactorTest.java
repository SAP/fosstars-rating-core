package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.Likelihood.HIGH;
import static com.sap.oss.phosphor.fosstars.model.feature.Likelihood.LOW;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.HANDLING_UNTRUSTED_DATA_LIKELIHOOD;
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

public class HandlingUntrustedDataRiskLikelihoodFactorTest {

  private static final HandlingUntrustedDataRiskLikelihoodFactor SCORE
      = new HandlingUntrustedDataRiskLikelihoodFactor();

  @Test
  public void testJsonSerialization() throws IOException {
    HandlingUntrustedDataRiskLikelihoodFactor clone
        = Json.read(Json.toBytes(SCORE), HandlingUntrustedDataRiskLikelihoodFactor.class);
    assertTrue(SCORE.equals(clone) && clone.equals(SCORE));
    assertEquals(SCORE.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    HandlingUntrustedDataRiskLikelihoodFactor clone
        = Yaml.read(Yaml.toBytes(SCORE), HandlingUntrustedDataRiskLikelihoodFactor.class);
    assertEquals(clone, SCORE);
  }

  @Test
  public void testCalculate() {
    ScoreValue scoreValue = SCORE.calculate(
        new EnumValue<>(HANDLING_UNTRUSTED_DATA_LIKELIHOOD, LOW));
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(scoreValue.confidence(), Confidence.MAX, DELTA);

    assertTrue(SCORE.calculate(HANDLING_UNTRUSTED_DATA_LIKELIHOOD.unknown()).isUnknown());
  }

  @Test
  public void testScoreValueSerialization() throws IOException {
    ScoreValue scoreValue = SCORE.calculate(
        new EnumValue<>(HANDLING_UNTRUSTED_DATA_LIKELIHOOD, HIGH));
    ScoreValue clone = Json.read(Json.toBytes(scoreValue), ScoreValue.class);
    assertTrue(scoreValue.equals(clone) && clone.equals(scoreValue));
    assertEquals(scoreValue.hashCode(), clone.hashCode());
  }
}