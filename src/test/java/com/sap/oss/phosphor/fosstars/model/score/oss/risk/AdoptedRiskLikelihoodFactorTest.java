package com.sap.oss.phosphor.fosstars.model.score.oss.risk;

import static com.sap.oss.phosphor.fosstars.TestUtils.DELTA;
import static com.sap.oss.phosphor.fosstars.model.feature.oss.OssRiskFeatures.IS_ADOPTED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sap.oss.phosphor.fosstars.model.Confidence;
import com.sap.oss.phosphor.fosstars.model.Score;
import com.sap.oss.phosphor.fosstars.model.value.ScoreValue;
import com.sap.oss.phosphor.fosstars.util.Json;
import com.sap.oss.phosphor.fosstars.util.Yaml;
import java.io.IOException;
import org.junit.Test;

public class AdoptedRiskLikelihoodFactorTest {

  private static final AdoptedRiskLikelihoodFactor SCORE = new AdoptedRiskLikelihoodFactor();

  @Test
  public void testJsonSerialization() throws IOException {
    AdoptedRiskLikelihoodFactor clone
        = Json.read(Json.toBytes(SCORE), AdoptedRiskLikelihoodFactor.class);
    assertTrue(SCORE.equals(clone) && clone.equals(SCORE));
    assertEquals(SCORE.hashCode(), clone.hashCode());
  }

  @Test
  public void testYamlSerialization() throws IOException {
    AdoptedRiskLikelihoodFactor clone
        = Yaml.read(Yaml.toBytes(SCORE), AdoptedRiskLikelihoodFactor.class);
    assertEquals(clone, SCORE);
  }

  @Test
  public void testCalculate() {
    ScoreValue scoreValue = SCORE.calculate(IS_ADOPTED.yes());
    assertFalse(scoreValue.isUnknown());
    assertFalse(scoreValue.isNotApplicable());
    assertTrue(Score.INTERVAL.contains(scoreValue.get()));
    assertEquals(scoreValue.confidence(), Confidence.MAX, DELTA);

    assertTrue(SCORE.calculate(IS_ADOPTED.unknown()).isUnknown());
  }

  @Test
  public void testScoreValueSerialization() throws IOException {
    ScoreValue scoreValue = SCORE.calculate(IS_ADOPTED.no());
    ScoreValue clone = Json.read(Json.toBytes(scoreValue), ScoreValue.class);
    assertTrue(scoreValue.equals(clone) && clone.equals(scoreValue));
    assertEquals(scoreValue.hashCode(), clone.hashCode());
  }
}